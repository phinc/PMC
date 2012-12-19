package by.phinc.pmc.secure;

public abstract class AbstractSecurityInterceptor {
	
	private AccessDecisionManager accessDecisionManager;
	
	private AuthenticationManager authenticationManager;

	public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public AccessDecisionManager getAccessDecisionManager() {
		return accessDecisionManager;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
	
	
}
