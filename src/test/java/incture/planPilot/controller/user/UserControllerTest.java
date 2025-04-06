package incture.planPilot.controller.user;

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
import incture.planPilot.dto.NotificationDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.entity.User;
import incture.planPilot.enums.NotificationStatus;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;
import incture.planPilot.service.notification.NotificationService;
import incture.planPilot.service.user.UserService;
import incture.planPilot.util.JwtUtil;

@SpringBootTest
class UserControllerTest {

	@InjectMocks
	UserController userController;
	
	@Mock
	UserService userService;
	@Mock
	JwtUtil jwtUtil;
	@Mock
	NotificationService notificationService;
	
	private static User user = new User();
	
	private static TaskDto taskDto = new TaskDto();
	
	private static List<TaskDto> tasks = new ArrayList<TaskDto>();
	
	private static DashboardDto dashboardDto = new DashboardDto();
	
	private static List<NotificationDto> notifications = new ArrayList<NotificationDto>();
	
	@BeforeAll
	public static void setUp() {
		user.setId(1);
		taskDto.setId(1);
		taskDto.setTitle("Task 1");
		taskDto.setDescription("Task 1 description");
		taskDto.setPriority(TaskPriority.HIGH);
		taskDto.setStatus(TaskStatus.PENDING);
		taskDto.setUserId(1);
		tasks.add(taskDto);
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
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setId(1);
		notificationDto.setDescription("Notification 1");
		notificationDto.setStatus(NotificationStatus.UNREAD);
		notificationDto.setUserId(1);
		notifications.add(notificationDto);
	}
	
	@Test
	void testCreateTask() {
		when(userService.createTask(user, taskDto)).thenReturn(taskDto);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		TaskDto task = (TaskDto) userController.createTask(taskDto).getBody();
		assertEquals(task.getId(), taskDto.getId());
		assertEquals(task.getTitle(), taskDto.getTitle());
		assertEquals(task.getDescription(), taskDto.getDescription());
		assertEquals(task.getPriority(), taskDto.getPriority());
		assertEquals(task.getUserId(), user.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetTasks() {
		when(userService.getTasksByUserId(user.getId())).thenReturn(tasks);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		List<TaskDto> taskList = (List<TaskDto>) userController.getTasks().getBody();
		assertEquals(taskList.size(), tasks.size());
		assertEquals(taskList.get(0).getId(), taskDto.getId());
		assertEquals(taskList.get(0).getTitle(), taskDto.getTitle());
		assertEquals(taskList.get(0).getDescription(), taskDto.getDescription());
		assertEquals(taskList.get(0).getPriority(), taskDto.getPriority());
		assertEquals(taskList.get(0).getUserId(), user.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetTasksSortedByDueDate() {
		when(userService.getTasksByUserIdSortedByDueDate(user.getId())).thenReturn(tasks);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		List<TaskDto> taskList = (List<TaskDto>) userController.getTasksSortedByDueDate().getBody();
		assertEquals(taskList.size(), tasks.size());
		assertEquals(taskList.get(0).getId(), taskDto.getId());
		assertEquals(taskList.get(0).getTitle(), taskDto.getTitle());
		assertEquals(taskList.get(0).getDescription(), taskDto.getDescription());
		assertEquals(taskList.get(0).getPriority(), taskDto.getPriority());
		assertEquals(taskList.get(0).getUserId(), user.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetTasksSortedByPriority() {
		when(userService.getTasksByUserIdSortedByPriority(user.getId())).thenReturn(tasks);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		List<TaskDto> taskList = (List<TaskDto>) userController.getTasksSortedByPriority().getBody();
		assertEquals(taskList.size(), tasks.size());
		assertEquals(taskList.get(0).getId(), taskDto.getId());
		assertEquals(taskList.get(0).getTitle(), taskDto.getTitle());
		assertEquals(taskList.get(0).getDescription(), taskDto.getDescription());
		assertEquals(taskList.get(0).getPriority(), taskDto.getPriority());
		assertEquals(taskList.get(0).getUserId(), user.getId());
	}
	
	@Test
	void testDeleteTask() {
		when(userService.deleteTask(user, taskDto.getId())).thenReturn("Task deleted successfully");
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		String response = (String) userController.deleteTask(taskDto.getId()).getBody();
		assertEquals(response, "Task deleted successfully");
	}
	
	@Test
	void testGetTask() {
		when(userService.getTaskById(user, taskDto.getId())).thenReturn(taskDto);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		TaskDto task = (TaskDto) userController.getTask(taskDto.getId()).getBody();
		assertEquals(task.getId(), taskDto.getId());
		assertEquals(task.getTitle(), taskDto.getTitle());
		assertEquals(task.getDescription(), taskDto.getDescription());
		assertEquals(task.getPriority(), taskDto.getPriority());
		assertEquals(task.getUserId(), user.getId());
	}
	
	@Test
	void testUpdateTask() {
		when(userService.updateTask(user, taskDto)).thenReturn(taskDto);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		TaskDto task = (TaskDto) userController.updateTask(taskDto).getBody();
		assertEquals(task.getId(), taskDto.getId());
		assertEquals(task.getTitle(), taskDto.getTitle());
		assertEquals(task.getDescription(), taskDto.getDescription());
		assertEquals(task.getPriority(), taskDto.getPriority());
		assertEquals(task.getUserId(), user.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testSearchTasks() {
		when(userService.searchTasks(user, taskDto.getTitle())).thenReturn(tasks);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		List<TaskDto> taskList = (List<TaskDto>) userController.searchTasks(taskDto.getTitle()).getBody();
		assertEquals(taskList.size(), tasks.size());
		assertEquals(taskList.get(0).getId(), taskDto.getId());
		assertEquals(taskList.get(0).getTitle(), taskDto.getTitle());
		assertEquals(taskList.get(0).getDescription(), taskDto.getDescription());
		assertEquals(taskList.get(0).getPriority(), taskDto.getPriority());
		assertEquals(taskList.get(0).getUserId(), user.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testFilterTaskByPriority() {
		when(userService.filterTaskByPriority(user, taskDto.getPriority().toString())).thenReturn(tasks);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		List<TaskDto> taskList = (List<TaskDto>) userController.filterTaskByPriority(taskDto.getPriority().toString()).getBody();
		assertEquals(taskList.size(), tasks.size());
		assertEquals(taskList.get(0).getId(), taskDto.getId());
		assertEquals(taskList.get(0).getTitle(), taskDto.getTitle());
		assertEquals(taskList.get(0).getDescription(), taskDto.getDescription());
		assertEquals(taskList.get(0).getPriority(), taskDto.getPriority());
		assertEquals(taskList.get(0).getUserId(), user.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testFilterTaskByStatus() {
		when(userService.filterTaskByStatus(user, taskDto.getStatus().name())).thenReturn(tasks);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		List<TaskDto> taskList = (List<TaskDto>) userController.filterTaskByStatus(taskDto.getStatus().name()).getBody();
		assertEquals(taskList.size(), tasks.size());
		assertEquals(taskList.get(0).getId(), taskDto.getId());
		assertEquals(taskList.get(0).getTitle(), taskDto.getTitle());
		assertEquals(taskList.get(0).getDescription(), taskDto.getDescription());
		assertEquals(taskList.get(0).getPriority(), taskDto.getPriority());
		assertEquals(taskList.get(0).getUserId(), user.getId());
	}
	
	@Test
	void testGetDashboard() {
		when(userService.getDashboardForUser(user)).thenReturn(dashboardDto);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		DashboardDto dashboard = (DashboardDto) userController.getDashboard().getBody();
		assertEquals(dashboard.getTotalTasks(), dashboardDto.getTotalTasks());
		assertEquals(dashboard.getLowPriorityTasks(), dashboardDto.getLowPriorityTasks());
		assertEquals(dashboard.getMediumPriorityTasks(), dashboardDto.getMediumPriorityTasks());
		assertEquals(dashboard.getHighPriorityTasks(), dashboardDto.getHighPriorityTasks());
		assertEquals(dashboard.getUrgentTasks(), dashboardDto.getUrgentTasks());
		assertEquals(dashboard.getPendingTasks(), dashboardDto.getPendingTasks());
		assertEquals(dashboard.getInProgressTasks(), dashboardDto.getInProgressTasks());
		assertEquals(dashboard.getCompletedTasks(), dashboardDto.getCompletedTasks());
		assertEquals(dashboard.getCancelledTasks(), dashboardDto.getCancelledTasks());
		assertEquals(dashboard.getOverdueTasks(), dashboardDto.getOverdueTasks());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testGetNotifications() {
		when(notificationService.getNotificationsByUserId(user.getId())).thenReturn(notifications);
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		List<NotificationDto> notificationList = (List<NotificationDto>) userController.getNotifications().getBody();
		assertEquals(notificationList.size(), notifications.size());
		assertEquals(notificationList.get(0).getId(), notifications.get(0).getId());
		assertEquals(notificationList.get(0).getDescription(), notifications.get(0).getDescription());
		assertEquals(notificationList.get(0).getStatus(), notifications.get(0).getStatus());
		assertEquals(notificationList.get(0).getUserId(), user.getId());
	}
	
	@Test
	void testMarkNotificationAsRead() {
		when(notificationService.markNotificationAsRead(user, notifications.get(0).getId())).thenReturn("Notification marked as read");
		when(jwtUtil.getLoggedInUser()).thenReturn(user);
		String response = (String) userController.markNotificationAsRead(notifications.get(0).getId()).getBody();
		assertEquals(response, "Notification marked as read");
	}

}
