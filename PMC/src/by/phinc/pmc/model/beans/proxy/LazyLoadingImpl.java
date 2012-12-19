package by.phinc.pmc.model.beans.proxy;

import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.dao.GenericDAO;

public class LazyLoadingImpl implements ILazyLoading {
	
	private boolean initialized;
	
	private GenericDAO<IModel<Integer>, Integer> dao;
	
	private IModel<Integer> model;
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public void init(Integer id) {
		model = dao.findById(id, false);
		initialized = true;
	}

	@Override
	public void setDAO(GenericDAO<IModel<Integer>, Integer> dao) {
		this.dao = dao;
	}

}
