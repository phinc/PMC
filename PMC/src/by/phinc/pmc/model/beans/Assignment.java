package by.phinc.pmc.model.beans;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;


public class Assignment implements IModel<Integer>, Comparable<Assignment>  {
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Date planStart;
	
	private Float planDuration;
	
	private Date actStart;
	
	private Float actDuration;
	
	private Status status;
	
	private TeamMember member;
	
	private Task task;
	
	private SortedSet<Activity> activities = new TreeSet<Activity>();
	
	public Assignment() {}
	
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
	
	public TeamMember getMember() {
		return member;
	}

	public void setMember(TeamMember member) {
		this.member = member;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	public SortedSet<Activity> getActivities() {
		return activities;
	}

	public void setActivities(SortedSet<Activity> activities) {
		this.activities = activities;
	}

//	public String getEmployeeName() {
//		return member.getLastName() + " " + member.getFirstName();
//	}
	
	public void addActivity(Activity activity) {
		activity.setAssignment(this);
		activities.add(activity);
	}

	@Override
	public int compareTo(Assignment o) {
		return id.compareTo(o.getId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Assignment)) {
			return false;
		}
		Assignment assignment = (Assignment)obj;
		return id.equals(assignment.getId());
	}
	
	@Override
	public String toString() {
		return "id=" + getId() + "; name=" + getName() + "; description=" +
				getDescription() + ";";
	}
}
