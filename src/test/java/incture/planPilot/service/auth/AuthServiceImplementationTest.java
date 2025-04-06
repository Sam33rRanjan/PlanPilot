package incture.planPilot.service.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import incture.planPilot.dao.UserRepository;
import incture.planPilot.dto.SignupRequest;
import incture.planPilot.dto.UserDto;
import incture.planPilot.entity.User;
import incture.planPilot.enums.UserRole;

@SpringBootTest
class AuthServiceImplementationTest {

	@InjectMocks
	private AuthServiceImplementation authServiceImplementation;
	
	@Mock
	private UserRepository userRepository;
	
	private static SignupRequest signupRequest = new SignupRequest();
	
	private static User user = new User();
	
	@BeforeAll
	public static void setup() {
		signupRequest.setUsername("testUser");
		signupRequest.setEmail("testEmail");
		signupRequest.setPassword("testPassword");
		
		user.setUsername("testUser");
		user.setEmail("testEmail");
		user.setPassword(new BCryptPasswordEncoder().encode("testPassword"));
		user.setUserRole(UserRole.USER);
	}
	
	@Test
	void testSignupUser() {
		when(userRepository.save(any(User.class))).thenReturn(user);
		UserDto userDto = authServiceImplementation.signupUser(signupRequest);
		assertNotNull(userDto);
		assertEquals(user.getUsername(), userDto.getEmail());
		assertEquals(user.getEmail(), userDto.getEmail());
		assertEquals(user.getUserRole(), userDto.getUserRole());
		assertEquals(user.getPassword(), userDto.getPassword());
	}
	
	@Test
	void testHasUserWithEmail() {
		when(userRepository.findByEmail("testEmail")).thenReturn(Optional.of(user));
		assertTrue(authServiceImplementation.hasUserWithEmail("testEmail"));
	}
	
	@Test
	void testHasUserWithUsername() {
		when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
		assertTrue(authServiceImplementation.hasUserWithUsername("testUser"));
	}

}
