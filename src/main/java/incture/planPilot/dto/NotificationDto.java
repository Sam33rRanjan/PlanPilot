package incture.planPilot.dto;

import incture.planPilot.enums.NotificationStatus;
import lombok.Data;

@Data
public class NotificationDto {
	private long id;
	private String description;
	private long userId;
	private long taskId;
	private NotificationStatus status;
}
