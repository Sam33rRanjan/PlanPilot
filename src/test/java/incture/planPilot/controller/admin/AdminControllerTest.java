package incture.planPilot.controller.admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.dto.UserDto;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.UserRole;
import incture.planPilot.service.admin.AdminService;

@SpringBootTest
class AdminControllerTest {

	@InjectMocks
	private AdminController adminController;
	
	@Mock
	private AdminService adminService;
	
	private static List<UserDto> users = new ArrayList<UserDto>();
	
	private static TaskDto taskDto = new TaskDto();
	private static TaskDto taskDto1 = new TaskDto();
	
	private static List<TaskDto> tasks = new ArrayList<TaskDto>();
	
	private static DashboardDto dashboardDto = new DashboardDto();
	
	@BeforeAll
	public static void setUp() {
		UserDto user1 = new UserDto();
		user1.setId(1);
		user1.setUsername("John Doe");
		user1.setEmail("email1");
		user1.setPassword("password1");
		user1.setUserRole(UserRole.ADMIN);
		users.add(user1);
		taskDto.setId(1);
		taskDto.setTitle("Task 1");
		taskDto.setDescription("Task 1 description");
		taskDto.setPriority(TaskPriority.HIGH);
		taskDto.setUserId(1);
		tasks.add(taskDto);
		taskDto1.setId(2);
		taskDto1.setTitle("Task 2");
		taskDto1.setDescription("Task 2 description");
		taskDto1.setPriority(TaskPriority.LOW);
		taskDto1.setUserId(2);
		tasks.add(taskDto1);
		dashboardDto.setTotalTasks(2);
		dashboardDto.setLowPriorityTasks(1);
		dashboardDto.setMediumPriorityTasks(2);
		dashboardDto.setHighPriorityTasks(3);
		dashboardDto.setUrgentTasks(4);
		dashboardDto.setPendingTasks(4);
		dashboardDto.setInProgressTasks(5);
		dashboardDto.setCompletedTasks(6);
		dashboardDto.setCancelledTasks(7);
		dashboardDto.setOverdueTasks(8);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetAllUsers() {
		when(adminService.getAllUsers()).thenReturn(users);
		List<UserDto> userList = (List<UserDto>) adminController.getAllUsers().getBody();
		assertEquals(1, userList.size());
		assertEquals("John Doe", userList.get(0).getUsername());
		assertEquals("email1", userList.get(0).getEmail());
		assertEquals("password1", userList.get(0).getPassword());
		assertEquals(UserRole.ADMIN, userList.get(0).getUserRole());
	}
	
	@Test
	void testAssignTask() {
		when(adminService.assignTask(taskDto)).thenReturn(taskDto);
		TaskDto assignedTask = (TaskDto) adminController.assignTask(taskDto).getBody();
		assertEquals(taskDto, assignedTask);
		assertEquals(1, assignedTask.getId());
		assertEquals("Task 1", assignedTask.getTitle());
		assertEquals("Task 1 description", assignedTask.getDescription());
		assertEquals(TaskPriority.HIGH, assignedTask.getPriority());
		assertEquals(1, assignedTask.getUserId());
		assertNull(adminController.assignTask(taskDto1).getBody());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetAllTasks() {
		when(adminService.getAllTasks()).thenReturn(tasks);
		List<TaskDto> taskList = (List<TaskDto>) adminController.getAllTasks().getBody();
		assertEquals(2, taskList.size());
		assertEquals("Task 1", taskList.get(0).getTitle());
		assertEquals("Task 1 description", taskList.get(0).getDescription());
		assertEquals(TaskPriority.HIGH, taskList.get(0).getPriority());
		assertEquals(1, taskList.get(0).getUserId());
		assertEquals("Task 2", taskList.get(1).getTitle());
		assertEquals("Task 2 description", taskList.get(1).getDescription());
		assertEquals(TaskPriority.LOW, taskList.get(1).getPriority());
		assertEquals(2, taskList.get(1).getUserId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetAllTasksSortedByDueDate() {
		when(adminService.getAllTasksSortedByDueDate()).thenReturn(tasks);
		List<TaskDto> taskList = (List<TaskDto>) adminController.getAllTasksSortedByDueDate().getBody();
		assertEquals(2, taskList.size());
		assertEquals("Task 1", taskList.get(0).getTitle());
		assertEquals("Task 1 description", taskList.get(0).getDescription());
		assertEquals(TaskPriority.HIGH, taskList.get(0).getPriority());
		assertEquals(1, taskList.get(0).getUserId());
		assertEquals("Task 2", taskList.get(1).getTitle());
		assertEquals("Task 2 description", taskList.get(1).getDescription());
		assertEquals(TaskPriority.LOW, taskList.get(1).getPriority());
		assertEquals(2, taskList.get(1).getUserId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetAllTasksSortedByPriority() {
		when(adminService.getAllTasksSortedByPriority()).thenReturn(tasks);
		List<TaskDto> taskList = (List<TaskDto>) adminController.getAllTasksSortedByPriority().getBody();
		assertEquals(2, taskList.size());
		assertEquals("Task 1", taskList.get(0).getTitle());
		assertEquals("Task 1 description", taskList.get(0).getDescription());
		assertEquals(TaskPriority.HIGH, taskList.get(0).getPriority());
		assertEquals(1, taskList.get(0).getUserId());
		assertEquals("Task 2", taskList.get(1).getTitle());
		assertEquals("Task 2 description", taskList.get(1).getDescription());
		assertEquals(TaskPriority.LOW, taskList.get(1).getPriority());
		assertEquals(2, taskList.get(1).getUserId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetTasksByUserId() {
		when(adminService.getTasksByUserId(1)).thenReturn(tasks);
		List<TaskDto> taskList = (List<TaskDto>) adminController.getTasksByUserId(1).getBody();
		assertEquals(2, taskList.size());
		assertEquals("Task 1", taskList.get(0).getTitle());
		assertEquals("Task 1 description", taskList.get(0).getDescription());
		assertEquals(TaskPriority.HIGH, taskList.get(0).getPriority());
		assertEquals(1, taskList.get(0).getUserId());
		assertEquals("Task 2", taskList.get(1).getTitle());
		assertEquals("Task 2 description", taskList.get(1).getDescription());
		assertEquals(TaskPriority.LOW, taskList.get(1).getPriority());
		assertEquals(2, taskList.get(1).getUserId());
	}
	
	@Test
	void testDeleteTask() {
		when(adminService.deleteTask(1)).thenReturn("Task deleted successfully");
		String response = (String) adminController.deleteTask(1).getBody();
		assertEquals("Task deleted successfully", response);
		assertNull(adminController.deleteTask(3).getBody());
	}
	
	@Test
	void testGetTask() {
		when(adminService.getTaskById(1)).thenReturn(taskDto);
		TaskDto task = (TaskDto) adminController.getTask(1).getBody();
		assertEquals(taskDto, task);
		assertEquals(1, task.getId());
		assertEquals("Task 1", task.getTitle());
		assertEquals("Task 1 description", task.getDescription());
		assertEquals(TaskPriority.HIGH, task.getPriority());
		assertEquals(1, task.getUserId());
		assertNull(adminController.getTask(3).getBody());
	}
	
	@Test
	void testUpdateTask() {
		when(adminService.updateTask(taskDto)).thenReturn(taskDto1);
		TaskDto updatedTask = (TaskDto) adminController.updateTask(taskDto).getBody();
		assertEquals(taskDto1, updatedTask);
		assertEquals(2, updatedTask.getId());
		assertEquals("Task 2", updatedTask.getTitle());
		assertEquals("Task 2 description", updatedTask.getDescription());
		assertEquals(TaskPriority.LOW, updatedTask.getPriority());
		assertEquals(2, updatedTask.getUserId());
		assertNull(adminController.updateTask(taskDto1).getBody());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testSearchTask() {
		when(adminService.searchTask("Task")).thenReturn(tasks);
		List<TaskDto> taskList = (List<TaskDto>) adminController.searchTask("Task").getBody();
		assertEquals(2, taskList.size());
		assertEquals("Task 1", taskList.get(0).getTitle());
		assertEquals("Task 1 description", taskList.get(0).getDescription());
		assertEquals(TaskPriority.HIGH, taskList.get(0).getPriority());
		assertEquals(1, taskList.get(0).getUserId());
		assertEquals("Task 2", taskList.get(1).getTitle());
		assertEquals("Task 2 description", taskList.get(1).getDescription());
		assertEquals(TaskPriority.LOW, taskList.get(1).getPriority());
		assertEquals(2, taskList.get(1).getUserId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testFilterTaskByPriority() {
		when(adminService.filterTaskByPriority("HIGH")).thenReturn(tasks);
		List<TaskDto> taskList = (List<TaskDto>) adminController.filterTaskByPriority("HIGH").getBody();
		assertEquals(2, taskList.size());
		assertEquals("Task 1", taskList.get(0).getTitle());
		assertEquals("Task 1 description", taskList.get(0).getDescription());
		assertEquals(TaskPriority.HIGH, taskList.get(0).getPriority());
		assertEquals(1, taskList.get(0).getUserId());
		assertEquals("Task 2", taskList.get(1).getTitle());
		assertEquals("Task 2 description", taskList.get(1).getDescription());
		assertEquals(TaskPriority.LOW, taskList.get(1).getPriority());
		assertEquals(2, taskList.get(1).getUserId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testFilterTaskByStatus() {
		when(adminService.filterTaskByStatus("COMPLETED")).thenReturn(tasks);
		List<TaskDto> taskList = (List<TaskDto>) adminController.filterTaskByStatus("COMPLETED").getBody();
		assertEquals(2, taskList.size());
		assertEquals("Task 1", taskList.get(0).getTitle());
		assertEquals("Task 1 description", taskList.get(0).getDescription());
		assertEquals(TaskPriority.HIGH, taskList.get(0).getPriority());
		assertEquals(1, taskList.get(0).getUserId());
		assertEquals("Task 2", taskList.get(1).getTitle());
		assertEquals("Task 2 description", taskList.get(1).getDescription());
		assertEquals(TaskPriority.LOW, taskList.get(1).getPriority());
		assertEquals(2, taskList.get(1).getUserId());
	}
	
	@Test
	void testGetDashboardData() {
		when(adminService.getDashboardData()).thenReturn(dashboardDto);
		DashboardDto dashboardData = (DashboardDto) adminController.getDashboardData().getBody();
		assertEquals(dashboardDto, dashboardData);
		assertEquals(2, dashboardData.getTotalTasks());
		assertEquals(1, dashboardData.getLowPriorityTasks());
		assertEquals(2, dashboardData.getMediumPriorityTasks());
		assertEquals(3, dashboardData.getHighPriorityTasks());
		assertEquals(4, dashboardData.getUrgentTasks());
		assertEquals(4, dashboardData.getPendingTasks());
		assertEquals(5, dashboardData.getInProgressTasks());
		assertEquals(6, dashboardData.getCompletedTasks());
		assertEquals(7, dashboardData.getCancelledTasks());
		assertEquals(8, dashboardData.getOverdueTasks());
	}
	
	@Test
	void testGetUserDashboardData() {
		when(adminService.getUserDashboardData(1)).thenReturn(dashboardDto);
		DashboardDto userDashboardData = (DashboardDto) adminController.getUserDashboardData(1).getBody();
		assertEquals(dashboardDto, userDashboardData);
		assertEquals(2, userDashboardData.getTotalTasks());
		assertEquals(1, userDashboardData.getLowPriorityTasks());
		assertEquals(2, userDashboardData.getMediumPriorityTasks());
		assertEquals(3, userDashboardData.getHighPriorityTasks());
		assertEquals(4, userDashboardData.getUrgentTasks());
		assertEquals(4, userDashboardData.getPendingTasks());
		assertEquals(5, userDashboardData.getInProgressTasks());
		assertEquals(6, userDashboardData.getCompletedTasks());
		assertEquals(7, userDashboardData.getCancelledTasks());
		assertEquals(8, userDashboardData.getOverdueTasks());
	}

}
