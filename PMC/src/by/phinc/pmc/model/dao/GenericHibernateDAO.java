package by.phinc.pmc.model.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;

import by.phinc.pmc.model.beans.IModel;

@Repository
public abstract class GenericHibernateDAO<T extends IModel<ID>, ID extends Serializable> 
		implements GenericDAO<T, ID> {
	
	private Class<T> persistentClass;
    
	private Session session;
 
    private SessionFactory sessionFactory;
	
	public GenericHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                                .getGenericSuperclass()).getActualTypeArguments()[0];
     }
 
   
    public void setSession(Session s) {
        this.session = s;
    }
 
    protected Session getSession() {
//        if (session == null)
//            throw new IllegalStateException("Session has not been set on DAO before usage");
        return sessionFactory.getCurrentSession();
    }
 
    public Class<T> getPersistentClass() {
        return persistentClass;
    }
 
    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

    @Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	@SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock)
            entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
        else
            entity = (T) getSession().load(getPersistentClass(), id);
 
        return entity;
    }
 

    public Collection<T> findAll() {
        return findByCriteria();
    }
 
    @SuppressWarnings("unchecked")
    public Collection<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example =  Example.create(exampleInstance);
        if (excludeProperty != null) {
	        for (String exclude : excludeProperty) {
	            example.excludeProperty(exclude);
	        }
        }
        crit.add(example);
        return crit.list();
    }
    
    
 
    @Override
	public Collection<T> findByExample(T exampleInstance) {
		return findByExample(exampleInstance, null);
	}

    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }
 
    public void makeTransient(T entity) {
        getSession().delete(entity);
    }
 
    public void flush() {
        getSession().flush();
    }
 
    public void clear() {
        getSession().clear();
    }
 
    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
   }
}
