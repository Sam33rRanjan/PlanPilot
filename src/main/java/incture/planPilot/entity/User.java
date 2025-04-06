package incture.planPilot.entity;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import incture.planPilot.dto.UserDto;
import incture.planPilot.enums.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true, nullable = false)
	private String username;
	@Column(unique = true, nullable = false)
	@Email
	private String email;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.ORDINAL)
	private UserRole userRole = UserRole.USER;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications;
	
	@Transient
	private static final Logger logger = LoggerFactory.getLogger(User.class);
	
	@Override
	public String getUsername() {
		logger.trace("Retrieving username for " + this.username);
		return email;
	}
	
	@Override
	public String getPassword() {
		logger.trace("Retrieving password for " + this.username);
		return password;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		logger.trace("Retrieving authorities for " + this.username);
		return List.of(new SimpleGrantedAuthority(userRole.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		logger.trace("Checking if account is non-expired for " + this.username);
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		logger.trace("Checking if account is non-locked for " + this.username);
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		logger.trace("Checking if credentials are non-expired for " + this.username);
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		logger.trace("Checking if account is enabled for " + this.username);
		return true;
	}
	
	public Task addTask(Task task) {
		logger.trace("Adding task: " + task.getTitle() + " to user " + this.username);
		tasks.add(task);
		return task;
	}
	
	public Task removeTask(long taskId) {
		logger.trace("Removing task with ID: " + taskId + " from user " + this.username);
		Task task = this.tasks.stream().filter(t -> t.getId() == taskId).findFirst().get();
		tasks.remove(task);
		return task;
	}
	
	public UserDto getUserDto() {
		logger.trace("Converting user " + this.username + " to UserDto");
		UserDto userDto = new UserDto();
		userDto.setId(this.id);
		userDto.setUsername(this.username);
		userDto.setEmail(this.email);
		userDto.setPassword(this.password);
		userDto.setUserRole(this.userRole);
		return userDto;
	}
	
}
