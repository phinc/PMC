/*
 * ProjectAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * Put the current project object to the session map.
 * Remove if there is assignment, team member and task objects from the session map.
 * Check user security constrains.
 */
package by.phinc.pmc.controller;


import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import by.phinc.pmc.action.utils.EmployeeAware;
import by.phinc.pmc.exception.PMCObjectNotFoundException;
import by.phinc.pmc.exception.PMCProjectNotAllowesException;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.service.IProjectService;
import by.phinc.pmc.service.ITeamService;
import by.phinc.pmc.util.SecurityBean;

public class ProjectAction extends BaseAction
		implements SessionAware, EmployeeAware	{

	private static final long serialVersionUID = 27L;
	
	private IProjectService projectService;
	
	private ITeamService teamService;
	
	@SuppressWarnings("rawtypes")
	private Map session;
	
	private Employee employee;
	
	private Integer id;
	
	

	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

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
		Project project = projectService.findById(id);
		if (project == null) {
			throw new PMCObjectNotFoundException("Project object with id = " + 
					id + "is not found");
		}
		teamService.getProjectTeam(project);
//		SecurityBean security = new SecurityBean(employee, project);
//		if (!(security.isProjectMember() || security.isAdmin())) {
//			throw new PMCProjectNotAllowesException(employee.getName() + 
//					" \n You have not right to view the project " + 
//					project.getName());
//		}
		session.put(Constants.PROJECT, project);
		
		session.remove(Constants.TASK);
		session.remove(Constants.ASSIGNMENT);
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
