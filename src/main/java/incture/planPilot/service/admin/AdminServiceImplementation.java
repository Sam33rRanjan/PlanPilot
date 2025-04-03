package incture.planPilot.service.admin;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.TaskRepository;
import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.NotificationDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.dto.UserDto;
import incture.planPilot.entity.Task;
import incture.planPilot.entity.User;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;
import incture.planPilot.enums.UserRole;
import incture.planPilot.service.notification.NotificationService;

@Service
public class AdminServiceImplementation implements AdminService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private NotificationService notificationService;

	@Override
	public List<UserDto> getAllUsers() {
		return userRepository.findByUserRole(UserRole.USER).stream().map(User::getUserDto).collect(Collectors.toList());
	}

	@Override
	public TaskDto assignTask(TaskDto taskDto) {
		Optional<User> optionalUser = userRepository.findById(taskDto.getUserId());
		if(optionalUser.isEmpty()) {
			return null;			
		}
		User user = optionalUser.get();
		Task task = new Task();
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		task.setUser(user);
		user.addTask(task);
		userRepository.save(user);
		Task savedTask = user.getTasks().get(user.getTasks().size()-1);
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setDescription("You have been assigned a new task: " + task.getTitle());
		notificationDto.setUserId(user.getId());
		notificationDto.setTaskId(savedTask.getId());
		notificationService.sendNotification(notificationDto);
		return user.getTasks().get(user.getTasks().size()-1).getTaskDto();
	}

	@Override
	public List<TaskDto> getAllTasks() {
		return taskRepository.findAll().stream()
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TaskDto> getAllTasksSortedByDueDate() {
		return taskRepository.findAll().stream()
				.map(Task::getTaskDto)
				.sorted(Comparator.comparing(TaskDto::getDueDate))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TaskDto> getAllTasksSortedByPriority() {
		return taskRepository.findAll().stream()
				.map(Task::getTaskDto)
				.sorted(Comparator.comparing(TaskDto::getPriority).reversed())
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TaskDto> getTasksByUserId(long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if(optionalUser.isEmpty()) {
			return null;
		}
		return optionalUser.get().getTasks().stream()
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}

	@Override
	public String deleteTask(long taskId) {
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		if(optionalTask.isEmpty()) {
			return null;
		}
		Task task = optionalTask.get();
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setDescription("Your task " + task.getTitle() + " has been deleted");
		notificationDto.setUserId(task.getUser().getId());
		notificationDto.setTaskId(task.getId());
		notificationService.sendNotification(notificationDto);
		taskRepository.delete(task);
		return "Task deleted successfully";
	}

	@Override
	public TaskDto getTaskById(long taskId) {
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		if(optionalTask.isEmpty()) {
			return null;
		}
		return optionalTask.get().getTaskDto();
	}

	@Override
	public TaskDto updateTask(TaskDto taskDto) {
		Optional<Task> optionalTask = taskRepository.findById(taskDto.getId());
		if(optionalTask.isEmpty()) {
			return null;
		}
		Task task = optionalTask.get();
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		User currentUser = task.getUser();
		User newUser = userRepository.findById(taskDto.getUserId()).get();
		task.setUser(newUser);
		currentUser.removeTask(task.getId());
		newUser.addTask(task);
		userRepository.save(currentUser);
		userRepository.save(newUser);
		long newUserId = newUser.getId();
		long currentUserId = currentUser.getId();
		if(newUserId != currentUserId) {
			NotificationDto notificationToCurrentUser = new NotificationDto();
			notificationToCurrentUser.setDescription("Your task " + task.getTitle() + " has been reassigned to another user");
			notificationToCurrentUser.setUserId(currentUserId);
			notificationToCurrentUser.setTaskId(task.getId());
			notificationService.sendNotification(notificationToCurrentUser);
			NotificationDto notificationToNewUser = new NotificationDto();
			notificationToNewUser.setDescription("You have been assigned a new task: " + task.getTitle());
			notificationToNewUser.setUserId(newUserId);
			notificationToNewUser.setTaskId(task.getId());
			notificationService.sendNotification(notificationToNewUser);
		}
		return task.getTaskDto();
	}

	@Override
	public List<TaskDto> searchTask(String searchString) {
		return taskRepository.findAll().stream()
				.filter(task -> task.getTitle().toLowerCase().contains(searchString.toLowerCase()) || task.getDescription().toLowerCase().contains(searchString.toLowerCase()))
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<TaskDto> filterTaskByPriority(String priority) {
		return taskRepository.findAll().stream()
				.filter(task -> task.getPriority().name().toLowerCase().equals(priority.toLowerCase()))
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<TaskDto> filterTaskByStatus(String status) {
		return taskRepository.findAll().stream()
				.filter(task -> task.getStatus().name().toLowerCase().equals(status.toLowerCase()))
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public DashboardDto getDashboardData() {
		DashboardDto dashboard = new DashboardDto();
		List<Task> tasks = taskRepository.findAll();
		Date currentDate = new Date();
		dashboard.setTotalTasks(tasks.size());
		dashboard.setCompletedTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.COMPLETED)).count());
		dashboard.setPendingTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.PENDING)).count());
		dashboard.setOverdueTasks(tasks.stream()
				.filter(t -> (t.getStatus().equals(TaskStatus.PENDING) || t.getStatus().equals(TaskStatus.IN_PROGRESS)) && t.getDueDate().before(currentDate))
				.count());
		dashboard.setInProgressTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.IN_PROGRESS)).count());
		dashboard.setCancelledTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.CANCELLED)).count());
		dashboard.setUrgentTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.URGENT)).count());
		dashboard.setHighPriorityTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.HIGH)).count());
		dashboard.setMediumPriorityTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.MEDIUM)).count());
		dashboard.setLowPriorityTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.LOW)).count());
		return dashboard;
	}
	
	@Override
	public DashboardDto getUserDashboardData(long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if(optionalUser.isEmpty()) {
			return null;
		}
		User user = optionalUser.get();
		DashboardDto dashboard = new DashboardDto();
		List<Task> tasks = user.getTasks();
		Date currentDate = new Date();
		dashboard.setTotalTasks(tasks.size());
		dashboard.setCompletedTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.COMPLETED)).count());
		dashboard.setPendingTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.PENDING)).count());
		dashboard.setOverdueTasks(tasks.stream()
				.filter(t -> (t.getStatus().equals(TaskStatus.PENDING) || t.getStatus().equals(TaskStatus.IN_PROGRESS)) && t.getDueDate().before(currentDate))
				.count());
		dashboard.setInProgressTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.IN_PROGRESS)).count());
		dashboard.setCancelledTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.CANCELLED)).count());
		dashboard.setUrgentTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.URGENT)).count());
		dashboard.setHighPriorityTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.HIGH)).count());
		dashboard.setMediumPriorityTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.MEDIUM)).count());
		dashboard.setLowPriorityTasks(tasks.stream().filter(task -> task.getPriority().equals(TaskPriority.LOW)).count());
		return dashboard;
	}
	
}
