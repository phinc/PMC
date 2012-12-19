/*
 * UserAssignmentsJSON
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class fetches the collection of assignments assigned to current authorised employee.
 * It also performs pagination based on given number of rows on 
 * the page and page number. The collection of assignments is sorted by given 
 * field (sidx property) and the sort order (sord property).
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.action.utils.EmployeeAware;
import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.service.IAssignmentService;

public class UserAssignmentsJSON extends BaseJSONAction 
		implements EmployeeAware {
	
    private static final long serialVersionUID = 27L;
	
    private IAssignmentService assignmentService;
    
	private Pagination<Assignment, Employee> pagination = 
			new Pagination<Assignment, Employee>();	
	
	
	public void setAssignmentService(IAssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	public void setId(Integer id) {
		pagination.setId(id);
	}
	
	public Integer getId() {
		return pagination.getId();
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
	
	public Collection<Assignment> getAssignments() {
		return pagination.getEntities();
	}
	
	@Override
	public void setEmployee(Employee employee) {
		pagination.setOwner(employee);
	}
	
	public String execute() {
		assignmentService.findUserAssignmentWithPagination(pagination);
		return SUCCESS;
	}

}
