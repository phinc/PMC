package by.phinc.pmc.model.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Document;
import by.phinc.pmc.model.beans.DocumentOwner;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;

public class DocumentHibernateDAOImpl extends
		GenericHibernateDAO<Document, Integer> implements IDocumentDAO {
	

	@Override
	public void fillOwnerWithDocuments(DocumentOwner owner) {
		// purposely left blank due to hibernate lazy loading
	}

	@SuppressWarnings("unchecked")
	@Override
	public void findWithPaginationByProject(Pagination<Document, Project> pagination) {
		
		String queryString = "select d from Project p join p.documents d where p = :project" + 
				((pagination.getSidx() != null && !pagination.getSidx().isEmpty())
					? " order by d." + pagination.getSidx() + " " + pagination.getSord()
					: "");
		Query query = getSession().createQuery(queryString)
						.setEntity("project", pagination.getOwner())
						.setFirstResult((pagination.getPage() - 1) * pagination.getRows())
						.setMaxResults(pagination.getRows());
		pagination.setEntities((Collection<Document>)query.list());
		
		String queryStr = "select count(d) from Project p join p.documents d where p = :project";
		Query query1 = getSession().createQuery(queryStr)
				.setEntity("project", pagination.getOwner());
		List<Long> result1 = query1.list();
		pagination.setRecords((result1.get(0)).intValue());
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void findWithPaginationByTask(Pagination<Document, Task> pagination) {
		
		String queryString = "select d from Task t join t.documents d where t = :task" + 
				((pagination.getSidx() != null && !pagination.getSidx().isEmpty())
					? " order by d." + pagination.getSidx() + " " + pagination.getSord()
					: "");
		Query query = getSession().createQuery(queryString)
						.setEntity("task", pagination.getOwner())
						.setFirstResult((pagination.getPage() - 1) * pagination.getRows())
						.setMaxResults(pagination.getRows());
		pagination.setEntities((Collection<Document>)query.list());
		
		String queryStr = "select count(d) from Task t join t.documents d where t = :task";
		Query query1 = getSession().createQuery(queryStr)
				.setEntity("task", pagination.getOwner());
		List<Long> result1 = query1.list();
		pagination.setRecords((result1.get(0)).intValue());
	}

}
