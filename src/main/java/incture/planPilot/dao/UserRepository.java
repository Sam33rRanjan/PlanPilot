package incture.planPilot.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import incture.planPilot.entity.User;
import incture.planPilot.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);

	List<User> findByUserRole(UserRole userRole);

	Optional<User> findByUsername(String username);

}
