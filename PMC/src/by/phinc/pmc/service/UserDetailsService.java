package by.phinc.pmc.service;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.exception.PMCLoginNotFoundException;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.UserDetails;

public class UserDetailsService extends AbstractService implements IUserDetailsService {

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadEmployeeByLogin(String login)
			throws PMCLoginNotFoundException {		
		UserDetails userDetails = new Employee(login);
		Collection<Employee> res = getDaoFactory().getEmployeeDAO().findByExample((Employee)userDetails);
		if (res != null && res.size() == 1) {
			return res.iterator().next();
		}
		throw new PMCLoginNotFoundException();
	}

	@Override
	public UserDetails findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<UserDetails> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<UserDetails> findByExample(UserDetails exmple) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetails makePersistent(UserDetails model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeTransient(UserDetails model) {
		// TODO Auto-generated method stub
		
	}

}
