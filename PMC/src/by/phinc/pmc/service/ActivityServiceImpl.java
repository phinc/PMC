package by.phinc.pmc.service;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.util.TimeJournalBean;

public class ActivityServiceImpl extends AbstractService implements
		IActivityService {

	@Override
	@Transactional(readOnly=true)
	public Activity findById(Integer id) {
		return getDaoFactory().getActivityDAO().findById(id, false);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Activity> findAll() {
		return getDaoFactory().getActivityDAO().findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<Activity> findByExample(Activity example) {
		return getDaoFactory().getActivityDAO().findByExample(example);
	}

	@Override
	@Transactional
	public Activity makePersistent(Activity model) {
		return getDaoFactory().getActivityDAO().makePersistent(model);
	}

	@Override
	@Transactional
	public void makeTransient(Activity model) {
		getDaoFactory().getActivityDAO().makeTransient(model);
	}

	@Override
	@Transactional(readOnly=true)
	public void findWithPagination(Pagination<Activity, Assignment> pagination) {
		getDaoFactory().getActivityDAO().findWithPagination(pagination);
	}

	@Override
	@Transactional(readOnly=true)
	public Set<TimeJournalBean> findUserActivityByDate(Date date,
			Employee employee) {
		return getDaoFactory().getActivityDAO().findUserActivityByDate(date, employee);
	}

}
