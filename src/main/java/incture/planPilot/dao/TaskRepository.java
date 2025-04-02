package incture.planPilot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import incture.planPilot.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
