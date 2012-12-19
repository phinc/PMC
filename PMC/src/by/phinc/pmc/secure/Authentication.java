package by.phinc.pmc.secure;

import java.util.Collection;

import by.phinc.pmc.model.beans.UserDetails;

public class Authentication {
	
	private UserDetails userDetails;
	
	private String login;
	
	private String password;
	
	private Collection<GrantedAuthority> authorities;
	
	private boolean authenticated;

	public Authentication(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}

	
	public Authentication(UserDetails userDetails, Collection<GrantedAuthority> authorities) {
		super();
		this.userDetails = userDetails;
		this.authorities = authorities;
		this.authenticated = true;
	}


	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}


	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}


	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}


	public boolean isAuthenticated() {
		return authenticated;
	}


	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	
}
