package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.SORT_DIR_ASC;

import java.util.Calendar;
import java.util.Collection;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.util.TimeJournalBean;

public class ActivityHibernateDAOImpl extends
		GenericHibernateDAO<Activity, Integer> implements IActivityDAO {


	@Override
	public void fillAssignmentWithActivities(Assignment assignment) {
		// intentionally left blank		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void findWithPagination(final Pagination<Activity, Assignment> pagination) {		
		Criteria crit = getSession().createCriteria(Activity.class);
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			crit.addOrder(Order.asc(pagination.getSidx()));
		} else {
			crit.addOrder(Order.desc(pagination.getSidx()));
		}
		crit.setFirstResult((pagination.getPage() - 1) * pagination.getRows());
		crit.setMaxResults(pagination.getRows());
		crit.add(Restrictions.eq("assignment", pagination.getOwner()));
		pagination.setEntities((Collection<Activity>)crit.list());
		
		String queryString = "select count(p) from Activity p where p.assignment=:assignment";
		Query query = getSession().createQuery(queryString);
		query.setEntity("assignment", pagination.getOwner());
		List<Long> result = query.list();
		pagination.setRecords((result.get(0)).intValue());
	}

	@Override
	public Set<TimeJournalBean> findUserActivityByDate(java.util.Date date, Employee employee) {
		Calendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date start = new Date(calendar.getTime().getTime());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Date end = new Date(calendar.getTime().getTime());
		
		String queryString = "select sum(activity.duration), activity.assignment.id, activity.assignment.name, "
				+ "activity.startDate from Activity activity where activity.startDate "
				+ ">= :start and activity.startDate <= :end and " +
				"activity.assignment.member.employee= :employee group by activity.startDate, "
				+ "activity.assignment.id";
		Query query = getSession().createQuery(queryString);
		query.setDate("start", start);
		query.setDate("end", end);
		query.setEntity("employee", employee);
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.list();
		Set<TimeJournalBean> res = new TreeSet<TimeJournalBean>();
		TimeJournalBean bean;
		for (Object[] obj : results) {
			bean = new TimeJournalBean((Integer)obj[1], (String)obj[2], 
					(Date)obj[3], ((Double)obj[0]).floatValue());
			res.add(bean);
		}
		return res;
	}

	
}
