package incture.planPilot.service.user;

import java.util.List;
import java.util.Optional;

import incture.planPilot.dto.TaskDto;
import incture.planPilot.entity.User;

public interface UserService {
	
	TaskDto createTask(User loggedInUser, TaskDto taskDto);

	Optional<User> getUserByEmail(String email);
	
	List<TaskDto> getTasksByUserId(long userId);
	
	String deleteTask(User loggedInUser, long taskId);
	
	TaskDto getTaskById(User loggedInUser, long taskId);
	
	TaskDto updateTask(User loggedInUser, TaskDto taskDto);
	
	List<TaskDto> searchTasks(User loggedInUser, String body);

	List<TaskDto> filterTaskByPriority(User loggedInUser, String priority);

	List<TaskDto> filterTaskByStatus(User loggedInUser, String status);
	
}
