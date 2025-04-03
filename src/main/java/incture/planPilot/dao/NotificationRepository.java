package incture.planPilot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import incture.planPilot.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	List<Notification> findByUserId(long userId);
	
}
