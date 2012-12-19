package by.phinc.pmc.secure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.phinc.pmc.action.utils.EmployeeAware;
import by.phinc.pmc.exception.PMCAccessDeniedException;
import by.phinc.pmc.exception.PMCAuthenticationException;
import by.phinc.pmc.model.beans.Employee;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class ActionSecurityInterceptor extends AbstractSecurityInterceptor
		implements Interceptor {

	private static final long serialVersionUID = 27L;

	@Override
	public void destroy() {}

	@Override
	public void init() {}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {		
		List<ConfigAttribute> attributes = getAttributes(
				getRoleAnnotation(invocation));
		Authentication auth = obtainAuthentication(invocation);	;
		setEmployee ((Action)invocation.getAction(), auth);
		//invocation is secured only if the list of ConfigAttributes is not empty
		if (attributes != null && !attributes.isEmpty()) {					
			if (auth != null && auth.isAuthenticated()) {
				try {
					getAccessDecisionManager().decide(auth, invocation.getAction(), attributes);
					return invocation.invoke();
				} catch (PMCAccessDeniedException e) {
					throw e;
				}
			} else {
				throw new PMCAuthenticationException();
			}
		} else {
			return invocation.invoke();
		}
	}
	
	private List<ConfigAttribute> getAttributes(Roles annotation) {
		List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
		if (annotation != null) {
			String roles = annotation.value();
			String[] roleArray = roles.split(",");
			for (String role : roleArray) {
				attributes.add(new ConfigAttributeImpl(role.trim()));
			}
		}
		return attributes;
	}
	
	private Roles getRoleAnnotation(ActionInvocation invocation) throws Exception {
		Class<?> clazz = invocation.getAction().getClass();
		String methodName = invocation.getProxy().getMethod();
		Method method = clazz.getMethod(methodName);
		return method.getAnnotation(Roles.class);
	}
	
	@SuppressWarnings("rawtypes")
	private Authentication obtainAuthentication(ActionInvocation invocation) {
		Map session = invocation.getInvocationContext().getSession();
		Object obj = session.get(SecureConstants.AUTHENICATION_KEY);
		if (obj != null && obj instanceof Authentication) {
			return (Authentication)obj;
		}
		return null;
	}
	
	private void setEmployee (Action action, Authentication auth) {
		if ((action instanceof EmployeeAware) && (auth != null)) {
			((EmployeeAware) action).setEmployee((Employee)auth.getUserDetails());
		}
	}
}
