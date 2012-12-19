/*
 * UserProjectsJSON
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class fetches the collection of projects assigned to the current authorized employee.
 * It also performs pagination based on given number of rows on 
 * the page and page number. The collection of projects is sorted by given 
 * field (sidx property) and the sort order (sord property).
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.action.utils.EmployeeAware;
import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.secure.Roles;
import by.phinc.pmc.service.IProjectService;

public class UserProjectsJSON extends BaseJSONAction 
		implements EmployeeAware{

	private static final long serialVersionUID = 1L;
	
	private IProjectService projectService;
	
	private Pagination<Project, Employee> pagination = new Pagination<Project, Employee>();
	
	
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
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
	
	public Collection<Project> getProjects() {
		return pagination.getEntities();
	}
	
	@Override
	public void setEmployee(Employee employee) {
		pagination.setOwner(employee);
	}

	@Roles(value="ROLE_ADMIN, ROLE_PROJECT_MANAGER, ROLE_PROJECT_MEMBER")
	public String execute() {
		projectService.findUserProjectWithPagination(pagination);
		return SUCCESS;
	}

}
