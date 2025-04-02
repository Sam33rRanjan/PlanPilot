package incture.planPilot.controller.email;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.internet.MimeMessage;

@RestController
public class EmailController {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@GetMapping("/sendSimpleEmail")
	public String sendSimpleEmail() {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("sameerranjan2003@gmail.com");
			message.setTo("sameerranjan2003@gmail.com");
			message.setSubject("Simple Test Email");
			message.setText("This is the body of a simple test email");
			mailSender.send(message);
			return "Simple email sent successfully";
		}
		catch(Exception e) {
			return "Error sending email: " + e.getMessage();
		}
	}
	
	@GetMapping("/sendEmailWithAttachment")
	public String sendEmailWithAttachment() {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("sameerranjan2003@gmail.com");
			helper.setTo("sameerranjan2003@gmail.com");
			helper.setSubject("Test Email with attachment");
			helper.setText("Please find the attached document");
			helper.addAttachment("scenery.jpg", new File("/home/sameer-ranjan/Downloads/landscape.jpg"));
			mailSender.send(message);
			return "Email with attachment sent successfully";
		}
		catch(Exception e) {
			return "Error sending email with attachment: " + e.getMessage();
		}
	}
	
	@GetMapping("/sendEmailWithHtml")
	public String sendEmailWithHtml() {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("sameerranjan2003@gmail.com");
			helper.setTo("sameerranjan2003@gmail.com");
			helper.setSubject("Test Email with HTML");
			try(var inputStream = EmailController.class.getResourceAsStream("/templates/email.html")) {
				var html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
				helper.setText(html, true);
			}
			helper.addInline("logo.png", new File("/home/sameer-ranjan/Desktop/Java-Projects/PlanPilot/src/main/resources/templates/logo.png"));
			mailSender.send(message);
			return "Email with attachment sent successfully";
		}
		catch(Exception e) {
			return "Error sending email with attachment: " + e.getMessage();
		}
	}
	
}
