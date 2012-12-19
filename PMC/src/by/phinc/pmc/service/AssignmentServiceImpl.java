package by.phinc.pmc.service;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Task;

public class AssignmentServiceImpl extends AbstractService 
			implements IAssignmentService {
	

	@Override
	@Transactional(readOnly=true)
	public Assignment findById(Integer id) {
		return getDaoFactory().getAssignmentDAO().findById(id, false);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Assignment> findAll() {
		return getDaoFactory().getAssignmentDAO().findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Assignment> findByExample(Assignment example) {
		return getDaoFactory().getAssignmentDAO().findByExample(example);
	}

	@Override
	@Transactional
	public Assignment makePersistent(Assignment model) {
		return getDaoFactory().getAssignmentDAO().makePersistent(model);
	}

	@Override
	@Transactional
	public void makeTransient(Assignment model) {
		getDaoFactory().getAssignmentDAO().makeTransient(model);
	}

	@Override
	@Transactional(readOnly=true)
	public void findWithPagination(Pagination<Assignment, Task> pagination) {
		getDaoFactory().getAssignmentDAO().findWithPagination(pagination);
	}

	@Override
	@Transactional(readOnly=true)
	public void findUserAssignmentWithPagination(
			Pagination<Assignment, Employee> pagination) {
		getDaoFactory().getAssignmentDAO().findUserAssignmentWithPagination(pagination);
	}

}
