package incture.planPilot.service.auth;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.SignupRequest;
import incture.planPilot.dto.UserDto;
import incture.planPilot.entity.User;
import incture.planPilot.enums.UserRole;
import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImplementation implements AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImplementation.class);
	
	@PostConstruct
	public void createAdminAccount() {
		List<User> adminUsers = userRepository.findByUserRole(UserRole.ADMIN);
		if(adminUsers.isEmpty()) {
			User user = new User();
			user.setUsername("Head Admin");
			user.setEmail("admin@gmail.com");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setUserRole(UserRole.ADMIN);
			userRepository.save(user);
			logger.info("Admin account created successfully with email: admin@gmail.com and password: admin");
		}
		else {
			logger.info("Admin account already exists");
		}
	}

	@Override
	public UserDto signupUser(SignupRequest signupRequest) {
		logger.info("Creating user with email: " + signupRequest.getEmail());
		User user = new User();
		user.setEmail(signupRequest.getEmail());
		user.setUsername(signupRequest.getUsername());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		User createdUser = userRepository.save(user);
		return createdUser.getUserDto();
	}

	@Override
	public boolean hasUserWithEmail(String email) {
		logger.info("Checking if user exists with email: " + email);
		return userRepository.findByEmail(email).isPresent();
	}

	@Override
	public boolean hasUserWithUsername(String username) {
		logger.info("Checking if user exists with username: " + username);
		return userRepository.findByUsername(username).isPresent();
	}
	
}
