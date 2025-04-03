package incture.planPilot.controller.admin;

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

import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/users")
	ResponseEntity<?> getAllUsers(){
		return ResponseEntity.ok(adminService.getAllUsers());
	}
	
	@PostMapping("/assignTask")
	ResponseEntity<?> assignTask(@RequestBody TaskDto taskDto) {
		TaskDto task = adminService.assignTask(taskDto);
		if(task != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(task);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be created");			
	}
	
	@GetMapping("/getAllTasks")
	ResponseEntity<?> getAllTasks(){
		return ResponseEntity.ok(adminService.getAllTasks());
	}
	
	@GetMapping("/getAllTasksSortedByDueDate")
	ResponseEntity<?> getAllTasksSortedByDueDate(){
		return ResponseEntity.ok(adminService.getAllTasksSortedByDueDate());
	}
	
	@GetMapping("/getAllTasksSortedByPriority")
	ResponseEntity<?> getAllTasksSortedByPriority(){
		return ResponseEntity.ok(adminService.getAllTasksSortedByPriority());
	}
	
	@GetMapping("/getTasksByUserId/{userId}")
	ResponseEntity<?> getTasksByUserId(@PathVariable long userId){
		List<TaskDto> tasks = adminService.getTasksByUserId(userId);
		if(tasks==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be found");
		}
		return ResponseEntity.ok(tasks);
	}
	
	@DeleteMapping("/deleteTask/{taskId}")
	ResponseEntity<?> deleteTask(@PathVariable long taskId) {
		String response = adminService.deleteTask(taskId);
		if(response != null) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be deleted");		
	}
	
	@GetMapping("/getTask/{taskId}")
	ResponseEntity<?> getTask(@PathVariable long taskId) {
		if(adminService.getTaskById(taskId) != null) {
			return ResponseEntity.status(HttpStatus.OK).body(adminService.getTaskById(taskId));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be found");
	}
	
	@PutMapping("/updateTask")
	ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto) {
		TaskDto task = adminService.updateTask(taskDto);
		if(task != null) {
			return ResponseEntity.status(HttpStatus.OK).body(task);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task could not be updated");			
	}
	
	@GetMapping("searchTask/{searchString}")
	ResponseEntity<?> searchTask(@PathVariable String searchString) {
		return ResponseEntity.ok(adminService.searchTask(searchString));
	}
	
	@GetMapping("/filterTaskByPriority/{priority}")
	ResponseEntity<?> filterTaskByPriority(@PathVariable String priority) {
		return ResponseEntity.ok(adminService.filterTaskByPriority(priority));
	}
	
	@GetMapping("filterTaskByStatus/{status}")
	ResponseEntity<?> filterTaskByStatus(@PathVariable String status) {
		return ResponseEntity.ok(adminService.filterTaskByStatus(status));
	}
	
	@GetMapping("/getDashboard")
	ResponseEntity<?> getDashboardData(){
		return ResponseEntity.ok(adminService.getDashboardData());
	}
	
	@GetMapping("/getUserDashboard/{userId}")
	ResponseEntity<?> getUserDashboardData(@PathVariable long userId){
		DashboardDto dashboard = adminService.getUserDashboardData(userId);
		if(dashboard == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be found");
		}
		return ResponseEntity.ok(dashboard);
	}
	
}
