package incture.planPilot.dto;

import incture.planPilot.enums.UserRole;
import lombok.Data;

@Data
public class LoginResponse {
	
	private String jwt;
	private long userId;
	private UserRole userRole;
	
}
