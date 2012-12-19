package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.SORT_DIR_ASC;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Task;

public class AssignmentHibernateDAOImpl extends
		GenericHibernateDAO<Assignment, Integer> implements IAssignmentDAO {
	

	@Override
	public void fillTaskWithAssignments(Task task) {
		//intentionally left blank	due to lazy loading
	}


	@SuppressWarnings("unchecked")
	@Override
	public void findWithPagination(final Pagination<Assignment, Task> pagination) {
		Criteria crit = getSession().createCriteria(Assignment.class);
		if (pagination.getSidx() != null && !pagination.getSidx().isEmpty()) {
			if (SORT_DIR_ASC.equals(pagination.getSord())) {
				crit.addOrder(Order.asc(pagination.getSidx()));
			} else {
				crit.addOrder(Order.desc(pagination.getSidx()));
			}
		}
		crit.setFirstResult(pagination.getFirstRow());
		crit.setMaxResults(pagination.getRows());
		crit.add(Restrictions.eq("task", pagination.getOwner()));
		pagination.setEntities((Collection<Assignment>)crit.list());
		
		//get total count
		String queryString = "select count(p) from Assignment p where p.task=:task";
		Query query = getSession().createQuery(queryString);
		query.setEntity("task", pagination.getOwner());
		List<Long> result = query.list();
		pagination.setRecords((result.get(0)).intValue());
	}

	/*
	 * return the collection of assignments relevant to the current employee
	 */
	@Override
	public void findUserAssignmentWithPagination(
			Pagination<Assignment, Employee> pagination) {
		String queryString = "select a from Assignment a join a.member m where " +
				"m.employee = :employee";
		if (pagination.getSidx() != null && !pagination.getSidx().isEmpty()) {
			queryString += " order by a." + pagination.getSidx() + " " + 
					pagination.getSord();
		}
		Query query = getSession().createQuery(queryString)
						.setEntity("employee", pagination.getOwner());
		query.setFirstResult(pagination.getFirstRow());
		query.setMaxResults(pagination.getRows());
		@SuppressWarnings("unchecked")
		SortedSet<Assignment> result = new TreeSet<Assignment>(query.list());
		pagination.setEntities(result);
		
		//get total count
		String queryStr = "select count(a) from Assignment a join a.member m " +
				"where m.employee=:employee";
		Query queryCount = getSession().createQuery(queryStr);
		queryCount.setEntity("employee", pagination.getOwner());
		@SuppressWarnings("unchecked")
		List<Long> resultCount = queryCount.list();
		pagination.setRecords((resultCount.get(0)).intValue());	
	}

}
