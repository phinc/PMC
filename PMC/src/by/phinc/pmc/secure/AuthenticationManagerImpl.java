package by.phinc.pmc.secure;

import by.phinc.pmc.exception.PMCAuthenticationException;
import by.phinc.pmc.exception.PMCBadCredentialsException;
import by.phinc.pmc.exception.PMCLoginNotFoundException;
import by.phinc.pmc.model.beans.UserDetails;
import by.phinc.pmc.service.IUserDetailsService;

public class AuthenticationManagerImpl implements AuthenticationManager {
	
	private IUserDetailsService userDetailsService;
	
	
	public IUserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(IUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}


	@Override
	public Authentication authenticate(Authentication auth)
			throws PMCAuthenticationException {
		try {
			UserDetails userDetails = userDetailsService.loadEmployeeByLogin(auth.getLogin());
			if (userDetails.getPassword().equals(auth.getPassword())) {
				return new Authentication(userDetails, 
						userDetails.getAuthorities());
			}
		} catch (PMCLoginNotFoundException e) {
			throw new PMCBadCredentialsException();
		}
		throw new PMCBadCredentialsException();
	}
	
}
