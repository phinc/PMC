package by.phinc.pmc.service;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;

public class TaskServiceImpl extends AbstractService implements ITaskService {

	@Override
	@Transactional(readOnly=true)
	public Task findById(Integer id) {
		return getDaoFactory().getTaskDAO().findById(id, false);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Task> findAll() {
		return getDaoFactory().getTaskDAO().findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Task> findByExample(Task exmple) {
		return getDaoFactory().getTaskDAO().findByExample(exmple);
	}

	@Override
	@Transactional
	public Task makePersistent(Task model) {
		return getDaoFactory().getTaskDAO().makePersistent(model);
	}

	@Override
	@Transactional
	public void makeTransient(Task model) {
		getDaoFactory().getTaskDAO().makeTransient(model);
	}

	@Override
	@Transactional(readOnly=true)
	public void findWithPagination(Pagination<Task, Project> pagination) {
		getDaoFactory().getTaskDAO().findWithPagination(pagination);
	}


}
