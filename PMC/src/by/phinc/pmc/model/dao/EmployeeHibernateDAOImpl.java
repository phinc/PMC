package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.SORT_DIR_ASC;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.TeamMember;

public class EmployeeHibernateDAOImpl extends
		GenericHibernateDAO<Employee, Integer> implements IEmployeeDAO {


	@SuppressWarnings("unchecked")
	@Override
	public void findWithPagination(final Pagination<Employee, Employee> pagination) {
		Criteria crit = getSession().createCriteria(Employee.class);
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			crit.addOrder(Order.asc(pagination.getSidx()));
		} else {
			crit.addOrder(Order.desc(pagination.getSidx()));
		}
		crit.setFirstResult((pagination.getPage() - 1) * pagination.getRows());
		crit.setMaxResults(pagination.getRows());
		pagination.setEntities((Collection<Employee>)crit.list());
		
		String queryString = "select count(p) from Employee p";
		Query query = getSession().createQuery(queryString);
		List<Long> result = query.list();
		pagination.setRecords(((Long)result.get(0)).intValue());
	}

	
	@Override
	public void makeTransient(Employee entity) {
		//remove references from TeamMember
		Query query = getSession().createQuery("from TeamMember t where t.employee=:employee");
		query.setParameter("employee", entity);
		Collection team = query.list();
		TeamMember member = null;
		for(Iterator it = team.iterator(); it.hasNext();) {
			member = (TeamMember)it.next();
			member.setEmployee(null);
		}
		//remove references from Activity
		Query queryA = getSession().createQuery("from Activity a where a.reporter=:employee");
		queryA.setParameter("employee", entity);
		Collection activities = queryA.list();
		Activity activity = null;
		for(Iterator i = activities.iterator(); i.hasNext();) {
			activity = (Activity)i.next();
			activity.setReporter(null);
		}
		super.makeTransient(entity);
	}
	
}
