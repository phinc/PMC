package by.phinc.pmc.model.beans.proxy;

import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.service.IEmployeeService;

public class EmployeeProxy extends Employee implements IProxy<IEmployeeService> {
	
	private Employee employee;
	
	private IEmployeeService service;
	
	public EmployeeProxy(Integer id) {
		super();
		setId(id);
	}
	
	

	@Override
	public void setService(IEmployeeService service) {
		this.service = service;
	}


	private Employee getEmployee() {
		if (employee == null) {
			employee = service.findById(getId());
		}
		return employee;
	}


	@Override
	public String getFirstName() {
		return getEmployee().getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		getEmployee().setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return getEmployee().getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		getEmployee().setLastName(lastName);
	}

	@Override
	public String getEmail() {
		return getEmployee().getEmail();
	}

	@Override
	public void setEmail(String email) {
		getEmployee().setEmail(email);
	}

	@Override
	public String getLogin() {
		return getEmployee().getLogin();
	}

	@Override
	public void setLogin(String login) {
		getEmployee().setLogin(login);
	}

	@Override
	public String getPassword() {
		return getEmployee().getPassword();
	}

	@Override
	public void setPassword(String password) {
		getEmployee().setPassword(password);
	}

	@Override
	public String getPost() {
		return getEmployee().getPost();
	}

	@Override
	public void setPost(String post) {
		getEmployee().setPost(post);
	}

	@Override
	public String getName() {
		return getEmployee().getName();
	}
	
}
