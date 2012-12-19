package by.phinc.pmc.model.dao;


import java.util.Collection;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;

public interface ITeamDAO extends GenericDAO<TeamMember, Integer> {
	
	/*
	 * Return collection of projects for the given employee
	 */
	Collection<Project> getProjectsByEmployee(Employee employee);
	
	/*
	 * Fill in the project with the collection of TeamMember objects. 
	 */
	void fillProjectWithTeam(Project project);
	
	/*
	 * Fetch the collection of TeamMembers by the given project id.
	 * Perform the pagination of the result.
	 */
	void findWithPagination(Pagination<TeamMember, Project> pagination);
	
	//Collection<Employee> getEmployeesOutOfTeam(Project project);
	
}
