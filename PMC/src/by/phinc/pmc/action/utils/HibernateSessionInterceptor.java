package by.phinc.pmc.action.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.exception.PMCNeedTransactionRallbackException;
import by.phinc.pmc.util.HibernateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

public class HibernateSessionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 27L;
	
	private static final Logger LOG = LoggerFactory.getLogger(HibernateSessionInterceptor.class);
	
	private static SessionFactory sf = HibernateUtil.getSessionFactory();
	
	protected static ThreadLocal <Session> hibernateHolder = new ThreadLocal <Session> ();

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		String result;
		Session session;
		
//		if (hibernateHolder.get() == null) {
//			LOG.debug("Hibernate session is not found in hibernateLocal");
//			//get new session
//			session = sf.openSession();
//			hibernateHolder.set(session);
//			session.beginTransaction();
//		}
		
		try {
	           sf.getCurrentSession().beginTransaction();
	 
	           result = actionInvocation.invoke();
	 
	           sf.getCurrentSession().getTransaction().commit();
	 
	        } catch (PMCNeedTransactionRallbackException rallbackExc) {
	        	LOG.info("Transaction rallback", rallbackExc);
	        	try {
	                if (sf.getCurrentSession().getTransaction().isActive()) {
	                    sf.getCurrentSession().getTransaction().rollback();
	                }
	            } catch (Throwable rbEx) {
	            	LOG.error("Fail to rollbak transaction", rbEx);
	            	throw new PMCException("Fail to rollbak transaction", rbEx);
	            }
	        	result = rallbackExc.getResult();
	        } catch (Throwable ex) {
	            LOG.error("Transaction error", ex);
	            try {
	                if (sf.getCurrentSession().getTransaction().isActive()) {
	                    sf.getCurrentSession().getTransaction().rollback();
	                }
	            } catch (Throwable rbEx) {
	            	LOG.error("Fail to rollbak transaction", rbEx);
	            	throw new PMCException("Fail to rollbak transaction", rbEx);
	            }
	 
	            throw new PMCException(ex);
	        } finally {
	        	if (sf.getCurrentSession() != null && sf.getCurrentSession().isOpen()) {
	        		sf.getCurrentSession().close();
	        	}
	        }
		return result;
	}

}
