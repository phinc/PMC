package by.phinc.pmc.model.beans.proxy;

import java.util.Date;
import java.util.SortedSet;

import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Status;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.service.ITaskService;

public class TaskProxy extends Task implements IProxy<ITaskService> {
	
	private Task task;
	
	private ITaskService service;

	
	public TaskProxy(Integer id) {
		super();
		setId(id);
	}
	

	@Override
	public void setService(ITaskService service) {
		this.service = service;
	}


	private Task getTask() {
		if (task == null) {
			task = service.findById(getId());
		}
		return task;
	}

	@Override
	public String getName() {
		return getTask().getName();
	}

	@Override
	public void setName(String name) {
		getTask().setName(name);
	}

	@Override
	public String getDescription() {
		return getTask().getDescription();
	}

	@Override
	public void setDescription(String description) {
		getTask().setDescription(description);
	}

	@Override
	public Date getPlanStart() {
		return getTask().getPlanStart();
	}

	@Override
	public void setPlanStart(Date planStart) {
		getTask().setPlanStart(planStart);
	}

	@Override
	public Float getPlanDuration() {
		return getTask().getPlanDuration();
	}

	@Override
	public void setPlanDuration(Float planDuration) {
		getTask().setPlanDuration(planDuration);
	}

	@Override
	public Date getActStart() {
		return getTask().getActStart();
	}

	@Override
	public void setActStart(Date actStart) {
		getTask().setActStart(actStart);
	}

	@Override
	public Float getActDuration() {
		return getTask().getActDuration();
	}

	@Override
	public void setActDuration(Float actDuration) {
		getTask().setActDuration(actDuration);
	}

	@Override
	public Status getStatus() {
		return getTask().getStatus();
	}

	@Override
	public void setStatus(Status status) {
		getTask().setStatus(status);
	}

	@Override
	public Project getProject() {
		return getTask().getProject();
	}

	@Override
	public void setProject(Project project) {
		getTask().setProject(project);
	}

	@Override
	public SortedSet<Assignment> getAssignments() {
		return getTask().getAssignments();
	}

	@Override
	public void setAssignments(SortedSet<Assignment> assignments) {
		getTask().setAssignments(assignments);
	}

	@Override
	public void addAssignment(Assignment assignment) {
		getTask().addAssignment(assignment);
	}
	
	@Override
	public SortedSet<Document> getDocuments() {
		return getTask().getDocuments();
	}

	@Override
	public void setDocuments(SortedSet<Document> documents) {
		getTask().setDocuments(documents);
	}

}
