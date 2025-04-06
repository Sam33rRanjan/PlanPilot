package incture.planPilot.controller.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import incture.planPilot.dto.LoginRequest;
import incture.planPilot.dto.SignupRequest;
import incture.planPilot.service.auth.AuthService;

@SpringBootTest
class AuthControllerTest {

	@InjectMocks
	private AuthController authController;
	
	@Mock
	private AuthService authService;
	
	private static SignupRequest signupRequest1 = new SignupRequest();
	private static SignupRequest signupRequest2 = new SignupRequest();
	private static SignupRequest signupRequest3 = new SignupRequest();
	
	private static LoginRequest loginRequest1 = new LoginRequest();

	
	@Test
	void testSignupUser() {
		when(authService.hasUserWithEmail(signupRequest1.getEmail())).thenReturn(true);
		assertEquals("User with email already exists", authController.signupUser(signupRequest1).getBody());
		when(authService.hasUserWithEmail(signupRequest2.getEmail())).thenReturn(false);
		when(authService.hasUserWithUsername(signupRequest2.getUsername())).thenReturn(true);
		assertEquals("User with username already exists", authController.signupUser(signupRequest2).getBody());
		when(authService.hasUserWithEmail(signupRequest3.getEmail())).thenReturn(false);
		when(authService.hasUserWithUsername(signupRequest3.getUsername())).thenReturn(false);
		when(authService.signupUser(signupRequest3)).thenReturn(null);
		assertNull(authController.signupUser(signupRequest3).getBody());
	}
	
	@Test
	void testLoginUser() {
		assertThrows(NullPointerException.class , () -> authController.login(loginRequest1).getBody());
	}

}
