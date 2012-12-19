package by.phinc.pmc.secure;

import by.phinc.pmc.exception.PMCAuthenticationException;

public interface AuthenticationManager {
	
	Authentication authenticate(Authentication auth) 
			throws PMCAuthenticationException;
}
