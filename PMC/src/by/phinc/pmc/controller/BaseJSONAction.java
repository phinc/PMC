package by.phinc.pmc.controller;

import by.phinc.pmc.action.utils.RedirectAware;

public abstract class BaseJSONAction extends BaseAction implements RedirectAware {

	private static final long serialVersionUID = 27L;
	
	private boolean redirect;
	

	public boolean isRedirect() {
		return redirect;
	}

	@Override
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	
}
