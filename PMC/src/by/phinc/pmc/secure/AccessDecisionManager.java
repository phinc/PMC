package by.phinc.pmc.secure;

import java.util.List;

import by.phinc.pmc.exception.PMCAccessDeniedException;

public interface AccessDecisionManager {
	
	/*
	 * Here it is taken a decision on whether or not to allow the user gain access
	 * to the secure resource. If access is not allowed the PMCAccessDeniedException
	 * has to be thrown.
	 */
	void decide(Authentication authentication, Object secureObject,
			List<ConfigAttribute> config) throws PMCAccessDeniedException;
}
