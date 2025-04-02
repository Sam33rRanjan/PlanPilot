package incture.planPilot.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
}
