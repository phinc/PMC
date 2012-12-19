package by.phinc.pmc.factory;

import org.hibernate.SessionFactory;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.dao.ActivityHibernateDAOImpl;
import by.phinc.pmc.model.dao.AssignmentHibernateDAOImpl;
import by.phinc.pmc.model.dao.DocumentHibernateDAOImpl;
import by.phinc.pmc.model.dao.EmployeeHibernateDAOImpl;
import by.phinc.pmc.model.dao.GenericHibernateDAO;
import by.phinc.pmc.model.dao.IActivityDAO;
import by.phinc.pmc.model.dao.IAssignmentDAO;
import by.phinc.pmc.model.dao.IDocumentDAO;
import by.phinc.pmc.model.dao.IEmployeeDAO;
import by.phinc.pmc.model.dao.IProjectDAO;
import by.phinc.pmc.model.dao.ITaskDAO;
import by.phinc.pmc.model.dao.ITeamDAO;
import by.phinc.pmc.model.dao.IUserDetailsDAO;
import by.phinc.pmc.model.dao.ProjectHibernateDAOImpl;
import by.phinc.pmc.model.dao.TaskHibernateDAOImpl;
import by.phinc.pmc.model.dao.TeamHibernateDAOImpl;
import by.phinc.pmc.model.dao.UserDetailsHibernateDAOImpl;

public class HibernateDAOFactory extends DAOFactory {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public IProjectDAO getProjectDAO() {
		return (IProjectDAO)instantiateDAO(ProjectHibernateDAOImpl.class);
	}

	@Override
	public IEmployeeDAO getEmployeeDAO() {
		return (IEmployeeDAO)instantiateDAO(EmployeeHibernateDAOImpl.class);
	}

	@Override
	public ITaskDAO getTaskDAO() {
		return (ITaskDAO)instantiateDAO(TaskHibernateDAOImpl.class);
	}

	@Override
	public ITeamDAO getTeamDAO() {
		return (ITeamDAO)instantiateDAO(TeamHibernateDAOImpl.class);
	}
	
	@Override
	public IAssignmentDAO getAssignmentDAO() {
		return (IAssignmentDAO)instantiateDAO(AssignmentHibernateDAOImpl.class);
	}
	
	@Override
	public IActivityDAO getActivityDAO() {
		return (IActivityDAO)instantiateDAO(ActivityHibernateDAOImpl.class);
	}
	
	@Override
	public IDocumentDAO getDocumentDAO() {
		return (IDocumentDAO)instantiateDAO(DocumentHibernateDAOImpl.class);
	}

	
	@SuppressWarnings("rawtypes")
	private GenericHibernateDAO instantiateDAO(Class daoClass) {
        try {
            GenericHibernateDAO dao = (GenericHibernateDAO)daoClass.newInstance();
//            dao.setSession(HibernateUtil.getCurrentSession());
            dao.setSessionFactory(sessionFactory);
            return dao;
        } catch (Exception ex) {
            throw new PMCException("Can not instantiate DAO: " + daoClass, ex);
        }
    }

	@Override
	public IUserDetailsDAO getUserDetailsDAO() {
		return (IUserDetailsDAO)instantiateDAO(UserDetailsHibernateDAOImpl.class);
	}
	
}
