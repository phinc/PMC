/*
 * DAOFactory
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 *
 * Provide abstract factory methods.
 */
package by.phinc.pmc.factory;

//import static by.phinc.pmc.util.Constants.RESORCE_BUNDLE;
//
//import java.util.ResourceBundle;
//
//import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.dao.IActivityDAO;
import by.phinc.pmc.model.dao.IAssignmentDAO;
import by.phinc.pmc.model.dao.IDocumentDAO;
import by.phinc.pmc.model.dao.IEmployeeDAO;
import by.phinc.pmc.model.dao.IProjectDAO;
import by.phinc.pmc.model.dao.ITaskDAO;
import by.phinc.pmc.model.dao.ITeamDAO;
import by.phinc.pmc.model.dao.IUserDetailsDAO;


//@SuppressWarnings("unchecked")
public abstract class DAOFactory {
	
//	public static final Class<? extends DAOFactory> factory;	
//	
//	static {
//		ResourceBundle bundle = ResourceBundle.getBundle(
//				RESORCE_BUNDLE);
//		String clazz = bundle.getString("daofactory.class");
//		try {			
//			factory = (Class<? extends DAOFactory>)Class.forName(clazz);
//		} catch (ClassNotFoundException e) {
//			throw new PMCException("Could not instantiate class: " + clazz);
//		}
//	}
//	
//	public static DAOFactory instance() {
//		try {
//			return (DAOFactory)factory.newInstance();
//		} catch (Exception e) {
//			throw new PMCException("Could not create DAOFactory: " + factory);
//		}
//	}
//	
	
	public abstract IProjectDAO getProjectDAO();
	
	public abstract IEmployeeDAO getEmployeeDAO();
	
	public abstract ITaskDAO getTaskDAO();
	
	public abstract ITeamDAO getTeamDAO();
	
	public abstract IAssignmentDAO getAssignmentDAO();
	
	public abstract IActivityDAO getActivityDAO();
	
	public abstract IDocumentDAO getDocumentDAO();
	
	public abstract IUserDetailsDAO getUserDetailsDAO();
}
