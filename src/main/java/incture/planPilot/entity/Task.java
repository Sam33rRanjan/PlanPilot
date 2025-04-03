package incture.planPilot.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import incture.planPilot.dto.TaskDto;
import incture.planPilot.enums.TaskPriority;
import incture.planPilot.enums.TaskStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
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
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications;
	
	private boolean reminderSent = false;
	
	
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
		taskDto.setReminderSent(this.reminderSent);
		return taskDto;
	}
	
}
