package by.phinc.pmc.model.beans.proxy;

import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.model.beans.TeamMember;

public interface ProxyFactory {
	
	Project getProjectProxy(Integer id);
	
	Task getTaskProxy(Integer id);
	
	Assignment getAssignmenmtProxy(Integer id);
	
	Employee getEmployeeProxy(Integer id);
	
	TeamMember getTeamMemberProxy(Integer id);
}
