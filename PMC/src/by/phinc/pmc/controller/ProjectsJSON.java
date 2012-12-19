/*
 * ProjectsJSON
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class fetches the collection of all projects.
 * It also performs pagination based on the given number of rows on 
 * the page and the page number. The collection of projects is sorted by the given 
 * field (sidx property) and the the sort order (sord property).
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.service.IProjectService;

public class ProjectsJSON extends BaseJSONAction {
	
	private static final long serialVersionUID = 1L;
	
	private Pagination<Project, Project> pagination = new Pagination<Project, Project>();
	
	private IProjectService projectService;
	
	
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
	
	

	public IProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	
	public String execute() {
//		IProjectDAO projectDAO = getDaoFactory().getProjectDAO();
//		projectDAO.findWithPagination(pagination);
		projectService.findWithPagination(pagination);
		return SUCCESS;
	}
}
