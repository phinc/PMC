package by.phinc.pmc.model.dao;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import static by.phinc.pmc.util.Constants.*;

public class ProjectHibernateDAOImpl extends
		GenericHibernateDAO<Project, Integer> implements IProjectDAO {

	@SuppressWarnings("unchecked")
	@Override
	public void findWithPagination(Pagination<Project, Project> pagination) {
		Criteria crit = getSession().createCriteria(Project.class);
		if (pagination.getSidx() != null && !pagination.getSidx().isEmpty()) {
			if (SORT_DIR_ASC.equals(pagination.getSord())) {
				crit.addOrder(Order.asc(pagination.getSidx()));
			} else {
				crit.addOrder(Order.desc(pagination.getSidx()));
			}
		}
		crit.setFirstResult((pagination.getPage() - 1) * pagination.getRows());
		crit.setMaxResults(pagination.getRows());
		pagination.setEntities((Collection<Project>)crit.list());
		
		String queryString = "select count(p) from Project p";
		Query query = getSession().createQuery(queryString);
		List<Long> result = query.list();
		pagination.setRecords(((Long)result.get(0)).intValue());
	}

	@Override
	public void findUserProjectWithPagination(Pagination<Project, Employee> pagination) {
		String queryString = "select p from Project p join p.team t where " +
				"t.employee = :employee";
		if (pagination.getSidx() != null && !pagination.getSidx().isEmpty()) {
			queryString += " order by p." + pagination.getSidx() + " " + 
					pagination.getSord();
		}
		Query query = getSession().createQuery(queryString)
						.setEntity("employee", pagination.getOwner());
		
		@SuppressWarnings("unchecked")
		SortedSet<Project> result = new TreeSet<Project>(query.list());
		pagination.setEntities(result);
		
		String queryStr = "select count(p) from Project p join p.team t " +
				"where t.employee=:employee";
		Query queryCount = getSession().createQuery(queryStr);
		queryCount.setEntity("employee", pagination.getOwner());
		@SuppressWarnings("unchecked")
		List<Long> resultCount = queryCount.list();
		pagination.setRecords((resultCount.get(0)).intValue());
	}

	
}
