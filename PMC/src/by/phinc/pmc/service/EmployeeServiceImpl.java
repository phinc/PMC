package by.phinc.pmc.service;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;

public class EmployeeServiceImpl extends AbstractService implements
		IEmployeeService {

	@Override
	@Transactional(readOnly=true)
	public Employee findById(Integer id) {
		return getDaoFactory().getEmployeeDAO().findById(id, false);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Employee> findAll() {
		return getDaoFactory().getEmployeeDAO().findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Employee> findByExample(Employee example) {
		return getDaoFactory().getEmployeeDAO().findByExample(example);
	}

	@Override
	@Transactional
	public Employee makePersistent(Employee model) {
		return getDaoFactory().getEmployeeDAO().makePersistent(model);
	}

	@Override
	@Transactional
	public void makeTransient(Employee model) {
		getDaoFactory().getEmployeeDAO().makeTransient(model);
	}

	@Override
	@Transactional(readOnly=true)
	public Employee getEmployee(String login) {
		Employee employee = new Employee();
		employee.setLogin(login);
		Collection<Employee> employees = getDaoFactory().getEmployeeDAO().findByExample(employee);
		if (employees != null && employees.size() == 1) {
			return employees.iterator().next();
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public void findWithPagination(Pagination<Employee, Employee> pagination) {
		getDaoFactory().getEmployeeDAO().findWithPagination(pagination);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean isEmployeeWithLogin(Employee employee) {
		Employee empl = new Employee();
		empl.setLogin(employee.getLogin());
		Collection<Employee> employees = getDaoFactory().getEmployeeDAO().findByExample(empl);
		return (employees != null) && (!employees.isEmpty());
	}

}
