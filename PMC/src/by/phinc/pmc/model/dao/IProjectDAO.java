package by.phinc.pmc.model.dao;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;

public interface IProjectDAO extends GenericDAO<Project, Integer> {
	
	/*
	 * Fetch the whole collection of projects.
	 * Perform the pagination of the result
	 */
	void findWithPagination(Pagination<Project, Project> pagination);
	
	/*
	 * Fetch the collection of projects by the employee id.
	 * Perform the pagination of the result
	 */
	void findUserProjectWithPagination(final Pagination<Project, Employee> pagination);
	
}
