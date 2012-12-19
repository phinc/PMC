package by.phinc.pmc.secure;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import by.phinc.pmc.action.utils.RedirectAware;
import by.phinc.pmc.exception.PMCAccessDeniedException;
import by.phinc.pmc.exception.PMCAuthenticationException;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

public class ExceptionTranslationInterceptor  extends AbstractInterceptor {
	
	private static final long serialVersionUID = 27L;
	
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionTranslationInterceptor.class);
	

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Action action = (Action)invocation.getAction();
		try {
			return invocation.invoke();
		} catch (PMCAuthenticationException authExc) {
			LOG.debug("Employee is unauthenticated. Redirect to login page");
			putCurrentRequestToSession(invocation);
			setRedirect(action);
			return Action.LOGIN;
		} catch (PMCAccessDeniedException accExc) {			
			Authentication auth = obtainAuthentication(invocation);
			if (auth == null) {
				LOG.debug("Employee is unauthenticated. Redirect to login page");
				putCurrentRequestToSession(invocation);
				setRedirect(action);
				return Action.LOGIN;
			}
			LOG.debug("Access denied");
			throw accExc;			
		}
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void putCurrentRequestToSession(ActionInvocation invocation) {
		final ActionContext context = invocation.getInvocationContext();
		Map session = context.getSession();
		HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);		
		String currentRequest = request.getServletPath() + 
				((request.getQueryString() != null)? "?" + request.getQueryString() : "");
		session.put(SecureConstants.SECURE_REJECTED_REQUEST_KEY, currentRequest);
	}
	
	/*
	 * set action redirect field to true indicating that client should implement
	 * redirection to login page
	 */
	private void setRedirect (Action action) {
		if (action instanceof RedirectAware) {
			((RedirectAware)action).setRedirect(true);
		}
	}
}
