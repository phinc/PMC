/*
 * ProjectCRUD
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class provides CRUD capabilities for project object.
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import org.hibernate.StaleObjectStateException;

import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.IEmployeeService;
import by.phinc.pmc.service.IModelService;
import by.phinc.pmc.service.IProjectService;
import by.phinc.pmc.util.SecurityBean;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class ProjectCRUD extends BaseCRUDAction 
		implements ModelDriven<Project>, Preparable {
	
	private static final long serialVersionUID = 27L;
	
	private static final String DESTINATION = "/projectDialog.jsp";
	
	private IProjectService projectService;
	
	private IEmployeeService employeeService;
	
	private Project model;
	
	private TeamMember projectManager;
	
	private Collection<Employee> employees;

	
	
	
	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	@Override
	public Project getModel() {
		return model;
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}	

	public TeamMember getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(TeamMember projectManager) {
		this.projectManager = projectManager;
	}

	@Override
	public void prepare() throws Exception {
		//fill the employee list
		employees = employeeService.findAll();
		
		if (getId() == null) {
			model = new Project();
		} else {
			model = projectService.findById(getId());
		}
	}
	
	/*
	 * It enables compatibility with hibernate
	 * in order to prevent saving of invalid data to db
	 * by keeping it in detached state
	 */
//	public void prepareUpdate() throws Exception {
//		setId(null);
//	}
	
	/*
	 * Ensure assigning a project manager to project
	 * @see by.phinc.pmc.controller.BaseAction#save()
	 */
	@Override
	public String save() {
		try {
			projectManager.setRole(SecurityBean.PROJECT_MANAGER_ROLE);
			projectManager.setProject(getModel());
			projectService.saveProjectWithProjectManager(model, projectManager);
		} catch (StaleObjectStateException e) {
			setMessage("You use stale data");
		}
		return SUCCESS;
	}		

	
	@Override
	public String getDestination() {
		return DESTINATION;
	}
	
	public void validate() {		
		if (getModel().getName().length() == 0) {
			addFieldError("name", getText("project.name.required"));
		}
		if (getModel().getDescription().length() == 0) {
			addFieldError("description", getText("project.description.required"));
		}
		if (getModel().getCustomer().length() == 0) {
			addFieldError("customer", getText("project.customer.required"));
		}
		if (getModel().getPlanStart() == null) {
			addFieldError("planStart", getText("project.planStart.required"));
		}
		if (getModel().getPlanDuration() == null) {
			addFieldError("planDuration", getText("project.planDuration.required"));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelService<IModel<Integer>, Integer> getService() {
		return (IModelService)projectService;
	}

}
