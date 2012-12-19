/*
 * AssignmentAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class puts current assignment object  to the session map.
 * Check user security constrains.
 */
package by.phinc.pmc.controller;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import by.phinc.pmc.action.utils.EmployeeAware;
import by.phinc.pmc.exception.PMCObjectNotFoundException;
import by.phinc.pmc.exception.PMCProjectNotAllowesException;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.service.IAssignmentService;
import by.phinc.pmc.util.SecurityBean;

public class AssignmentAction extends BaseAction 
	implements SessionAware, EmployeeAware	{
	
	private static final long serialVersionUID = 27L;
	
	@SuppressWarnings("rawtypes")
	private Map session;
	
	private IAssignmentService assignmentService;
	
	private Employee employee;
	
	private Integer id;
	
	

	public void setAssignmentService(IAssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public String execute() {
		Assignment assignment = assignmentService.findById(id);
		if (assignment == null) {
			throw new PMCObjectNotFoundException("Assignment object with id = " + 
						id + "is not found");
		}
		session.put(Constants.ASSIGNMENT, assignment);
		
		Task task = assignment.getTask();
		session.put(Constants.TASK, assignment.getTask());
		
		//check if the user permitted to view the page
		Project project = task.getProject();
		SecurityBean security = new SecurityBean(employee, project);
		if (!(security.isProjectMember() || security.isAdmin())) {
			throw new PMCProjectNotAllowesException(employee.getName() + 
					" \n You have not right to view the project " + 
					project.getName());
		}
		session.put(Constants.PROJECT, project);
		
		session.remove(Constants.TEAM_MEMBER);
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
