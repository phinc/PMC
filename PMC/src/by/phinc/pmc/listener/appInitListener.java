package by.phinc.pmc.listener;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import by.phinc.pmc.model.beans.Status;

/**
 * Application Lifecycle Listener implementation class appInitListener
 *
 */
public class appInitListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public appInitListener() {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
       List<Status> statuses = Arrays.asList(Status.values());
       ServletContext servletContext = sce.getServletContext();       
       servletContext.setAttribute("statuses", statuses);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
