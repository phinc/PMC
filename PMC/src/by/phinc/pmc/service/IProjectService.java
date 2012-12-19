package by.phinc.pmc.service;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;

public interface IProjectService extends IModelService<Project, Integer>{
	
	/*
	 * Fetch the whole collection of the projects.
	 * Perform the pagination of the result
	 */
	void findWithPagination(Pagination<Project, Project> pagination);
	
	/*
	 * Fetch the collection of projects by the employee id.
	 * Perform the pagination of the result
	 */
	void findUserProjectWithPagination(final Pagination<Project, Employee> pagination);
	
	/*
	 * Save project and project manager
	 */
	void saveProjectWithProjectManager(Project project, TeamMember manager);

}
