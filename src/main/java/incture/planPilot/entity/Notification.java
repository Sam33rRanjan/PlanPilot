package incture.planPilot.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import incture.planPilot.dto.NotificationDto;
import incture.planPilot.enums.NotificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String description;
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	@ManyToOne(optional = false)
	@JoinColumn(name = "task_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Task task;
	private NotificationStatus status=NotificationStatus.UNREAD;
	
	@Transient
	private static final Logger logger = LoggerFactory.getLogger(Notification.class);
	
	public NotificationDto getNotificationDto() {
		logger.trace("Converting Notification with ID " + this.id + " to NotificationDto");
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setId(this.id);
		notificationDto.setDescription(this.description);
		notificationDto.setUserId(this.user.getId());
		notificationDto.setTaskId(this.task.getId());
		notificationDto.setStatus(this.status);
		return notificationDto;
	}
}
