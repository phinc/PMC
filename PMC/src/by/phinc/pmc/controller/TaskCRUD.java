/*
 * TaskCRUD
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class provides CRUD capabilities for task object.
 */
package by.phinc.pmc.controller;

import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.service.IModelService;
import by.phinc.pmc.service.ITaskService;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class TaskCRUD extends BaseCRUDAction 
	implements ModelDriven<Task>, Preparable{

	private static final long serialVersionUID = 27L;
	
	private static final String DESTINATION = "/taskDialog.jsp";
	
	private ITaskService taskService;
	
	private Task model;

	
	
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}

	@Override
	public Task getModel() {
		return model;
	}
	
	/*
	 * It enables compatibility with hibernate
	 * in order to prevent saving of invalid data to db
	 * by keeping it in detached state
	 */
	public void prepareUpdate() throws Exception {
		setId(null);
	}
	
	@Override
	public void prepare() throws Exception {
		if (getId() == null) {
			model = new Task();
			model.setProject(new Project());
		} else {
			model = taskService.findById(getId());
		}
	}
	
	
	@Override
	public String getDestination() {
		return DESTINATION;
	}

	public void validate() {
		if (getModel().getName().length() == 0) {
			addFieldError("name", getText("task.name.required"));
		}
		if (getModel().getDescription().length() == 0) {
			addFieldError("description", getText("task.description.required"));
		}
		if (getModel().getPlanStart() == null) {
			addFieldError("planStart", getText("task.planStart.required"));
		}
		if (getModel().getPlanDuration() == null) {
			addFieldError("planDuration", getText("task.planDuration.required"));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelService<IModel<Integer>, Integer> getService() {
		return (IModelService)taskService;
	}


}
