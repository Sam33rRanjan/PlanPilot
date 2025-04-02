package incture.planPilot.dto;

import incture.planPilot.enums.UserRole;

public class LoginResponse {
	
	private String jwt;
	private long userId;
	private UserRole userRole;
	
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
	public String getJwt() {
		return jwt;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}
	
}
