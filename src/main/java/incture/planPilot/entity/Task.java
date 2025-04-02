package incture.planPilot.entity;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import incture.planPilot.dto.TaskDto;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	private String description;
	private Date dueDate;
	@Enumerated(EnumType.ORDINAL)
	private TaskPriority priority;
	@Enumerated(EnumType.ORDINAL)
	private TaskStatus status;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
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
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public TaskDto getTaskDto() {
		TaskDto taskDto = new TaskDto();
		taskDto.setId(this.id);
		taskDto.setTitle(this.title);
		taskDto.setDescription(this.description);
		taskDto.setDueDate(this.dueDate);
		taskDto.setPriority(this.priority);
		taskDto.setStatus(this.status);
		taskDto.setUserId(this.user.getId());
		taskDto.setUserName(this.user.getUsername());
		return taskDto;
	}
	
}
