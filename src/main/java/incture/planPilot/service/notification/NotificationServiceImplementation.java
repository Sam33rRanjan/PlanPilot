package incture.planPilot.service.notification;

import java.util.List;
import java.util.Optional;

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
	
	@Override
	public List<NotificationDto> getNotificationsByUserId(long userId) {
		return notificationRepository.findByUserId(userId).stream()
				.map(Notification::getNotificationDto)
				.toList();
	}

	@Override
	public NotificationDto sendNotification(NotificationDto notificationDto) {
		Notification notification = new Notification();
		notification.setDescription(notificationDto.getDescription());
		notification.setUser(userRepository.findById(notificationDto.getUserId()).orElse(null));
		notification.setTask(taskRepository.findById(notificationDto.getTaskId()).orElse(null));
		Notification savedNotification = notificationRepository.save(notification);
		return savedNotification.getNotificationDto();
	}
	
	@Override
	public String markNotificationAsRead(User loggedInUser, long notificationId) {
		Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
		if(notificationOptional.isPresent()) {
			Notification notification = notificationOptional.get();
			if(notification.getUser().getId() == loggedInUser.getId()) {
				notification.setStatus(NotificationStatus.READ);
				notificationRepository.save(notification);
				return "Notification marked as read";
			} else {
				return "You are not authorized to mark this notification as read";
			}
		} else {
			return null;
		}
	}

}
