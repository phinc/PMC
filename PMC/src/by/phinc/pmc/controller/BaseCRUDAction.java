/*
 * BaseAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 *
 * Provides basic CRUD functionality.
 */
package by.phinc.pmc.controller;

import org.apache.struts2.interceptor.validation.SkipValidation;

import by.phinc.pmc.action.utils.RedirectAware;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.service.IModelService;


public abstract class BaseCRUDAction extends BaseAction implements RedirectAware {

	private static final long serialVersionUID = 27L;
	
	private Integer id;
	
	private boolean readOnly;
	
	private String message;
	
	private String mappedRequest;
	
	private boolean redirect;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMappedRequest(String actionMethod) {
		this.mappedRequest = getActionClass() + "_" + actionMethod;
	}

	@Override
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}

	public boolean isRedirect() {
		return redirect;
	}

	@SkipValidation
	public String show() {
		setReadOnly(true);
		message = "";
		setMappedRequest(Constants.SHOW);
		return SUCCESS;
	}
	
	//prepare web page for saving
	@SkipValidation
	public String add() {
		message = "";
		setMappedRequest(Constants.SAVE);
		return SUCCESS;
	}
	
	public String save() {
		message = "";
		getService().makePersistent(getModel());
		message = getText("data.save.message");
		return SUCCESS;
	}
	
	//prepare web page for updating
	@SkipValidation
	public String edit() {
		message = "";
		setMappedRequest(Constants.UPDATE);
		return SUCCESS;
	}
	
	public String update() {
		message = "";
		getService().makePersistent(getModel());
		message = getText("data.update.message");
		return SUCCESS;
	}
	
	//prepare for deleting
	@SkipValidation
	public String destroy() {
		setReadOnly(true);
		message = getText("data.destroy.message");
		setMappedRequest(Constants.REMOVE);
		return SUCCESS;
	}
	
	@SkipValidation
	public String remove() {
		setReadOnly(true);
		message = "";
		getService().makeTransient(getModel());
		message = getText("data.remove.message");
		return SUCCESS;
	}
	
	
	public String getActionClass() {
		return getClass().getSimpleName();
	}
	
	public String getDestination() {
		return getClass().getSimpleName();
	}
	
	public String getActionMethod() {
		return mappedRequest;
	}
	
	public void setActionMethod(String method) {
		this.mappedRequest = method;
	}
	
	public abstract IModel<Integer> getModel();
	
	public abstract IModelService<IModel<Integer>, Integer> getService();
}
