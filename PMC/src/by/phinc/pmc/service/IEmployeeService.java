package by.phinc.pmc.service;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;

public interface IEmployeeService extends IModelService<Employee, Integer> {
	
	/*
	 * Looks for the employee object by the given login.
	 * Returns employee object or null otherwise.
	 */
	Employee getEmployee(String login);
	
	/* 
	 * Fetch  the whole collection of employees.
	 * Perform the pagination of the result.
	 */
	void findWithPagination(final Pagination<Employee, Employee> pagination);
	
	/*
	 * Check if there is another employee with the given login already in database.
	 * Return true if the given login is already in use.
	 */
	boolean isEmployeeWithLogin(Employee employee);
}
