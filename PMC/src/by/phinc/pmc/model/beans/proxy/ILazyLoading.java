package by.phinc.pmc.model.beans.proxy;

import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.dao.GenericDAO;

public interface ILazyLoading {
	
	/*
	 * return true if object fully initilized
	 */
	boolean isInitialized();
	
	/*
	 * initialize the object
	 */
	void init(Integer id);
	
	/*
	 * set dao object to be used while initializing the object
	 */
	void setDAO(GenericDAO<IModel<Integer>, Integer> dao);
}
