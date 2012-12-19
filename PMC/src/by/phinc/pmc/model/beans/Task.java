package by.phinc.pmc.model.beans;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class Task implements IModel<Integer>, Comparable<Task>, DocumentOwner {
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Date planStart;
	
	private Float planDuration;
	
	private Date actStart;
	
	private Float actDuration;
	
	private Status status;
	
	private Project project;
	
	private SortedSet<Document> documents = new TreeSet<Document>();
	
	private SortedSet<Assignment> assignments = new TreeSet<Assignment>();
	

	public Task() {
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
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public SortedSet<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(SortedSet<Assignment> assignments) {
		this.assignments = assignments;
	}
	
	@Override
	public SortedSet<Document> getDocuments() {
		return documents;
	}
	
	@Override
	public void setDocuments(SortedSet<Document> documents) {
		this.documents = documents;
	}
	
	public void addAssignment(Assignment assignment) {
		assignment.setTask(this);
		getAssignments().add(assignment);
	}
	
	@Override
	public void addDocument(Document document) {
		//document.setOwner(this);
		documents.add(document);
	}

	@Override
	public String toString() {
		return "id=" + getId() + "; name=" + getName() + "; description=" +
				getDescription() + ";";
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Task)) {
			return false;
		}
		Task task = (Task)obj;
		return id.equals(task.getId());
	}


	@Override
	public int compareTo(Task o) {
		return id.compareTo(o.getId());
	}
	
}
