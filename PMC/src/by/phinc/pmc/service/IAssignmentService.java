package by.phinc.pmc.service;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Task;

public interface IAssignmentService extends IModelService<Assignment, Integer> {
	
	
	/*
	 * Fetch the collection of the assignments by the given task id
	 * Perform the pagination of the result
	 */
	void findWithPagination(final Pagination<Assignment, Task> pagination);
	
	
	/*
	 * Fetch the collection of the assignments by the given employee id
	 * Perform the pagination of the result
	 */
	void findUserAssignmentWithPagination(final Pagination<Assignment, Employee> pagination);
}
