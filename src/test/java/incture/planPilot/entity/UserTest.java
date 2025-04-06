package incture.planPilot.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import incture.planPilot.dto.UserDto;
import incture.planPilot.enums.TaskStatus;
import incture.planPilot.enums.UserRole;

@SpringBootTest
class UserTest {
	
	private static Task task = new Task();
	private static UserDto userDto = new UserDto();
	
	private static User user = new User();
	
	private static List<Task> tasks = new ArrayList<Task>();
	
	@BeforeAll
	public static void setup() {
		user.setId(1);
		user.setUsername("Test User");
		user.setEmail("User Email");
		user.setPassword("User Password");
		user.setTasks(tasks);
		task.setId(1);
		task.setTitle("Test Task");
		task.setDescription("Test Description");
		task.setStatus(TaskStatus.PENDING);
		userDto.setId(1);
		userDto.setUsername("Test User");
		userDto.setEmail("User Email");
		userDto.setPassword("User Password");
		userDto.setUserRole(UserRole.USER);
	}
	
	@Test
	void testGetUsername() {
		assertEquals("User Email", user.getUsername());
	}
	
	@Test
	void testGetPassword() {
		assertEquals("User Password", user.getPassword());
	}
	
	@Test
	void testGetAuthorities() {
		assertEquals(1, user.getAuthorities().size());
	}
	
	@Test
	void testIsAccountNonExpired() {
		assertTrue(user.isAccountNonExpired());
	}
	
	@Test
	void testIsAccountNonLocked() {
		assertTrue(user.isAccountNonLocked());
	}
	
	@Test
	void testIsCredentialsNonExpired() {
		assertTrue(user.isCredentialsNonExpired());
	}
	
	@Test
	void testIsEnabled() {
		assertTrue(user.isEnabled());
	}
	
	@Test
	void testAddTask() {
		assertEquals(task, user.addTask(task));
	}
	
	@Test
	void testRemoveTask() {
		user.addTask(task);
		assertEquals(task, user.removeTask(1));
	}
	
	@Test
	void testGetUserDto() {
		assertEquals(userDto, user.getUserDto());
	}

}
