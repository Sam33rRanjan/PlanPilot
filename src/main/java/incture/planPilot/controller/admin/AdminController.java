package incture.planPilot.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@GetMapping("/users")
	ResponseEntity<?> getAllUsers(){
		logger.info("Retrieving all users through admin controller");
		return ResponseEntity.ok(adminService.getAllUsers());
	}
	
	@PostMapping("/assignTask")
	ResponseEntity<?> assignTask(@RequestBody TaskDto taskDto) {
		logger.info("Assigning task through admin controller");
		TaskDto task = adminService.assignTask(taskDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(task);
	}
	
	@GetMapping("/getAllTasks")
	ResponseEntity<?> getAllTasks(){
		logger.info("Retrieving all tasks through admin controller");
		return ResponseEntity.ok(adminService.getAllTasks());
	}
	
	@GetMapping("/getAllTasksSortedByDueDate")
	ResponseEntity<?> getAllTasksSortedByDueDate(){
		logger.info("Retrieving all tasks sorted by due date through admin controller");
		return ResponseEntity.ok(adminService.getAllTasksSortedByDueDate());
	}
	
	@GetMapping("/getAllTasksSortedByPriority")
	ResponseEntity<?> getAllTasksSortedByPriority(){
		logger.info("Retrieving all tasks sorted by priority through admin controller");
		return ResponseEntity.ok(adminService.getAllTasksSortedByPriority());
	}
	
	@GetMapping("/getTasksByUserId/{userId}")
	ResponseEntity<?> getTasksByUserId(@PathVariable long userId){
		logger.info("Retrieving tasks by user ID through admin controller");
		List<TaskDto> tasks = adminService.getTasksByUserId(userId);
		return ResponseEntity.ok(tasks);
	}
	
	@DeleteMapping("/deleteTask/{taskId}")
	ResponseEntity<?> deleteTask(@PathVariable long taskId) {
		logger.info("Deleting task through admin controller");
		String response = adminService.deleteTask(taskId);
		return ResponseEntity.status(HttpStatus.OK).body(response);	
	}
	
	@GetMapping("/getTask/{taskId}")
	ResponseEntity<?> getTask(@PathVariable long taskId) {
		logger.info("Retrieving task by task ID through admin controller");
		return ResponseEntity.status(HttpStatus.OK).body(adminService.getTaskById(taskId));
	}
	
	@PutMapping("/updateTask")
	ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto) {
		logger.info("Updating task through admin controller");
		TaskDto task = adminService.updateTask(taskDto);
		return ResponseEntity.status(HttpStatus.OK).body(task);		
	}
	
	@GetMapping("searchTask/{searchString}")
	ResponseEntity<?> searchTask(@PathVariable String searchString) {
		logger.info("Searching task through admin controller");
		return ResponseEntity.ok(adminService.searchTask(searchString));
	}
	
	@GetMapping("/filterTaskByPriority/{priority}")
	ResponseEntity<?> filterTaskByPriority(@PathVariable String priority) {
		logger.info("Filtering tasks by priority through admin controller");
		return ResponseEntity.ok(adminService.filterTaskByPriority(priority));
	}
	
	@GetMapping("filterTaskByStatus/{status}")
	ResponseEntity<?> filterTaskByStatus(@PathVariable String status) {
		logger.info("Filtering tasks by status through admin controller");
		return ResponseEntity.ok(adminService.filterTaskByStatus(status));
	}
	
	@GetMapping("/getDashboard")
	ResponseEntity<?> getDashboardData(){
		logger.info("Retrieving dashboard data through admin controller");
		return ResponseEntity.ok(adminService.getDashboardData());
	}
	
	@GetMapping("/getUserDashboard/{userId}")
	ResponseEntity<?> getUserDashboardData(@PathVariable long userId){
		logger.info("Retrieving user dashboard data through admin controller");
		DashboardDto dashboard = adminService.getUserDashboardData(userId);
		return ResponseEntity.ok(dashboard);
	}
	
}
