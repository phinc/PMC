/*
 * TeamJSONAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class fetches the collection of project members according to the given project id.
 * It also performs pagination based on given number of rows on 
 * the page and page number. The collection of project members is sorted by given 
 * field (sidx property) and the sort order (sord property).
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.IProjectService;
import by.phinc.pmc.service.ITeamService;

public class TeamJSONAction extends BaseJSONAction {

	private static final long serialVersionUID = 27L;
	
	private ITeamService teamService;
	
	private IProjectService projectService;
	
	private Pagination<TeamMember, Project> pagination = 
			new Pagination<TeamMember, Project>();
	
	
	
	public void setTeamService(ITeamService teamService) {
		this.teamService = teamService;
	}

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
	
	public Collection<TeamMember> getTeam() {
		return pagination.getEntities();
	}

	public String execute() {
		pagination.setOwner(projectService.findById(getId()));
		teamService.findWithPagination(pagination);
		return SUCCESS;
	}
}
