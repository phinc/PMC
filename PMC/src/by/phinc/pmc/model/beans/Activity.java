package by.phinc.pmc.model.beans;

import java.util.Date;

public class Activity implements IModel<Integer>, Comparable<Activity> {
	
	private Integer id;
	
	private Assignment assignment;
	
	private String description;
	
	private Date startDate;
	
	private Float duration; //hours
	
	private Employee reporter;	
	
	
	public Activity() {
		super();
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer pId) {
		this.id = pId;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	public Employee getReporter() {
		return reporter;
	}

	public void setReporter(Employee reporter) {
		this.reporter = reporter;
	}

	@Override
	public int compareTo(Activity o) {
		return id.compareTo(o.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Activity)) {
			return false;
		}
		Activity activity = (Activity)obj;
		return id.equals(activity.getId());
	}
	
	
}
