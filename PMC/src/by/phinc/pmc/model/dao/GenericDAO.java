package by.phinc.pmc.model.dao;

import java.io.Serializable;
import java.util.Collection;

import by.phinc.pmc.model.beans.IModel;

public interface GenericDAO<T extends IModel<ID>, ID extends Serializable> {
	
	T findById(ID id, boolean lock);
	 
    Collection<T> findAll();
 
    Collection<T> findByExample(T exampleInstance);
 
    T makePersistent(T entity);
 
    void makeTransient(T entity);
}
