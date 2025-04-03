package incture.planPilot.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import incture.planPilot.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	List<Task> findByDueDateBetween(Date startDate, Date endDate);
	
	List<Task> findByDueDateBefore(Date date);
	
}
