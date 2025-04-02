package incture.planPilot.service.auth;

import java.util.List;

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
	
	@PostConstruct
	public void createAdminAccount() {
		List<User> adminUsers = userRepository.findByUserRole(UserRole.ADMIN);
		if(adminUsers.isEmpty()) {
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setUsername("Head Admin");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setUserRole(UserRole.ADMIN);
			userRepository.save(user);
			System.out.println("Admin account created successfully");
		}
		else {
			System.out.println("Admin account already exists");
		}
	}

	@Override
	public UserDto signupUser(SignupRequest signupRequest) {
		User user = new User();
		user.setEmail(signupRequest.getEmail());
		user.setUsername(signupRequest.getUsername());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		User createdUser = userRepository.save(user);
		return createdUser.getUserDto();
	}

	@Override
	public boolean hasUserWithEmail(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	@Override
	public boolean hasUserWithUsername(String username) {
		return userRepository.findByUsername(username).isPresent();
	}
	
}
