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
	
	public long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
}
