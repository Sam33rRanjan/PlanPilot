package incture.planPilot.service.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.TaskRepository;
import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.dto.UserDto;
import incture.planPilot.entity.Task;
import incture.planPilot.entity.User;
import incture.planPilot.enums.UserRole;

@Service
public class AdminServiceImplementation implements AdminService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;

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
		return user.getTasks().get(user.getTasks().size()-1).getTaskDto();
	}

	@Override
	public List<TaskDto> getAllTasks() {
		return taskRepository.findAll().stream()
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}
	
}
