/*
 * ActivityCRUD
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class provides CRUD capabilities for activity object.
 */
package by.phinc.pmc.controller;

import by.phinc.pmc.action.utils.EmployeeAware;
import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.service.IActivityService;
import by.phinc.pmc.service.IModelService;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class ActivityCRUD extends BaseCRUDAction implements ModelDriven<Activity>,
		Preparable, EmployeeAware {
	
	private static final long serialVersionUID = 27L;
	
	private static final String DESTINATION = "/activityDialog.jsp";
	
	private Activity model;
	
	private Employee employee;
	
	//set due to dependency injection
	private IActivityService activityService;
	
	
	@Override
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	@Override
	public Activity getModel() {
		return model;
	}
	
	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}
	
	
	@Override
	public void prepare() throws Exception {
		if (getId() == null) {
			model = new Activity();
			model.setReporter(employee);
		} else {
			model = activityService.findById(getId());
		}
	}
	
	
	@Override
	public String getDestination() {
		return DESTINATION;
	}

	public void validate() {
		if (getModel().getDescription().length() == 0) {
			addFieldError("description", getText("activity.description.required"));
		}
		if (getModel().getStartDate() == null) {
			addFieldError("startDate", getText("activity.startDate.required"));
		}
		if (getModel().getDuration() == null) {
			addFieldError("duration", getText("activity.duration.required"));
		}
		if ((getModel().getDuration() != null) && (getModel().getDuration() <= 0)) {
			addFieldError("duration", getText("activity.duration.beyond.range"));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IModelService<IModel<Integer>, Integer> getService() {
		return (IModelService)activityService;
	}

}
