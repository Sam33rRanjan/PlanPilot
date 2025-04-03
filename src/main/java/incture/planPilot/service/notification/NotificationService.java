package incture.planPilot.service.notification;

import java.util.List;

import incture.planPilot.dto.NotificationDto;
import incture.planPilot.entity.User;

public interface NotificationService {
	
	public NotificationDto sendNotification(NotificationDto notificationDto);
	
	public List<NotificationDto> getNotificationsByUserId(long userId);

	public String markNotificationAsRead(User loggedInUser, long notificationId);
	
}
