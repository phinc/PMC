/*
 * EmployeeCRUD
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class provides CRUD capabilities for employee object.
 */
package by.phinc.pmc.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.service.IEmployeeService;
import by.phinc.pmc.service.IModelService;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class EmployeeCRUD extends BaseCRUDAction 
		implements ModelDriven<Employee>, Preparable {

	private static final long serialVersionUID = 27L;
	
	private static final String DESTINATION = "/employeeTab.jsp";
	
	private static final String EMAIL_PATTERN = 
			"^[\\w-]+(\\.[\\w-]+)*@[a-zA-Z0-9]+(\\.[A-Za-z0-9]+)*\\.[a-zA-Z]{2,6}$";
	
	private IEmployeeService employeeService;
	
	private Pattern pattern;
	
	private Employee model;
	

	public EmployeeCRUD() {
		super();
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	@Override
	public Employee getModel() {
		return model;
	}
	

	@Override
	public void prepare() throws Exception {
		if (getId() == null) {
			model = new Employee();
		} else {
			model = employeeService.findById(getId());
		}
	}

	
	@Override
	public String getDestination() {
		return DESTINATION;
	}
	
	public void validate() {
		if (getModel().getFirstName().length() == 0) {
			addFieldError("firstName", getText("first.name.required"));
		}
		if (getModel().getLastName().length() == 0) {
			addFieldError("lastName", getText("last.name.required"));
		}
		if (getModel().getLogin().length() == 0) {
			addFieldError("login", getText("login.required"));
		} else if (employeeService.isEmployeeWithLogin(model)) {
			addFieldError("login", getText("login.used"));
		}
		if (getModel().getPost().length() == 0) {
			addFieldError("post", getText("post.required"));
		}
		if (getModel().getPassword().length() == 0) {
			addFieldError("password", getText("password.required"));
		}
		if (getModel().getEmail().length() == 0) {
			addFieldError("email", getText("email.required"));
		}
		Matcher matcher = pattern.matcher(getModel().getEmail());
		if (!matcher.matches()) {
			addFieldError("email", getText("email.invalid"));
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelService<IModel<Integer>, Integer> getService() {
		return (IModelService)employeeService;
	}
}
