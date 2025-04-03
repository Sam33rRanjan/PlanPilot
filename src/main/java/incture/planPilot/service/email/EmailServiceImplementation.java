package incture.planPilot.service.email;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImplementation implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public String sendEmail(String to, String subject) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("sameerranjan2003@gmail.com");
			helper.setTo(to);
			helper.setSubject(subject);
			try(var inputStream = EmailServiceImplementation.class.getResourceAsStream("/templates/email.html")) {
				var html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
				helper.setText(html, true);
			}
			helper.addInline("logo.png", new File("/home/sameer-ranjan/Desktop/Java-Projects/PlanPilot/src/main/resources/templates/logo.png"));
			mailSender.send(message);
			return "Email sent successfully";
		} 
		catch (Exception e) {
			return "Error sending email: " + e.getMessage();
		} 
	}

}
