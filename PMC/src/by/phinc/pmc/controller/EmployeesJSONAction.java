/*
 * EmployeesJSONAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class fetches the collection of all employees registered in the program.
 * It also performs pagination based on given number of rows on 
 * the page and page number. The collection of employees is sorted by given 
 * field (sidx property) and the sort order (sord property).
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.service.IEmployeeService;

public class EmployeesJSONAction extends BaseJSONAction {

	private static final long serialVersionUID = 27L;
	
	private IEmployeeService employeeService;
	
	private Pagination<Employee, Employee> pagination = 
			new Pagination<Employee, Employee>();	
	
	
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public int getPage() {
		return pagination.getPage();
	}

	public void setPage(int page) {
		pagination.setPage(page);
	}

	public int getRows() {
		return pagination.getRows();
	}

	public void setRows(int rows) {
		pagination.setRows(rows);
	}

	public void setSidx(String sidx) {
		pagination.setSidx(sidx);
	}

	public void setSord(String sord) {
		pagination.setSord(sord);
	}

	public int getRecords() {
		return pagination.getRecords();
	}

	public void setRecords(int records) {
		pagination.setRecords(records);
	}

	public int getTotal() {
		return pagination.getTotal();
	}
	
	public Collection<Employee> getEmployees() {
		return pagination.getEntities();
	}

	
	public String execute() {
		employeeService.findWithPagination(pagination);
		return SUCCESS;
	}
}
