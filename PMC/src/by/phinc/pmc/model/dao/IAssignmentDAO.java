package by.phinc.pmc.model.dao;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Task;

public interface IAssignmentDAO extends GenericDAO<Assignment, Integer> {
	
	
	/*
	 * fill in the collection of assignments of the given task
	 */
	void fillTaskWithAssignments(Task task);
	
	
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
