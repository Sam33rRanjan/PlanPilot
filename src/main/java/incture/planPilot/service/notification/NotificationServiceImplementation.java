package incture.planPilot.service.notification;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.NotificationRepository;
import incture.planPilot.dao.TaskRepository;
import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.NotificationDto;
import incture.planPilot.entity.Notification;
import incture.planPilot.entity.User;
import incture.planPilot.enums.NotificationStatus;

@Service
public class NotificationServiceImplementation implements NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImplementation.class);
	
	@Override
	public List<NotificationDto> getNotificationsByUserId(long userId) {
		logger.info("Fetching notifications for user with ID: {}", userId);
		return notificationRepository.findByUserId(userId).stream()
				.map(Notification::getNotificationDto)
				.toList();
	}

	@Override
	public NotificationDto sendNotification(NotificationDto notificationDto) {
		logger.info("Sending notification to user with ID: " + notificationDto.getUserId());
		Notification notification = new Notification();
		notification.setDescription(notificationDto.getDescription());
		notification.setUser(userRepository.findById(notificationDto.getUserId()).orElse(null));
		notification.setTask(taskRepository.findById(notificationDto.getTaskId()).orElse(null));
		Notification savedNotification = notificationRepository.save(notification);
		return savedNotification.getNotificationDto();
	}
	
	@Override
	public String markNotificationAsRead(User loggedInUser, long notificationId) {
		logger.info("Marking notification with ID: " + notificationId + " as read for user with ID: " + loggedInUser.getId());
		Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
		Notification notification = notificationOptional.get();
		if(notification.getUser().getId() == loggedInUser.getId()) {
			notification.setStatus(NotificationStatus.READ);
			notificationRepository.save(notification);
			return "Notification marked as read";
		} else {
			return "You are not authorized to mark this notification as read";
		}
	}

}
