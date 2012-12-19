package by.phinc.pmc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static Configuration configuration;
	private static SessionFactory sessionFactory;

	// Create the initial SessionFactory from the default configuration files
	static {
		try {
			configuration = new Configuration();
			sessionFactory = configuration.configure().buildSessionFactory();
			// We could also let Hibernate bind it to JNDI:
			// configuration.configure().buildSessionFactory()
		} catch (Throwable ex) {
			// We have to catch Throwable, otherwise we will miss
			// NoClassDefFoundError and other subclasses of Error
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Returns the SessionFactory used for this static class.
	 *
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		/* Instead of a static variable, use JNDI:
		SessionFactory sessions = null;
		try {
			Context ctx = new InitialContext();
			String jndiName = "java:hibernate/HibernateFactory";
			sessions = (SessionFactory)ctx.lookup(jndiName);
		} catch (NamingException ex) {
			throw new InfrastructureException(ex);
		}
		return sessions;
		*/
		return sessionFactory;
	}
	
	public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
	
	
	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}
