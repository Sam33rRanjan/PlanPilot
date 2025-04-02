package incture.planPilot.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import incture.planPilot.dto.TaskDto;
import incture.planPilot.entity.User;
import incture.planPilot.service.user.UserService;
import incture.planPilot.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/addTask")
	public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		TaskDto task = userService.createTask(loggedInUser, taskDto);
		if(task != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(task);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be created");			
	}
	
	@GetMapping("/getTasks")
	public ResponseEntity<?> getTasks() {
		User loggedInUser = jwtUtil.getLoggedInUser();
		List<TaskDto> tasks = userService.getTasksByUserId(loggedInUser.getId());
		return ResponseEntity.status(HttpStatus.OK).body(tasks);		
	}
	
	@DeleteMapping("/deleteTask/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable long taskId) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		String response = userService.deleteTask(loggedInUser,taskId);
		if(response != null) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be deleted");		
	}
	
	@GetMapping("/getTask/{taskId}")
	public ResponseEntity<?> getTask(@PathVariable long taskId) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		TaskDto task = userService.getTaskById(loggedInUser,taskId);
		if(task != null) {
			return ResponseEntity.status(HttpStatus.OK).body(task);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be found");		
	}
	
	@PutMapping("/updateTask")
	public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		TaskDto task = userService.updateTask(loggedInUser,taskDto);
		if(task != null) {
			return ResponseEntity.status(HttpStatus.OK).body(task);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be updated");	
	}
	
	@GetMapping("/searchTasks/{body}")
	public ResponseEntity<?> searchTasks(@PathVariable String body) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		List<TaskDto> tasks = userService.searchTasks(loggedInUser,body);
		return ResponseEntity.status(HttpStatus.OK).body(tasks);			
	}
	
	@GetMapping("/filterTaskByPriority/{priority}")
	public ResponseEntity<?> filterTaskByPriority(@PathVariable String priority) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		List<TaskDto> tasks = userService.filterTaskByPriority(loggedInUser,priority);
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}
	
	@GetMapping("/filterTaskByStatus/{status}")
	public ResponseEntity<?> filterTaskByStatus(@PathVariable String status) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		List<TaskDto> tasks = userService.filterTaskByStatus(loggedInUser,status);
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}
	
}
