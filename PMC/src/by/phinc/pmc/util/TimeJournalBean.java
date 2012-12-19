package by.phinc.pmc.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeJournalBean implements Comparable<TimeJournalBean>{
	
	private Integer assignmentId;
	
	private String assignmentName;
	
	private Date date;
	
	private float duration;
	
	private Map<Date, Float> workingWeek = new HashMap<Date, Float>();
	
	
	public Integer getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Integer assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public Map<Date, Float> getWorkingWeek() {
		return workingWeek;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}
	

	public TimeJournalBean(Integer assignmentId, String assignmentName,
			Date date, float duration) {
		super();
		this.assignmentId = assignmentId;
		this.assignmentName = assignmentName;
		this.date = date;
		this.duration = duration;
		workingWeek.put(date, duration);
	}

	@Override
	public int compareTo(TimeJournalBean bean) {
		int res = assignmentId - bean.getAssignmentId();
		if (res == 0) {	
			bean.workingWeek.put(date, duration);
		}
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TimeJournalBean)) {
			return false;
		}
		TimeJournalBean bean = (TimeJournalBean)obj;
		return assignmentId.equals(bean.getAssignmentId());
	}
	
	
}
