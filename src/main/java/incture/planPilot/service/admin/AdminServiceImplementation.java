package incture.planPilot.service.admin;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import incture.planPilot.service.email.EmailService;
import incture.planPilot.service.notification.NotificationService;

@Service
public class AdminServiceImplementation implements AdminService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private EmailService emailService;
	
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImplementation.class);

	@Override
	public List<UserDto> getAllUsers() {
		logger.info("Fetching all users");
		return userRepository.findAll().stream().map(User::getUserDto).collect(Collectors.toList());
	}

	@Override
	public TaskDto assignTask(TaskDto taskDto) {
		logger.info("Assigning task with title: " + taskDto.getTitle() + " to user with ID: " + taskDto.getUserId());
		logger.debug("Retrieving user with ID: " + taskDto.getUserId());
		Optional<User> optionalUser = userRepository.findById(taskDto.getUserId());
		User user = optionalUser.get();
		logger.debug("Creating new task for user: " + user.getUsername());
		Task task = new Task();
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		task.setUser(user);
		logger.debug("Saving task to database and assigning it to user: " + user.getUsername());
		user.addTask(task);
		userRepository.save(user);
		Task savedTask = user.getTasks().get(user.getTasks().size()-1);
		logger.debug("Sending email notification to user: " + user.getEmail());
		emailService.sendAssignment(user.getEmail(), "New Task Assigned: " + task.getTitle());
		logger.debug("Sending in-app notification to user: " + user.getUsername());
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setDescription("You have been assigned a new task: " + task.getTitle());
		notificationDto.setUserId(user.getId());
		notificationDto.setTaskId(savedTask.getId());
		notificationService.sendNotification(notificationDto);
		return savedTask.getTaskDto();
	}

	@Override
	public List<TaskDto> getAllTasks() {
		logger.info("Retrieving all tasks");
		return taskRepository.findAll().stream()
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TaskDto> getAllTasksSortedByDueDate() {
		logger.info("Retrieving all tasks sorted by due date");
		return taskRepository.findAll().stream()
				.map(Task::getTaskDto)
				.sorted(Comparator.comparing(TaskDto::getDueDate))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TaskDto> getAllTasksSortedByPriority() {
		logger.info("Retrieving all tasks sorted by priority");
		return taskRepository.findAll().stream()
				.map(Task::getTaskDto)
				.sorted(Comparator.comparing(TaskDto::getPriority).reversed())
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TaskDto> getTasksByUserId(long userId) {
		logger.info("Retrieving tasks for user with ID: " + userId);
		Optional<User> optionalUser = userRepository.findById(userId);
		return optionalUser.get().getTasks().stream()
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}

	@Override
	public String deleteTask(long taskId) {
		logger.info("Deleting task with ID: " + taskId);
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		Task task = optionalTask.get();
		logger.debug("Removing task from user: " + task.getUser().getUsername());
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
		logger.info("Retrieving task with ID: " + taskId);
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		return optionalTask.get().getTaskDto();
	}

	@Override
	public TaskDto updateTask(TaskDto taskDto) {
		logger.info("Updating task with ID: " + taskDto.getId());
		Optional<Task> optionalTask = taskRepository.findById(taskDto.getId());
		logger.debug("Retrieving task with ID: " + taskDto.getId());
		Task task = optionalTask.get();
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		User currentUser = task.getUser();
		User newUser = userRepository.findById(taskDto.getUserId()).get();
		logger.debug("Assigning it to user: " + newUser.getUsername());
		task.setUser(newUser);
		logger.debug("Removing task from user: " + currentUser.getUsername());
		currentUser.removeTask(task.getId());
		logger.debug("Adding task to user: " + newUser.getUsername());
		newUser.addTask(task);
		logger.debug("Saving task to database");
		userRepository.save(currentUser);
		userRepository.save(newUser);
		long newUserId = newUser.getId();
		long currentUserId = currentUser.getId();
		if(newUserId != currentUserId) {
			logger.debug("Sending email notification to both users");
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
		logger.info("Searching tasks with string: " + searchString);
		return taskRepository.findAll().stream()
				.filter(task -> task.getTitle().toLowerCase().contains(searchString.toLowerCase()) || task.getDescription().toLowerCase().contains(searchString.toLowerCase()))
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<TaskDto> filterTaskByPriority(String priority) {
		logger.info("Filtering tasks by priority: " + priority);
		return taskRepository.findAll().stream()
				.filter(task -> task.getPriority().name().toLowerCase().equals(priority.toLowerCase()))
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<TaskDto> filterTaskByStatus(String status) {
		logger.info("Filtering tasks by status: " + status);
		return taskRepository.findAll().stream()
				.filter(task -> task.getStatus().name().toLowerCase().equals(status.toLowerCase()))
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public DashboardDto getDashboardData() {
		logger.info("Retrieving dashboard data");
		logger.debug("Fetching all tasks");
		List<Task> tasks = taskRepository.findAll();
		logger.debug("Calculating dashboard data");
		DashboardDto dashboard = new DashboardDto();
		dashboard.setTotalTasks(tasks.size());
		dashboard.setCompletedTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.COMPLETED)).count());
		dashboard.setPendingTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.PENDING)).count());
		Date currentDate = new Date();
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
		logger.info("Retrieving dashboard data for user with ID: " + userId);
		logger.debug("Retrieving user with ID: " + userId);
		Optional<User> optionalUser = userRepository.findById(userId);
		User user = optionalUser.get();
		logger.debug("Retrieving tasks for user: " + user.getUsername());
		List<Task> tasks = user.getTasks();
		logger.debug("Calculating dashboard data for user: " + user.getUsername());
		DashboardDto dashboard = new DashboardDto();
		dashboard.setTotalTasks(tasks.size());
		dashboard.setCompletedTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.COMPLETED)).count());
		dashboard.setPendingTasks(tasks.stream().filter(task -> task.getStatus().equals(TaskStatus.PENDING)).count());
		Date currentDate = new Date();
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
