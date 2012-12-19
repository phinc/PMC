/*
 * EmployeeAware
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * This interface allows actions to have the employee object automatically 
 * injected into a setter method.
 */
package by.phinc.pmc.action.utils;

import by.phinc.pmc.model.beans.Employee;

public interface EmployeeAware {
	
	void setEmployee(Employee employee);
}
