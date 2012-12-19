/*
 * TeamCRUD
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class provides CRUD capabilities for team member object.
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.IEmployeeService;
import by.phinc.pmc.service.IModelService;
import by.phinc.pmc.service.ITeamService;

public class TeamCRUD extends BaseCRUDAction 
		implements ModelDriven<TeamMember>, Preparable{

	private static final long serialVersionUID = 27L;
	
	private static final String DESTINATION = "/teamMemberDialog.jsp";
	
	private ITeamService teamService;
	
	private IEmployeeService employeeService;
	
	private TeamMember model;
	
	private Collection<Employee> employees;

	
	
	public void setTeamService(ITeamService teamService) {
		this.teamService = teamService;
	}

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	@Override
	public TeamMember getModel() {
		return model;
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}
	

	@Override
	public void prepare() throws Exception {
		//fill the employee list
		if (getId() == null) {
			model = new TeamMember();
			model.setProject(new Project());
		} else {
			model = teamService.findById(getId());
		}
		employees = employeeService.findAll();
	}
	

	@Override
	public String getDestination() {
		return DESTINATION;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelService<IModel<Integer>, Integer> getService() {
		return (IModelService)teamService;
	}	
	
}
