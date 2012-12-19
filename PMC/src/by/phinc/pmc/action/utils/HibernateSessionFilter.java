/*
 * HibernateSessionFilter
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * Start and close hibernate per request transaction.
 */
package by.phinc.pmc.action.utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.SessionFactory;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.util.HibernateUtil;
/**
 * Servlet Filter implementation class HibernateSessionFilter
 */
public class HibernateSessionFilter implements Filter {
	
	private static final Logger LOG = LoggerFactory.getLogger(HibernateSessionFilter.class);
	
	private SessionFactory sf;
	
    /**
     * Default constructor. 
     */
    public HibernateSessionFilter() {}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		try {
           // sf.getCurrentSession().beginTransaction();
 
            chain.doFilter(request, response);
 
           // sf.getCurrentSession().getTransaction().commit();
 
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
 
            throw new ServletException(ex);
        } finally {
        	if (sf.getCurrentSession() != null && sf.getCurrentSession().isOpen()) {
        		sf.getCurrentSession().close();
        	}
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		sf = HibernateUtil.getSessionFactory();
	}

}
