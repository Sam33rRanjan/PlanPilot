package incture.planPilot.service.email;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImplementation implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")  
    private String emailFrom;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImplementation.class);
	
	@Override
	public String sendReminder(String to, String subject) {
		try {
			logger.info("Sending reminder email to: " + to);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(emailFrom);
			helper.setTo(to);
			helper.setSubject(subject);
			try(var inputStream = EmailServiceImplementation.class.getResourceAsStream("/templates/reminder.html")) {
				var html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
				helper.setText(html, true);
			}
			helper.addInline("logo.png", new File("/home/sameer-ranjan/Desktop/Java-Projects/PlanPilot/src/main/resources/templates/logo.png"));
			mailSender.send(message);
			return "Reminder email sent successfully";
		} 
		catch (Exception e) {
			logger.error("Error sending reminder email: " + e.getMessage());
			return e.getMessage();
		} 
	}
	
	
	@Override
	public String sendOverdueReminder(String to, String subject) {
		try {
			logger.info("Sending reminder email to: " + to);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(emailFrom);
			helper.setTo(to);
			helper.setSubject(subject);
			try(var inputStream = EmailServiceImplementation.class.getResourceAsStream("/templates/overdue.html")) {
				var html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
				helper.setText(html, true);
			}
			helper.addInline("logo.png", new File("/home/sameer-ranjan/Desktop/Java-Projects/PlanPilot/src/main/resources/templates/logo.png"));
			mailSender.send(message);
			return "Reminder email sent successfully";
		} 
		catch (Exception e) {
			logger.error("Error sending reminder email: " + e.getMessage());
			return e.getMessage();
		} 
	}

	@Override
	public String sendAssignment(String to, String subject) {
		try {
			logger.info("Sending assignment email to: " + to);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(emailFrom);
			helper.setTo(to);
			helper.setSubject(subject);
			try(var inputStream = EmailServiceImplementation.class.getResourceAsStream("/templates/assignment.html")) {
				var html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
				helper.setText(html, true);
			}
			helper.addInline("logo.png", new File("/home/sameer-ranjan/Desktop/Java-Projects/PlanPilot/src/main/resources/templates/logo.png"));
			mailSender.send(message);
			return "Assignment email sent successfully";
		} 
		catch (Exception e) {
			logger.error("Error sending assignment email: " + e.getMessage());
			return e.getMessage();
		} 

	}

}
