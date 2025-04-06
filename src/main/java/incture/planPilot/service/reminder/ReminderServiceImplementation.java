package incture.planPilot.service.reminder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.TaskRepository;
import incture.planPilot.dto.NotificationDto;
import incture.planPilot.entity.Task;
import incture.planPilot.enums.TaskStatus;
import incture.planPilot.service.email.EmailService;
import incture.planPilot.service.notification.NotificationService;

@Service
public class ReminderServiceImplementation implements ReminderService {
	
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private NotificationService notificationService;
	
	private static final Logger logger = LoggerFactory.getLogger(ReminderServiceImplementation.class);
	
	@Override
	@Scheduled(fixedRate = 60000)
	public void sendReminders() {
		Date currentDate = new Date();
		LocalDateTime reminderTimeLocalDateTime = LocalDateTime.now().plusHours(24);
		Date reminderTime = Date.from(reminderTimeLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		logger.info("Retrieving tasks due within the next 24 hours");
		List<Task> tasks = taskRepository.findByDueDateBetween(currentDate, reminderTime).stream()
				.filter(task -> task.getStatus().equals(TaskStatus.PENDING) || task.getStatus().equals(TaskStatus.IN_PROGRESS))
				.filter(task -> task.isReminderSent() == false)
				.toList();
		
		for (Task task : tasks) {
			String responseString = emailService.sendReminder(task.getUser().getEmail(), "Upcoming Task Reminder: " + task.getTitle());
			if(responseString.equals("Reminder email sent successfully")) {
				logger.info("Email reminder sent successfully to : " + task.getUser().getEmail());
				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setDescription("You have a task due soon: " + task.getTitle());
				notificationDto.setUserId(task.getUser().getId());
				notificationDto.setTaskId(task.getId());
				notificationService.sendNotification(notificationDto);
				task.setReminderSent(true);
				taskRepository.save(task);
				logger.info("Notification reminder sent successfully to: " + task.getUser().getUsername());
			} else {
				logger.error("Failed to send email reminder to: " + task.getUser().getEmail() + ". CAUSE: " + responseString);
			}
		}
	}
	
	@Override
	@Scheduled(fixedRate = 86400000)
	public void sendOverdueReminders() {
		Date currentDate = new Date();
		
		logger.info("Retrieving overdue tasks");
		List<Task> tasks = taskRepository.findByDueDateBefore(currentDate).stream()
				.filter(task -> task.getStatus().equals(TaskStatus.PENDING) || task.getStatus().equals(TaskStatus.IN_PROGRESS))
				.toList();
		
		for (Task task : tasks) {
			String responseString = emailService.sendOverdueReminder(task.getUser().getEmail(), "Overdue Task Reminder: " + task.getTitle());
			if(responseString.equals("Reminder email sent successfully")) {
				logger.info("Email overdue reminder sent successfully to : " + task.getUser().getEmail());
				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setDescription("You have an overdue task: " + task.getTitle());
				notificationDto.setUserId(task.getUser().getId());
				notificationDto.setTaskId(task.getId());
				notificationService.sendNotification(notificationDto);
				logger.info("Notification overdue reminder sent successfully to: " + task.getUser().getUsername());
			} else {
				logger.error("Failed to send overdue email reminder to: " + task.getUser().getEmail() + ". CAUSE: " + responseString);
			}
		}
	}

}
