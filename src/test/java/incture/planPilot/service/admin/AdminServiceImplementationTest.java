package incture.planPilot.service.admin;

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

import incture.planPilot.dao.TaskRepository;
import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.DashboardDto;
import incture.planPilot.dto.TaskDto;
import incture.planPilot.dto.UserDto;
import incture.planPilot.entity.Task;
import incture.planPilot.entity.User;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;
import incture.planPilot.service.email.EmailService;
import incture.planPilot.service.notification.NotificationService;

@SpringBootTest
class AdminServiceImplementationTest {

	@InjectMocks
	private AdminServiceImplementation adminServiceImplementation;
	
	@Mock
	private UserRepository userRepository;
	@Mock
	private TaskRepository taskRepository;
	@Mock
	private NotificationService notificationService;
	@Mock
	private EmailService emailService;
	
	private static List<User> userList = new ArrayList<User>();
	private static User user1 = new User();
	private static User user2 = new User();
	
	private static List<Task> taskList = new ArrayList<Task>();
	private static Task task1 = new Task();
	private static Task task2 = new Task();
	
	private static TaskDto taskDto1 = new TaskDto();
	
	@BeforeAll
	static void setUp() {
		user1.setId(1);
		user1.setUsername("user1");
		user1.setEmail("email1");
		user2.setId(2);
		user2.setUsername("user2");
		user2.setEmail("email2");
		userList.add(user1);
		userList.add(user2);
		
		task1.setId(1);
		task1.setTitle("task1");
		task1.setDescription("task1 description");
		task1.setDueDate(new Date());
		task1.setPriority(TaskPriority.LOW);
		task1.setStatus(TaskStatus.PENDING);
		task1.setUser(user1);
		task2.setId(2);
		task2.setTitle("task2");
		task2.setDescription("task2 description");
		task2.setDueDate(new Date());
		task2.setPriority(TaskPriority.HIGH);
		task2.setStatus(TaskStatus.COMPLETED);
		task2.setUser(user2);
		taskList.add(task1);
		taskList.add(task2);
		
		user1.setTasks(taskList);
		
		taskDto1.setId(1);
		taskDto1.setTitle("task1");
		taskDto1.setDescription("task1 description");
		taskDto1.setPriority(TaskPriority.LOW);
		taskDto1.setStatus(TaskStatus.PENDING);
		taskDto1.setDueDate(new Date());
		taskDto1.setUserId(1);
	}
	
	@Test
	void testGetAllUsers() {
		when(userRepository.findAll()).thenReturn(userList);
		List<UserDto> result = adminServiceImplementation.getAllUsers();
		assertEquals(2, result.size());
		assertEquals("user1", result.get(0).getUsername());
		assertEquals("user2", result.get(1).getUsername());
	}
	
	@Test
	void testAssignTask() {
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(user1));
		when(taskRepository.findById((long) 1)).thenReturn(Optional.of(task1));
		when(taskRepository.save(task1)).thenReturn(task1);
		
		TaskDto result = adminServiceImplementation.assignTask(taskDto1);
		assertEquals("task1", result.getTitle());
		assertEquals("task1 description", result.getDescription());
		assertEquals(1, result.getUserId());
	}
	
	@Test
	void testGetAllTasks() {
		when(taskRepository.findAll()).thenReturn(taskList);
		List<TaskDto> result = adminServiceImplementation.getAllTasks();
		assertEquals(2, result.size());
		assertEquals("task1", result.get(0).getTitle());
		assertEquals("task2", result.get(1).getTitle());
	}
	
	@Test
	void testGetAllTasksSortedByDueDate() {
		when(taskRepository.findAll()).thenReturn(taskList);
		List<TaskDto> result = adminServiceImplementation.getAllTasksSortedByDueDate();
		assertEquals(taskList.size(), result.size());
	}
	
	@Test
	void testGetAllTasksSortedByPriority() {
		when(taskRepository.findAll()).thenReturn(taskList);
		List<TaskDto> result = adminServiceImplementation.getAllTasksSortedByPriority();
		assertEquals(taskList.size(), result.size());
	}
	
	@Test
	void testGetTasksByUserId() {
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(user1));
		List<TaskDto> result = adminServiceImplementation.getTasksByUserId(1);
		assertEquals("task2", result.get(0).getTitle());
		assertEquals("task1", result.get(1).getTitle());
	}
	
	@Test
	void testDeleteTask() {
		when(taskRepository.findById((long) 1)).thenReturn(Optional.of(task1));
		when(taskRepository.save(task1)).thenReturn(task1);
		String response = adminServiceImplementation.deleteTask(1);
		assertEquals(response, "Task deleted successfully");
	}
	
	@Test
	void testGetTaskById() {
		when(taskRepository.findById((long) 1)).thenReturn(Optional.of(task1));
		TaskDto result = adminServiceImplementation.getTaskById(1);
		assertEquals("task1", result.getTitle());
		assertEquals("task1 description", result.getDescription());
		assertEquals(1, result.getUserId());
	}
	
	@Test
	void testUpdateTask() {
		when(taskRepository.findById((long) 1)).thenReturn(Optional.of(task1));
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(user1));
		when(taskRepository.save(task1)).thenReturn(task1);
		TaskDto result = adminServiceImplementation.updateTask(taskDto1);
		assertEquals("task1", result.getTitle());
		assertEquals("task1 description", result.getDescription());
		assertEquals(1, result.getUserId());
	}
	
	@Test
	void testSearchTask() {
		when(taskRepository.findAll()).thenReturn(taskList);
		List<TaskDto> result = adminServiceImplementation.searchTask("task");
		assertEquals(2, result.size());
	}
	
	@Test
	void testFilterTaskByPriority() {
		when(taskRepository.findAll()).thenReturn(taskList);
		List<TaskDto> result = adminServiceImplementation.filterTaskByPriority("LOW");
		assertEquals(1, result.size());
		assertEquals("task1", result.get(0).getTitle());
	}
	
	@Test
	void testFilterTaskByStatus() {
		when(taskRepository.findAll()).thenReturn(taskList);
		List<TaskDto> result = adminServiceImplementation.filterTaskByStatus("PENDING");
		assertEquals(2, result.size());
	}
	
	@Test
	void testGetDashboardData() {
		when(taskRepository.findAll()).thenReturn(taskList);
		DashboardDto result = adminServiceImplementation.getDashboardData();
		assertEquals(taskList.size(), result.getTotalTasks());
		assertEquals(2, result.getPendingTasks());
		assertEquals(1, result.getHighPriorityTasks());
	}
	
	@Test
	void testGetUserDashboardData() {
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(user1));
		DashboardDto result = adminServiceImplementation.getUserDashboardData(1);
		assertEquals(user1.getTasks().size(), result.getTotalTasks());
		assertEquals(2, result.getPendingTasks());
		assertEquals(1, result.getHighPriorityTasks());
	}

}
