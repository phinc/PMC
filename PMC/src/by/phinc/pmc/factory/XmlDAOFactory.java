package by.phinc.pmc.factory;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.proxy.ProxyFactory;
import by.phinc.pmc.model.dao.ActivityXmlDAOImpl;
import by.phinc.pmc.model.dao.AssignmentXmlDAOImpl;
import by.phinc.pmc.model.dao.DocumentXmlDAOImpl;
import by.phinc.pmc.model.dao.EmployeeXmlDAOImpl;
import by.phinc.pmc.model.dao.GenericXmlDAO;
import by.phinc.pmc.model.dao.IActivityDAO;
import by.phinc.pmc.model.dao.IAssignmentDAO;
import by.phinc.pmc.model.dao.IDocumentDAO;
import by.phinc.pmc.model.dao.IEmployeeDAO;
import by.phinc.pmc.model.dao.IProjectDAO;
import by.phinc.pmc.model.dao.ITaskDAO;
import by.phinc.pmc.model.dao.ITeamDAO;
import by.phinc.pmc.model.dao.IUserDetailsDAO;
import by.phinc.pmc.model.dao.ProjectXmlDAOImpl;
import by.phinc.pmc.model.dao.TaskXmlDAOImpl;
import by.phinc.pmc.model.dao.TeamXmlDAOImpl;

public class XmlDAOFactory extends DAOFactory {
	
	private ProxyFactory proxyFactory;
	
	
	public void setProxyFactory(ProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

	@Override
	public IProjectDAO getProjectDAO() {
		ProjectXmlDAOImpl dao = (ProjectXmlDAOImpl)instantiateDAO(ProjectXmlDAOImpl.class);
		dao.setDocumentDAO(getDocumentDAO());
		dao.setTaskDAO(getTaskDAO());
		dao.setTeamDAO(getTeamDAO());
		return dao;
	}
	
	@Override
	public IEmployeeDAO getEmployeeDAO() {
		return (IEmployeeDAO)instantiateDAO(EmployeeXmlDAOImpl.class);
	}
	
	@Override
	public ITaskDAO getTaskDAO() {
		TaskXmlDAOImpl dao = (TaskXmlDAOImpl)instantiateDAO(TaskXmlDAOImpl.class);
		dao.setAssignmentDAO(getAssignmentDAO());
		dao.setDocumentDAO(getDocumentDAO());
		return dao;
	}
	
	@Override
	public ITeamDAO getTeamDAO() {
		return (ITeamDAO)instantiateDAO( TeamXmlDAOImpl.class);
	}
	
	@Override
	public IAssignmentDAO getAssignmentDAO() {
		AssignmentXmlDAOImpl dao = (AssignmentXmlDAOImpl)instantiateDAO(AssignmentXmlDAOImpl.class);
		dao.setActivityDAO(getActivityDAO());
		return dao;
	}
	
	@Override
	public IActivityDAO getActivityDAO() {
		return (IActivityDAO)instantiateDAO(ActivityXmlDAOImpl.class);
	}
	
	@Override
	public IDocumentDAO getDocumentDAO() {
		return (IDocumentDAO)instantiateDAO(DocumentXmlDAOImpl.class);
	}
	
	@SuppressWarnings("rawtypes")
	private GenericXmlDAO instantiateDAO(Class daoClass) {
        try {
            GenericXmlDAO dao = (GenericXmlDAO)daoClass.newInstance();
            dao.setProxyFactory(proxyFactory);
            return dao;
        } catch (Exception ex) {
            throw new PMCException("Can not instantiate DAO: " + daoClass, ex);
        }
    }

	@Override
	public IUserDetailsDAO getUserDetailsDAO() {
		// TODO Auto-generated method stub
		return null;
	}

}
