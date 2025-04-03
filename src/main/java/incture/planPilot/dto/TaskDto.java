package incture.planPilot.dto;

import java.util.Date;

import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskDto {
	private long id;
	private String title;
	private String description;
	private Date dueDate;
	private TaskPriority priority;
	private TaskStatus status;
	
	private long userId;
	private String userName;
	
	private boolean reminderSent = false;
	
}
