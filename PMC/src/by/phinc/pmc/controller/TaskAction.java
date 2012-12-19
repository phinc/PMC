/*
 * TaskAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * Put the current task object to the session map.
 * Remove if there is assignment object from the session map.
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
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.service.ITaskService;
import by.phinc.pmc.util.SecurityBean;

public class TaskAction extends BaseAction 
	implements SessionAware, EmployeeAware{
	
	private static final long serialVersionUID = 27L;

	@SuppressWarnings("rawtypes")
	private Map session;
	
	private ITaskService taskService;
	
	private Employee employee;
	
	private Integer id; //task id
	
	

	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public String execute() {
		Task task = taskService.findById(id);
		if (task == null) {
			throw new PMCObjectNotFoundException("Task object with id = " + 
					id + "is not found");
		}
		session.put(Constants.TASK, task);
		//check if the user permitted to view the page
		Project project = task.getProject();
		SecurityBean security = new SecurityBean(employee, project);
		if (!(security.isProjectMember() || security.isAdmin())) {
			throw new PMCProjectNotAllowesException(employee.getName() + 
					" \n You have not right to view the project " + 
					project.getName());
		}
		session.put(Constants.PROJECT, project);
		
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
