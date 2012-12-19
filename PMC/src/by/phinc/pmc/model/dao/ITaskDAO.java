package by.phinc.pmc.model.dao;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;

public interface ITaskDAO extends GenericDAO<Task, Integer>{
	
	void fillProjectWithTasks(Project project);
	
	/*
	 * Fetch the collection of task by the project id.
	 * Perform the pagination of the result.
	 */
	void findWithPagination(Pagination<Task, Project> pagination);
}
