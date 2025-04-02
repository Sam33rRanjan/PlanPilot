package incture.planPilot.service.auth;

import incture.planPilot.dto.SignupRequest;
import incture.planPilot.dto.UserDto;

public interface AuthService {
	
	UserDto signupUser(SignupRequest signupRequest);
	
	boolean hasUserWithEmail(String email);
	
	boolean hasUserWithUsername(String username);
	
}
