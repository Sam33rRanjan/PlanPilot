package incture.planPilot.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.entity.Task;
import incture.planPilot.entity.User;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;
import incture.planPilot.enums.UserRole;

@SpringBootTest
class UserServiceImplementationTest {

	@InjectMocks
	private UserServiceImplementation userServiceImplementation;
	
	@Mock
	private UserRepository userRepository;
	
	private static User loggedInUser = new User();
	private static List<Task> tasks = new ArrayList<Task>();
	
	private static TaskDto taskDto = new TaskDto();
	
	@BeforeAll
	public static void setUp() {
		loggedInUser.setId(1L);
		loggedInUser.setEmail("email1");
		loggedInUser.setUsername("username1");
		loggedInUser.setPassword("password1");
		loggedInUser.setUserRole(UserRole.USER);
		loggedInUser.setTasks(tasks);
		
		taskDto.setId(1L);
		taskDto.setTitle("title1");
		taskDto.setDescription("description1");
		taskDto.setDueDate(new Date());
		taskDto.setPriority(TaskPriority.HIGH);
		taskDto.setStatus(TaskStatus.PENDING);
		
		Task task = new Task();
		task.setId(1L);
		task.setTitle("title1");
		task.setDescription("description1");
		task.setDueDate(taskDto.getDueDate());
		task.setPriority(TaskPriority.HIGH);
		task.setStatus(TaskStatus.PENDING);
		task.setUser(loggedInUser);
		tasks.add(task);
	}
	
	@Test
	void testCreateTask() {
		TaskDto createdTask = userServiceImplementation.createTask(loggedInUser, taskDto);
		assertNotNull(createdTask);
		assertEquals(taskDto.getTitle(), createdTask.getTitle());
		assertEquals(taskDto.getDescription(), createdTask.getDescription());
		assertEquals(taskDto.getDueDate(), createdTask.getDueDate());
		assertEquals(taskDto.getPriority(), createdTask.getPriority());
		assertEquals(taskDto.getStatus(), createdTask.getStatus());
	}
	
	@Test
	void testGetUserByEmail() {
		when(userRepository.findByEmail("email1")).thenReturn(Optional.of(loggedInUser));
		Optional<User> retrievedUser = userServiceImplementation.getUserByEmail("email1");
		assertTrue(retrievedUser.isPresent());
		assertEquals("email1", retrievedUser.get().getEmail());
	}
	
	@Test
	void testGetTasksByUserId() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(loggedInUser));
		List<TaskDto> retrievedTasks = userServiceImplementation.getTasksByUserId(1L);
		assertNotNull(retrievedTasks);
		assertEquals(loggedInUser.getTasks().size(), retrievedTasks.size());
	}
	
	@Test
	void testGetTasksByUserIdSortedByDueDate() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(loggedInUser));
		List<TaskDto> retrievedTasks = userServiceImplementation.getTasksByUserIdSortedByDueDate(1L);
		assertNotNull(retrievedTasks);
		assertEquals(loggedInUser.getTasks().size(), retrievedTasks.size());
	}
	
	@Test
	void testGetTasksByUserIdSortedByPriority() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(loggedInUser));
		List<TaskDto> retrievedTasks = userServiceImplementation.getTasksByUserIdSortedByPriority(1L);
		assertNotNull(retrievedTasks);
		assertEquals(loggedInUser.getTasks().size(), retrievedTasks.size());
	}
	
	@Test
	void testDeleteTask() {
		String result = userServiceImplementation.deleteTask(loggedInUser, 1L);
		assertEquals("Task deleted successfully", result);
		assertEquals(tasks.size(), loggedInUser.getTasks().size());
	}
	
	@Test
	void testGetTaskById() {
		TaskDto retrievedTask = userServiceImplementation.getTaskById(loggedInUser, 1L);
		assertNotNull(retrievedTask);
		assertEquals(taskDto.getTitle(), retrievedTask.getTitle());
		assertEquals(taskDto.getDescription(), retrievedTask.getDescription());
		assertEquals(taskDto.getDueDate(), retrievedTask.getDueDate());
		assertEquals(taskDto.getPriority(), retrievedTask.getPriority());
		assertEquals(taskDto.getStatus(), retrievedTask.getStatus());
	}
	
	@Test
	void testUpdateTask() {
		TaskDto updatedTask = userServiceImplementation.updateTask(loggedInUser, taskDto);
		assertNotNull(updatedTask);
		assertEquals(taskDto.getTitle(), updatedTask.getTitle());
		assertEquals(taskDto.getDescription(), updatedTask.getDescription());
		assertEquals(taskDto.getDueDate(), updatedTask.getDueDate());
		assertEquals(taskDto.getPriority(), updatedTask.getPriority());
		assertEquals(taskDto.getStatus(), updatedTask.getStatus());
	}
	
	@Test
	void testSearchTasks() {
		List<TaskDto> retrievedTasks = userServiceImplementation.searchTasks(loggedInUser, "title1");
		assertNotNull(retrievedTasks);
		assertEquals(loggedInUser.getTasks().size(), retrievedTasks.size());
		for (TaskDto task : retrievedTasks) {
			assertTrue(task.getTitle().contains("title1"));
		}
	}
	
	@Test
	void testFilterTaskByPriority() {
		List<TaskDto> retrievedTasks = userServiceImplementation.filterTaskByPriority(loggedInUser, "HIgh");
		assertNotNull(retrievedTasks);
		assertEquals(loggedInUser.getTasks().size(), retrievedTasks.size());
		for (TaskDto task : retrievedTasks) {
			assertEquals(TaskPriority.HIGH, task.getPriority());
		}
	}
	
	@Test
	void testFilterTaskByStatus() {
		List<TaskDto> retrievedTasks = userServiceImplementation.filterTaskByStatus(loggedInUser, "PENding");
		assertNotNull(retrievedTasks);
		assertEquals(loggedInUser.getTasks().size(), retrievedTasks.size());
		for (TaskDto task : retrievedTasks) {
			assertEquals(TaskStatus.PENDING, task.getStatus());
		}
	}
	
	@Test
	void testGetDashboardForUser() {
		DashboardDto dashboardDto = userServiceImplementation.getDashboardForUser(loggedInUser);
		assertNotNull(dashboardDto);
		assertEquals(loggedInUser.getTasks().size(), dashboardDto.getTotalTasks());
		assertEquals(1, dashboardDto.getPendingTasks());
		assertEquals(0, dashboardDto.getInProgressTasks());
		assertEquals(0, dashboardDto.getCompletedTasks());
		assertEquals(0, dashboardDto.getCancelledTasks());
		assertEquals(1, dashboardDto.getOverdueTasks());
		assertEquals(1, dashboardDto.getHighPriorityTasks());
		assertEquals(0, dashboardDto.getMediumPriorityTasks());
		assertEquals(0, dashboardDto.getLowPriorityTasks());
		assertEquals(0, dashboardDto.getUrgentTasks());
	}

}
