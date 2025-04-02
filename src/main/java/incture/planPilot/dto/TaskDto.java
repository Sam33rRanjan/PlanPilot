package incture.planPilot.dto;

import java.util.Date;

import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;

public class TaskDto {
	private long id;
	private String title;
	private String description;
	private Date dueDate;
	private TaskPriority priority;
	private TaskStatus status;
	
	private long userId;
	private String userName;
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	
	public TaskPriority getPriority() {
		return priority;
	}
	
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	public TaskStatus getStatus() {
		return status;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
}
