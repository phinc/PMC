package by.phinc.pmc.model.dao;

import static by.phinc.pmc.util.Constants.SORT_DIR_ASC;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;

public class TaskHibernateDAOImpl extends GenericHibernateDAO<Task, Integer>
		implements ITaskDAO {


	
//	@Override
//	public Collection<Task> getTasksByProjectId(Integer projectId) {
//		Project project = new Project();
//		project.setId(projectId);
//		List filteredCollection =
//				getSession().createFilter( project.getTasks(),
//				"order by this.id asc" ).list();
////		SQLQuery query = getSession().createSQLQuery(
////				"select {c.*}, {m.*} from task c join main m on m.id = c.main_id where project_id ="+projectId).addEntity("c", Task.class);
//		return filteredCollection;
//	}



	@Override
	public void fillProjectWithTasks(Project project) {
		// intentionally left blank	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void findWithPagination(final Pagination<Task, Project> pagination) {		
		Criteria crit = getSession().createCriteria(Task.class);
		if (SORT_DIR_ASC.equals(pagination.getSord())) {
			crit.addOrder(Order.asc(pagination.getSidx()));
		} else {
			crit.addOrder(Order.desc(pagination.getSidx()));
		}
		crit.setFirstResult((pagination.getPage() - 1) * pagination.getRows());
		crit.setMaxResults(pagination.getRows());
		crit.add(Restrictions.eq("project", pagination.getOwner()));
		pagination.setEntities((Collection<Task>)crit.list());
		
		String queryString = "select count(p) from Task p where p.project=:project";
		Query query = getSession().createQuery(queryString);
		query.setEntity("project", pagination.getOwner());
		List result = query.list();
		pagination.setRecords(((Long)result.get(0)).intValue());
	}

}
