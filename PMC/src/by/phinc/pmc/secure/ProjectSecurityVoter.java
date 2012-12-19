package by.phinc.pmc.secure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectSecurityVoter implements AccessDecisionVoter {
	
	public static final String ROLE_PREFIX = "ROLE_";

	@Override
	public Access vote(Authentication authentication, Object secureObject,
			List<ConfigAttribute> config) {
		List<String> userRoles = getUserRoles(authentication.getAuthorities());
		List<String> elementRoles = getElementRoles(config);
		elementRoles.retainAll(userRoles);
		if (elementRoles.isEmpty()) {
			return Access.ACCESS_DENIED;
		}
		return Access.ACCESS_GRANTED;
	}
	
	private List<String> getUserRoles(Collection<GrantedAuthority> authorities){
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().startsWith(ROLE_PREFIX)) {
				roles.add(authority.getAuthority());
			}
		}
		return roles;
	}
	
	private List<String> getElementRoles(List<ConfigAttribute> attributes){
		List<String> roles = new ArrayList<String>();
		for (ConfigAttribute attribute : attributes) {
			if (attribute.getAttribute().startsWith(ROLE_PREFIX)) {
				roles.add(attribute.getAttribute());
			}
		}
		return roles;
	}

}
