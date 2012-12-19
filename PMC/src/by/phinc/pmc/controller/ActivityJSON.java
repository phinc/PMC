/*
 * ActivityJSON
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class fetches the collection of activities for given assignment.
 * It also performs pagination based on given number of rows on 
 * the page and page number. The collection of activities is sorted by given 
 * field (sidx property) and the sort order (sord property).
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.service.IActivityService;
import by.phinc.pmc.service.IAssignmentService;

public class ActivityJSON extends BaseJSONAction {
	
	private static final long serialVersionUID = 27L;
	
	private IActivityService activityService;
	
	private IAssignmentService assignmentService;
		
	private Pagination<Activity, Assignment> pagination = 
			new Pagination<Activity, Assignment>();
	
	
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
	
	public Collection<Activity> getActivities() {
		return pagination.getEntities();
	}

	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}
	
	public void setAssignmentService(IAssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	
	public String execute() {
		pagination.setOwner(assignmentService.findById(getId()));
		activityService.findWithPagination(pagination);
		return SUCCESS;
	}
}
