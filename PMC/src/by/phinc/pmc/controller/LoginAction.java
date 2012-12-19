/*
 * LoginAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 *
 * The login action does user authentication.
 * It searches for the employee object by the given login and password 
 * and  put it into the session map. Otherwise control is passed back to 
 * the login page.
 */
package by.phinc.pmc.controller;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import by.phinc.pmc.exception.PMCBadCredentialsException;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.secure.Authentication;
import by.phinc.pmc.secure.AuthenticationManager;
import by.phinc.pmc.secure.SecureConstants;
import by.phinc.pmc.service.IEmployeeService;
import by.phinc.pmc.util.SecurityBean;

public class LoginAction extends BaseAction implements SessionAware {

	private static final long serialVersionUID = 27L;
	
	private IEmployeeService employeeService;
	
	private AuthenticationManager authenticationManager;
	
	@SuppressWarnings("rawtypes")
	private Map session;
	
	private String login;
	
	private String password;
	
	private String url;
	
	
	
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
		
	public String getUrl() {
		return url;
	}

	@SuppressWarnings("unchecked")
	public String execute() {
		//Employee employee = employeeService.getEmployee(getLogin());
		Authentication request = new Authentication(login, password);
		try {
			Authentication auth = authenticationManager.authenticate(request);
			session.put(SecureConstants.AUTHENICATION_KEY, auth);
			
			setURLOfOriginalRequest();
			if (url != null) {
				return Constants.REDIRECT;
			}
		} catch (PMCBadCredentialsException ex) {
			addActionError(getText("login.fail"));
			return INPUT;
		}
		return SUCCESS;
		
//		if (employee != null && employee.getPassword().equals(getPassword())) {
//			session.put(Constants.EMPLOYEE, employee);
//			SecurityBean security = new SecurityBean();
//			security.setEmployee(employee);
//			if (security.isAdmin()) {
//				return Constants.ADMIN;
//			}
//			return SUCCESS;
//		} else {
//			addActionError(getText("login.fail"));
//			return INPUT;
//		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setSession(Map session) {
		this.session = session;
	}
	
	private void setURLOfOriginalRequest() {
		Object obj = session.get(SecureConstants.SECURE_REJECTED_REQUEST_KEY);
		if ((obj != null) && (obj instanceof String)) {
			url = (String)obj;
		}
	}

}
