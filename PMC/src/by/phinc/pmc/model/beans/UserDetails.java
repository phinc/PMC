package by.phinc.pmc.model.beans;

import java.util.Collection;

import by.phinc.pmc.secure.GrantedAuthority;

public interface UserDetails extends IModel<Integer> {

	public String getLogin();

	public String getPassword();

	public Collection<GrantedAuthority> getAuthorities(); 
	
}
