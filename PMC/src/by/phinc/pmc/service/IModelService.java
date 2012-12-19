package by.phinc.pmc.service;

import java.io.Serializable;
import java.util.Collection;

import by.phinc.pmc.model.beans.IModel;

public interface IModelService<M extends IModel<ID>, ID extends Serializable> {
	
	M findById(ID id);
	
	Collection<M> findAll();
	
	Collection<M> findByExample(M exmple);
	
	M makePersistent(M model);
	
	void makeTransient(M model);
}
