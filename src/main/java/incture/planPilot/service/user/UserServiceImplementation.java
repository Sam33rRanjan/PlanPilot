package incture.planPilot.service.user;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.entity.Task;
import incture.planPilot.entity.User;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);
	
	@Override
	public String updateUser(User loggedInUser, String name, String email, String password) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if(optionalUser.isPresent() && !optionalUser.get().equals(loggedInUser)) {
			logger.error("Email is already registered: " + email);
			return "Email is registered with another user";
		}
		Optional<User> optionalUserByUsername = userRepository.findByUsername(name);
		if(optionalUserByUsername.isPresent() && !optionalUserByUsername.get().equals(loggedInUser)) {
			logger.error("Username is already registered: " + name);
			return "Username is registered with another user";
		}
		logger.info("Updating user: " + loggedInUser.getUsername());
		loggedInUser.setUsername(name);
		loggedInUser.setEmail(email);
		loggedInUser.setPassword(password);
		userRepository.save(loggedInUser);
		return "User updated successfully";
	}
	
	@Override
	public TaskDto createTask(User loggedInUser, TaskDto taskDto) {
		logger.info("Creating task for user: {}", loggedInUser.getEmail());
		Task task = new Task();
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		task.setUser(loggedInUser);
		logger.debug("Assigned task to user: " + loggedInUser.getUsername());
		loggedInUser.addTask(task);
		userRepository.save(loggedInUser);
		return loggedInUser.getTasks().get(loggedInUser.getTasks().size()-1).getTaskDto();
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		logger.info("Retrieving user by email: " + email);
		return userRepository.findByEmail(email);
	}

	@Override
	public List<TaskDto> getTasksByUserId(long userId) {
		logger.info("Retrieving tasks for user ID: " + userId);
		List<Task> tasks = userRepository.findById(userId).get().getTasks();
		return tasks.stream()
				.map(Task::getTaskDto)
				.toList();
	}
	
	@Override
	public List<TaskDto> getTasksByUserIdSortedByDueDate(long userId) {
		logger.info("Retrieving tasks for user ID: " + userId + " sorted by due date");
		List<Task> tasks = userRepository.findById(userId).get().getTasks();
		return tasks.stream()
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}
	
	@Override
	public List<TaskDto> getTasksByUserIdSortedByPriority(long userId) {
		logger.info("Retrieving tasks for user ID: " + userId + " sorted by priority");
		List<Task> tasks = userRepository.findById(userId).get().getTasks();
		return tasks.stream()
				.sorted(Comparator.comparing(Task::getPriority).reversed())
				.map(Task::getTaskDto)
				.toList();
	}

	@Override
	public String deleteTask(User loggedInUser, long taskId) {
		logger.info("Deleting task with ID: " + taskId + " for user: " + loggedInUser.getUsername());
		loggedInUser.removeTask(taskId);
		userRepository.save(loggedInUser);
		return "Task deleted successfully";
	}

	@Override
	public TaskDto getTaskById(User loggedInUser, long taskId) {
		logger.info("Retrieving task with ID: " + taskId + " for user: " + loggedInUser.getUsername());
		Task task = loggedInUser.getTasks().stream()
				.filter(t -> t.getId() == taskId)
				.findFirst()
				.get();
		return task.getTaskDto();
	}

	@Override
	public TaskDto updateTask(User loggedInUser, TaskDto taskDto) {
		logger.info("Updating task with ID: " + taskDto.getId() + " for user: " + loggedInUser.getUsername());
		Task task = loggedInUser.getTasks().stream()
				.filter(t -> t.getId() == taskDto.getId())
				.findFirst()
				.get();
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		userRepository.save(loggedInUser);
		return task.getTaskDto();
	}

	@Override
	public List<TaskDto> searchTasks(User loggedInUser, String searchString) {
		logger.info("Searching tasks for user: " + loggedInUser.getUsername() + " with search string: " + searchString);
		List<Task> tasks = loggedInUser.getTasks();
		return tasks.stream()
				.filter(t -> t.getTitle().toLowerCase().contains(searchString.toLowerCase()) || t.getDescription().toLowerCase().contains(searchString.toLowerCase()))
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}

	@Override
	public List<TaskDto> filterTaskByPriority(User loggedInUser, String priority) {
		logger.info("Filtering tasks for user: " + loggedInUser.getUsername() + " by priority: " + priority);
		List<Task> tasks = loggedInUser.getTasks();
		return tasks.stream()
				.filter(t -> t.getPriority().name().toLowerCase().equals(priority.toLowerCase()))
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}

	@Override
	public List<TaskDto> filterTaskByStatus(User loggedInUser, String status) {
		logger.info("Filtering tasks for user: " + loggedInUser.getUsername() + " by status: " + status);
		List<Task> tasks = loggedInUser.getTasks();
		return tasks.stream()
				.filter(t -> t.getStatus().name().toLowerCase().equals(status.toLowerCase()))
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}

	@Override
	public DashboardDto getDashboardForUser(User loggedInUser) {
		logger.info("Generating dashboard for user: " + loggedInUser.getUsername());
		logger.debug("Retrieving tasks for " + loggedInUser.getUsername());
		List<Task> tasks = loggedInUser.getTasks();
		logger.debug("Calculating dashboard statistics");
		DashboardDto dashboard = new DashboardDto();
		dashboard.setTotalTasks(tasks.size());
		dashboard.setCompletedTasks(tasks.stream().filter(t -> t.getStatus().equals(TaskStatus.COMPLETED)).count());
		dashboard.setPendingTasks(tasks.stream().filter(t -> t.getStatus().equals(TaskStatus.PENDING)).count());
		Date currentDate = new Date();
		dashboard.setOverdueTasks(tasks.stream()
				.filter(t -> (t.getStatus().equals(TaskStatus.PENDING) || t.getStatus().equals(TaskStatus.IN_PROGRESS)) && t.getDueDate().before(currentDate))
				.count());
		dashboard.setInProgressTasks(tasks.stream().filter(t -> t.getStatus().equals(TaskStatus.IN_PROGRESS)).count());
		dashboard.setCancelledTasks(tasks.stream().filter(t -> t.getStatus().equals(TaskStatus.CANCELLED)).count());
		dashboard.setUrgentTasks(tasks.stream().filter(t -> t.getPriority().equals(TaskPriority.URGENT)).count());
		dashboard.setHighPriorityTasks(tasks.stream().filter(t -> t.getPriority().equals(TaskPriority.HIGH)).count());
		dashboard.setMediumPriorityTasks(tasks.stream().filter(t -> t.getPriority().equals(TaskPriority.MEDIUM)).count());
		dashboard.setLowPriorityTasks(tasks.stream().filter(t -> t.getPriority().equals(TaskPriority.LOW)).count());
		return dashboard;
	}

}
