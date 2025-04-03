package incture.planPilot.service.admin;

import java.util.List;

import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.dto.UserDto;

public interface AdminService {
	
	List<UserDto> getAllUsers();

	TaskDto assignTask(TaskDto taskDto);

	List<TaskDto> getAllTasks();
	
	List<TaskDto> getAllTasksSortedByDueDate();
	
	List<TaskDto> getAllTasksSortedByPriority();
	
	List<TaskDto> getTasksByUserId(long userId);
	
	String deleteTask(long taskId);

	TaskDto getTaskById(long taskId);

	TaskDto updateTask(TaskDto taskDto);

	List<TaskDto> searchTask(String searchString);

	List<TaskDto> filterTaskByPriority(String priority);

	List<TaskDto> filterTaskByStatus(String status);
	
	DashboardDto getDashboardData();
	
	DashboardDto getUserDashboardData(long userId);
	
}
