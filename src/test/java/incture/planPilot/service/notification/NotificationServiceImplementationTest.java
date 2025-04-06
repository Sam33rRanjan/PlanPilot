package incture.planPilot.service.notification;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import incture.planPilot.dao.NotificationRepository;
import incture.planPilot.dao.TaskRepository;
import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.NotificationDto;
import incture.planPilot.entity.Notification;
import incture.planPilot.entity.Task;
import incture.planPilot.entity.User;
import incture.planPilot.enums.NotificationStatus;

@SpringBootTest
class NotificationServiceImplementationTest {

	@InjectMocks
	private NotificationServiceImplementation notificationServiceImplementation;
	
	@Mock
	private NotificationRepository notificationRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private TaskRepository taskRepository;
	
	private static List<Notification> notificationList = new ArrayList<Notification>();
	
	private static Notification notification1 = new Notification();
	private static Notification notification2 = new Notification();
	
	private static User user1 = new User();
	private static Task task1 = new Task();
	
	@BeforeAll
	static void setUp() {
		user1.setId(1);
		user1.setUsername("User 1");
		task1.setId(1);
		task1.setTitle("Task 1");
		
		
		notification1.setId(1);
		notification1.setDescription("Notification 1");
		notification1.setUser(user1);
		notification1.setTask(task1);
		notification2.setId(2);
		notification2.setDescription("Notification 2");
		notification2.setUser(user1);
		notification2.setTask(task1);
		
		notificationList.add(notification1);
		notificationList.add(notification2);
		
	}
	
	@Test
	void testGetNotificationsByUserId() {
		when(notificationRepository.findByUserId((long)1)).thenReturn(notificationList);
		List<NotificationDto> result = notificationServiceImplementation.getNotificationsByUserId(1);
		assertEquals(2, result.size());
		assertEquals("Notification 1", result.get(0).getDescription());
		assertEquals("Notification 2", result.get(1).getDescription());
	}
	
	@Test
	void testSendNotification() {
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setDescription("New Notification");
		notificationDto.setUserId(1);
		notificationDto.setTaskId(1);
		when(userRepository.findById((long)1)).thenReturn(Optional.of(user1));
		when(taskRepository.findById((long)1)).thenReturn(Optional.of(task1));
		when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);
		NotificationDto result = notificationServiceImplementation.sendNotification(notificationDto);
		assertEquals("Notification 1", result.getDescription());
		assertEquals(1, result.getUserId());
		assertEquals(1, result.getTaskId());
	}
	
	@Test
	void testMarkNotificationAsRead() {
		when(notificationRepository.findById((long)1)).thenReturn(Optional.of(notification1));
		String result = notificationServiceImplementation.markNotificationAsRead(user1, 1);
		assertEquals("Notification marked as read", result);
		assertEquals(NotificationStatus.READ, notification1.getStatus());
	}

}
