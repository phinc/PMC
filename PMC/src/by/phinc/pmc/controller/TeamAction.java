/*
 * TeamAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * Put the current team member object to the session map.
 */
package by.phinc.pmc.controller;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import by.phinc.pmc.action.utils.EmployeeAware;
import by.phinc.pmc.exception.PMCObjectNotFoundException;
import by.phinc.pmc.exception.PMCProjectNotAllowesException;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.ITeamService;
import by.phinc.pmc.util.SecurityBean;

public class TeamAction extends BaseAction 
	implements SessionAware, EmployeeAware {
	
	private static final long serialVersionUID = 27L;

	@SuppressWarnings("rawtypes")
	private Map session;
	
	private ITeamService teamService;	
	
	private Employee employee;
	
	private Integer id; //team member id
	
	
	public void setTeamService(ITeamService teamService) {
		this.teamService = teamService;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public String execute() {
		TeamMember member = teamService.findById(id);
		if (member == null) {
			throw new PMCObjectNotFoundException("Team member object with id = " + 
					id + "is not found");
		}		
		//check if the user permitted to view the page
		Project project = member.getProject();
		SecurityBean security = new SecurityBean(employee, project);
		if (!(security.isProjectMember() || security.isAdmin())) {
			throw new PMCProjectNotAllowesException(employee.getName() + 
					" \n You have not right to view the information relaited " +
					"to the project " + project.getName());
		}
		session.put(Constants.TEAM_MEMBER, member);
		session.put(Constants.PROJECT, project);
		
		session.remove(Constants.TASK);
		session.remove(Constants.ASSIGNMENT);
		
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
