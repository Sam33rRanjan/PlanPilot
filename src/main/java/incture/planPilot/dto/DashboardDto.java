package incture.planPilot.dto;

import lombok.Data;

@Data
public class DashboardDto {
	
	private long urgentTasks;
	private long highPriorityTasks;
	private long mediumPriorityTasks;
	private long lowPriorityTasks;
	private long completedTasks;
	private long inProgressTasks;
	private long pendingTasks;
	private long cancelledTasks;
	private long overdueTasks;
	private long totalTasks;
	
}
