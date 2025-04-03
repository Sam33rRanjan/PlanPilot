package incture.planPilot.dto;

import incture.planPilot.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
	private long id;
	private String username;
	private String email;
	private String password;
	private UserRole userRole;
	
}
