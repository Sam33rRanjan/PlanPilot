package incture.planPilot.service.admin;

import java.util.List;

import incture.planPilot.dto.TaskDto;
import incture.planPilot.dto.UserDto;

public interface AdminService {
	
	List<UserDto> getAllUsers();

	TaskDto assignTask(TaskDto taskDto);

	List<TaskDto> getAllTasks();
	
}
