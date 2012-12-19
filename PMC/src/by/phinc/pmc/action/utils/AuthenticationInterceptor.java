/*
 * AuthenticationInterceptor
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * Check whether the request is coming from an authenticated employee.
 * If the employee hasn’t been authenticated, the interceptor will return 
 * a control string Action.LOGIN. The control string will route 
 * the employee to the login page.
 */
package by.phinc.pmc.action.utils;
import java.util.Map;

import by.phinc.pmc.controller.Constants;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.service.IEmployeeService;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;


public class AuthenticationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 27L;
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationInterceptor.class);
	
	private IEmployeeService employeeService;
	
	
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		@SuppressWarnings("rawtypes")
		Map session = actionInvocation.getInvocationContext().getSession();
		Action action = (Action)actionInvocation.getAction();
		
		Employee employee = (Employee)session.get(Constants.EMPLOYEE);		
		if (employee == null) {
			LOG.debug("Employee is unauthenticated. Redirect to login page", "");
			setRedirect(action);
			return Action.LOGIN;
		} else {
			Employee empl = employeeService.getEmployee(employee.getLogin());
			if (empl != null && empl.getPassword().equals(employee.getPassword())) {
				if (action instanceof EmployeeAware) {
					((EmployeeAware) action).setEmployee(employee);
				}
				LOG.debug("Authenticated employee: " + empl.getName(), "");
				return actionInvocation.invoke();
			} else {
				LOG.debug("Perhaps Employee account was deleted: " + employee.getName(), "");
				addActionError(action, "login.invalid");
				setRedirect(action);
				return Action.LOGIN;
			}			
		}
		
	}
	
	private void addActionError(Action action, String message) {
		if(action instanceof ValidationAware) {
			if (action instanceof TextProvider) {
				message = ((TextProvider)action).getText(message);
			}
			((ValidationAware) action).addActionError(message);
		}
	}
	
	private void setRedirect (Action action) {
		if (action instanceof RedirectAware) {
			((RedirectAware)action).setRedirect(true);
		}
	}

}
