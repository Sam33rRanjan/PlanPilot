package incture.planPilot.service.user;

import java.util.List;
import java.util.Optional;

import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.entity.User;

public interface UserService {
	
	String updateUser(User loggedInUser, String name, String email, String password);
	
	TaskDto createTask(User loggedInUser, TaskDto taskDto);

	Optional<User> getUserByEmail(String email);
	
	List<TaskDto> getTasksByUserId(long userId);
	
	List<TaskDto> getTasksByUserIdSortedByDueDate(long userId);
	
	List<TaskDto> getTasksByUserIdSortedByPriority(long userId);
	
	String deleteTask(User loggedInUser, long taskId);
	
	TaskDto getTaskById(User loggedInUser, long taskId);
	
	TaskDto updateTask(User loggedInUser, TaskDto taskDto);
	
	List<TaskDto> searchTasks(User loggedInUser, String searchString);

	List<TaskDto> filterTaskByPriority(User loggedInUser, String priority);

	List<TaskDto> filterTaskByStatus(User loggedInUser, String status);
	
	DashboardDto getDashboardForUser(User loggedInUser);
	
}
