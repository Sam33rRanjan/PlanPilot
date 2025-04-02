package incture.planPilot.service.user;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.entity.Task;
import incture.planPilot.entity.User;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public TaskDto createTask(User loggedInUser, TaskDto taskDto) {
		Task task = new Task();
		task.setTitle(taskDto.getTitle());
		task.setDescription(taskDto.getDescription());
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(taskDto.getPriority());
		task.setStatus(taskDto.getStatus());
		task.setUser(loggedInUser);
		loggedInUser.addTask(task);
		userRepository.save(loggedInUser);
		return loggedInUser.getTasks().get(loggedInUser.getTasks().size()-1).getTaskDto();
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<TaskDto> getTasksByUserId(long userId) {
		List<Task> tasks = userRepository.findById(userId).get().getTasks();
		return tasks.stream()
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}

	@Override
	public String deleteTask(User loggedInUser, long taskId) {
		try {
			loggedInUser.removeTask(taskId);
			userRepository.save(loggedInUser);
			return "Task deleted successfully";
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public TaskDto getTaskById(User loggedInUser, long taskId) {
		try {
			Task task = loggedInUser.getTasks().stream()
					.filter(t -> t.getId() == taskId)
					.findFirst()
					.get();
			return task.getTaskDto();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public TaskDto updateTask(User loggedInUser, TaskDto taskDto) {
		try {
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
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public List<TaskDto> searchTasks(User loggedInUser, String body) {
		List<Task> tasks = loggedInUser.getTasks();
		return tasks.stream()
				.filter(t -> t.getTitle().toLowerCase().contains(body.toLowerCase()) || t.getDescription().toLowerCase().contains(body.toLowerCase()))
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}

	@Override
	public List<TaskDto> filterTaskByPriority(User loggedInUser, String priority) {
		List<Task> tasks = loggedInUser.getTasks();
		return tasks.stream()
				.filter(t -> t.getPriority().name().toLowerCase().equals(priority.toLowerCase()))
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}

	@Override
	public List<TaskDto> filterTaskByStatus(User loggedInUser, String status) {
		List<Task> tasks = loggedInUser.getTasks();
		return tasks.stream()
				.filter(t -> t.getStatus().name().toLowerCase().equals(status.toLowerCase()))
				.sorted(Comparator.comparing(Task::getDueDate))
				.map(Task::getTaskDto)
				.toList();
	}

}
