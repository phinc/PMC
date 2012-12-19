package by.phinc.pmc.model.beans;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;


public class Project implements Comparable<Project>, 
			DocumentOwner {
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Date planStart;
	
	private Float planDuration;
	
	private Date actStart;
	
	private Float actDuration;
	
	private Status status;
	
	private String customer;
	
	private SortedSet<Document> documents = new TreeSet<Document>();
	
	private SortedSet<TeamMember> team = new TreeSet<TeamMember>();
	
	private SortedSet<Task> tasks = new TreeSet<Task>();
	
	private int version;

	
	public Project() {
		super();
	}
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer pId) {
		id = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPlanStart() {
		return planStart;
	}

	public void setPlanStart(Date planStart) {
		this.planStart = planStart;
	}

	public Float getPlanDuration() {
		return planDuration;
	}

	public void setPlanDuration(Float planDuration) {
		this.planDuration = planDuration;
	}

	public Date getActStart() {
		return actStart;
	}

	public void setActStart(Date actStart) {
		this.actStart = actStart;
	}

	public Float getActDuration() {
		return actDuration;
	}

	public void setActDuration(Float actDuration) {
		this.actDuration = actDuration;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public SortedSet<Document> getDocuments() {
		return documents;
	}
	
	@Override
	public void setDocuments(SortedSet<Document> documents) {
		this.documents = documents;
	}
	
	@Override
	public void addDocument(Document document) {
		//document.setOwner(this);
		documents.add(document);
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}


	public SortedSet<TeamMember> getTeam() {
		return team;
	}

	public void setTeam(SortedSet<TeamMember> team) {
		this.team = team;
	}
	
	public SortedSet<Task> getTasks() {
		return tasks;
	}

	public void setTasks(SortedSet<Task> tasks) {
		this.tasks = tasks;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void addTeamMember(TeamMember member) {
		member.setProject(this);
		team.add(member);
	}
	
	public void addTask(Task task) {
		task.setProject(this);
		tasks.add(task);
	}

	@Override
	public String toString() {
		return "id=" + getId() + "; name=" + getName() + "; description=" +
				getDescription() + ";" + " customer=" + customer + ";";
	}

	@Override
	public int compareTo(Project o) {
		return id.compareTo(o.getId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Project)) {
			return false;
		}
		Project project = (Project)obj;
		return id.equals(project.getId());
	}
}
