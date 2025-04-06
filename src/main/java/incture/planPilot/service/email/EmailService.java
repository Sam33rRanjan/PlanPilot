package incture.planPilot.service.email;

public interface EmailService {
	
	String sendReminder(String to, String subject);
	
	String sendOverdueReminder(String to, String subject);
	
	String sendAssignment(String to, String subject);
	
}
