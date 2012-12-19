package by.phinc.pmc.secure;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import by.phinc.pmc.exception.PMCAccessDeniedException;
import by.phinc.pmc.exception.PMCAuthenticationException;

import com.opensymphony.xwork2.ActionContext;

@Aspect
public class AspectSecurityInterceptor extends AbstractSecurityInterceptor {
	
	@Pointcut("execution(* by.phinc.pmc.service.*.*(..))")
	public void businessService(){}
	
	@Around("businessService() && @annotation(annotation)")
	public Object invoke(ProceedingJoinPoint  pjp, by.phinc.pmc.secure.Roles annotation) throws Throwable {		
		List<ConfigAttribute> attributes = getAttributes(annotation);
		//invocation is secured only if the list of ConfigAttributes is not empty
		if (attributes != null && !attributes.isEmpty()) {
			Authentication auth = obtainAuthentication();			
			if (auth != null && auth.isAuthenticated()) {
				try {
					getAccessDecisionManager().decide(auth, pjp.getTarget(), attributes);
					return pjp.proceed();
				} catch (PMCAccessDeniedException e) {
					throw e;
				}
			} else {
				throw new PMCAuthenticationException();
			}
		} else {
			return pjp.proceed();
		}
	}
	
	private Authentication obtainAuthentication() {
		ActionContext context = ActionContext.getContext();
		Object obj = context.getSession().get(SecureConstants.AUTHENICATION_KEY);
		if (obj instanceof Authentication) {
			return (Authentication)obj;
		}
		return null;
	}
	
	
	
	private List<ConfigAttribute> getAttributes(by.phinc.pmc.secure.Roles annotation) {
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
}
