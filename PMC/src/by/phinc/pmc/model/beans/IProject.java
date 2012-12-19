package by.phinc.pmc.model.beans;

import java.util.Date;
import java.util.SortedSet;

public interface IProject extends IModel<Integer> {
	
	String getName();
	
	void setName(String name);
	
	public String getDescription();
	
	void setDescription(String descrption);
	
	public Date getPlanStart();
	
	public void setPlanStart(Date planStart);
	
	public Float getPlanDuration();
	
	public void setPlanDuration(Float planDuration);
	
	public Date getActStart();
	
	public void setActStart(Date actStart);
	
	public Float getActDuration();
	
	public void setActDuration(Float actDuration);
	
	public Status getStatus();
	
	public void setStatus(Status status);
	
	public String getCustomer();

	public void setCustomer(String customer);

	public SortedSet<TeamMember> getTeam() ;

	public void setTeam(SortedSet<TeamMember> team) ;
	
	public SortedSet<Task> getTasks() ;

	public void setTasks(SortedSet<Task> tasks) ;

	public int getVersion() ;

	public void setVersion(int version) ;
}
