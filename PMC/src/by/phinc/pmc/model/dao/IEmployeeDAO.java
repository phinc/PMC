package by.phinc.pmc.model.dao;


import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;

public interface IEmployeeDAO extends GenericDAO<Employee, Integer> {
	
	/* 
	 * Fetch  the whole collection of employees.
	 * Perform the pagination of the result.
	 */
	void findWithPagination(final Pagination<Employee, Employee> pagination);
	
}
