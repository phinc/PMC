package by.phinc.pmc.model.beans.proxy;

import java.util.Date;
import java.util.SortedSet;

import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Status;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.IProjectService;

public class ProjectProxy extends Project implements IProxy<IProjectService> {
	
	private Project project;
	
	private IProjectService service;

	public ProjectProxy(Integer id) {
		super();
		setId(id);
	}
	

	@Override
	public void setService(IProjectService service) {
		this.service = service;
	}
	

	private Project getProject() {
		if (project == null) {
			project = service.findById(getId());
		}
		return project;
	}

	@Override
	public String getCustomer() {
		return getProject().getCustomer();
	}

	@Override
	public void setCustomer(String customer) {
		getProject().setCustomer(customer);
	}

	@Override
	public SortedSet<TeamMember> getTeam() {
		return getProject().getTeam();
	}

	@Override
	public void setTeam(SortedSet<TeamMember> team) {
		getProject().setTeam(team);
	}

	@Override
	public SortedSet<Task> getTasks() {
		return getProject().getTasks();
	}

	@Override
	public void setTasks(SortedSet<Task> tasks) {
		getProject().setTasks(tasks);
	}

	@Override
	public void addTeamMember(TeamMember member) {
		getProject().addTeamMember(member);
	}

	@Override
	public void addTask(Task task) {
		getProject().addTask(task);
	}

	@Override
	public String getName() {
		return getProject().getName();
	}

	@Override
	public void setName(String name) {
		getProject().setName(name);
	}

	@Override
	public String getDescription() {
		return getProject().getDescription();
	}

	@Override
	public void setDescription(String description) {
		getProject().setDescription(description);
	}

	@Override
	public Date getPlanStart() {
		return getProject().getPlanStart();
	}

	@Override
	public void setPlanStart(Date planStart) {
		getProject().setPlanStart(planStart);
	}

	@Override
	public Float getPlanDuration() {
		return getProject().getPlanDuration();
	}

	@Override
	public void setPlanDuration(Float planDuration) {
		getProject().setPlanDuration(planDuration);
	}

	@Override
	public Date getActStart() {
		return getProject().getActStart();
	}

	@Override
	public void setActStart(Date actStart) {
		getProject().setActStart(actStart);
	}

	@Override
	public Float getActDuration() {
		return getProject().getActDuration();
	}

	@Override
	public void setActDuration(Float actDuration) {
		getProject().setActDuration(actDuration);
	}

	@Override
	public Status getStatus() {
		return getProject().getStatus();
	}

	@Override
	public void setStatus(Status status) {
		getProject().setStatus(status);
	}

	@Override
	public SortedSet<Document> getDocuments() {
		return getProject().getDocuments();
	}

	@Override
	public void setDocuments(SortedSet<Document> documents) {
		getProject().setDocuments(documents);
	}
	
}
