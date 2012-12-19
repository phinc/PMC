package by.phinc.pmc.service;

import by.phinc.pmc.factory.DAOFactory;

public abstract class AbstractService {
	
	protected DAOFactory daoFactory;

	public DAOFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
}
