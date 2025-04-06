package incture.planPilot.controller.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import incture.planPilot.dto.TaskDto;
import incture.planPilot.dto.UserDto;
import incture.planPilot.entity.User;
import incture.planPilot.service.notification.NotificationService;
import incture.planPilot.service.user.UserService;
import incture.planPilot.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private NotificationService notificationService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/getUser")
	public ResponseEntity<?> getProfile() {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is fetching profile");
		UserDto userDto = loggedInUser.getUserDto();
		return ResponseEntity.status(HttpStatus.OK).body(userDto);		
	}
	
	@PutMapping("/updateUser")
	public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is updating user details");
		String passwordString = new BCryptPasswordEncoder().encode(userDto.getPassword());
		String response = userService.updateUser(loggedInUser, userDto.getUsername(), userDto.getEmail(), passwordString);
		if (response.equals("Email is registered with another user")) {
			logger.error("Email already exists: " + userDto.getEmail());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else if (response.equals("Username is registered with another user")) {
			logger.error("Username already exists: " + userDto.getUsername());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}
			
	@PostMapping("/addTask")
	public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is creating task with title: " + taskDto.getTitle());
		TaskDto task = userService.createTask(loggedInUser, taskDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(task);		
	}
	
	@GetMapping("/getTasks")
	public ResponseEntity<?> getTasks() {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is fetching tasks");
		List<TaskDto> tasks = userService.getTasksByUserId(loggedInUser.getId());
		return ResponseEntity.status(HttpStatus.OK).body(tasks);		
	}
	
	@GetMapping("/getTasksSortedByDueDate")
	public ResponseEntity<?> getTasksSortedByDueDate() {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is fetching tasks sorted by due date");
		List<TaskDto> tasks = userService.getTasksByUserIdSortedByDueDate(loggedInUser.getId());
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}
	
	@GetMapping("/getTasksSortedByPriority")
	public ResponseEntity<?> getTasksSortedByPriority() {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is fetching tasks sorted by priority");
		List<TaskDto> tasks = userService.getTasksByUserIdSortedByPriority(loggedInUser.getId());
		return ResponseEntity.status(HttpStatus.OK).body(tasks);		
	}
	
	@DeleteMapping("/deleteTask/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable long taskId) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is deleting task with ID: " + taskId);
		String response = userService.deleteTask(loggedInUser,taskId);
		return ResponseEntity.status(HttpStatus.OK).body(response);	
	}
	
	@GetMapping("/getTask/{taskId}")
	public ResponseEntity<?> getTask(@PathVariable long taskId) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is fetching task with ID: " + taskId);
		TaskDto task = userService.getTaskById(loggedInUser,taskId);
		return ResponseEntity.status(HttpStatus.OK).body(task);	
	}
	
	@PutMapping("/updateTask")
	public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is updating task with ID: " + taskDto.getId());
		TaskDto task = userService.updateTask(loggedInUser,taskDto);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}
	
	@GetMapping("/searchTask/{searchString}")
	public ResponseEntity<?> searchTasks(@PathVariable String searchString) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is searching tasks with {" + searchString + "} in title or description");
		List<TaskDto> tasks = userService.searchTasks(loggedInUser,searchString);
		return ResponseEntity.status(HttpStatus.OK).body(tasks);			
	}
	
	@GetMapping("/filterTaskByPriority/{priority}")
	public ResponseEntity<?> filterTaskByPriority(@PathVariable String priority) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is filtering tasks by priority: " + priority);
		List<TaskDto> tasks = userService.filterTaskByPriority(loggedInUser,priority);
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}
	
	@GetMapping("/filterTaskByStatus/{status}")
	public ResponseEntity<?> filterTaskByStatus(@PathVariable String status) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is filtering tasks by status: " + status);
		List<TaskDto> tasks = userService.filterTaskByStatus(loggedInUser,status);
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}
	
	@GetMapping("/getDashboard")
	public ResponseEntity<?> getDashboard() {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is fetching dashboard");
		return ResponseEntity.status(HttpStatus.OK).body(userService.getDashboardForUser(loggedInUser));
	}
	
	@GetMapping("/notifications")
	public ResponseEntity<?> getNotifications() {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is fetching notifications");
		return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationsByUserId(loggedInUser.getId()));
	}
	
	@PutMapping("/markNotificationAsRead/{notificationId}")
	public ResponseEntity<?> markNotificationAsRead(@PathVariable long notificationId) {
		User loggedInUser = jwtUtil.getLoggedInUser();
		logger.info(loggedInUser.getUsername() + " is marking notification with ID: " + notificationId + " as read");
		String response = notificationService.markNotificationAsRead(loggedInUser,notificationId);
		return ResponseEntity.status(HttpStatus.OK).body(response);		
	}
	
}
