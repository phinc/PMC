package by.phinc.pmc.model.beans.proxy;

import java.util.Date;
import java.util.SortedSet;

import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Status;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.IAssignmentService;

public class AssignmentProxy extends Assignment implements IProxy<IAssignmentService> {
	
	private Assignment assignment;
	
	private IAssignmentService service;

	
	public AssignmentProxy(Integer id) {
		super();
		setId(id);
	}
	
	

	@Override
	public void setService(IAssignmentService service) {
		this.service = service;
	}
	

	private Assignment getAssignment() {
		if (assignment == null) {
			assignment = service.findById(getId());
		}
		return assignment;
	}

	@Override
	public TeamMember getMember() {
		return getAssignment().getMember();
	}

	@Override
	public void setMember(TeamMember member) {
		getAssignment().setMember(member);
	}

	@Override
	public Task getTask() {
		return getAssignment().getTask();
	}

	@Override
	public void setTask(Task task) {
		getAssignment().setTask(task);
	}

	@Override
	public SortedSet<Activity> getActivities() {
		return getAssignment().getActivities();
	}

	@Override
	public void setActivities(SortedSet<Activity> activities) {
		getAssignment().setActivities(activities);
	}

	@Override
	public void addActivity(Activity activity) {
		getAssignment().addActivity(activity);
	}

	@Override
	public String getName() {
		return getAssignment().getName();
	}

	@Override
	public void setName(String name) {
		getAssignment().setName(name);
	}

	@Override
	public String getDescription() {
		return getAssignment().getDescription();
	}

	@Override
	public void setDescription(String description) {
		getAssignment().setDescription(description);
	}

	@Override
	public Date getPlanStart() {
		return getAssignment().getPlanStart();
	}

	@Override
	public void setPlanStart(Date planStart) {
		getAssignment().setPlanStart(planStart);
	}

	@Override
	public Float getPlanDuration() {
		return getAssignment().getPlanDuration();
	}

	@Override
	public void setPlanDuration(Float planDuration) {
		getAssignment().setPlanDuration(planDuration);
	}

	@Override
	public Date getActStart() {
		return getAssignment().getActStart();
	}

	@Override
	public void setActStart(Date actStart) {
		getAssignment().setActStart(actStart);
	}

	@Override
	public Float getActDuration() {
		return getAssignment().getActDuration();
	}

	@Override
	public void setActDuration(Float actDuration) {
		getAssignment().setActDuration(actDuration);
	}

	@Override
	public Status getStatus() {
		return getAssignment().getStatus();
	}

	@Override
	public void setStatus(Status status) {
		getAssignment().setStatus(status);
	}
	
}
