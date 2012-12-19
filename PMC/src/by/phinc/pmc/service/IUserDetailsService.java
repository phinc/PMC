package by.phinc.pmc.service;

import by.phinc.pmc.exception.PMCLoginNotFoundException;
import by.phinc.pmc.model.beans.UserDetails;

public interface IUserDetailsService extends IModelService<UserDetails, Integer> {
	
	UserDetails loadEmployeeByLogin(String login) throws PMCLoginNotFoundException;
}
