/*
 * AssignmentCRUD
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * The class provides CRUD capabilities for assignment object.
 */
package by.phinc.pmc.controller;

import java.util.Collection;

import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.IAssignmentService;
import by.phinc.pmc.service.IModelService;
import by.phinc.pmc.service.IProjectService;
import by.phinc.pmc.service.ITeamService;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class AssignmentCRUD extends BaseCRUDAction implements
		ModelDriven<Assignment>, Preparable {
	
	private static final long serialVersionUID = 27L;
	
	private static final String DESTINATION = "/assignmentDialog.jsp";
	
	private IAssignmentService assignmentService;
	
	private IProjectService projectService;
	
	private ITeamService teamService;
	
	private Assignment model;
	
	private Integer projectId;
	
	private Integer teamMemberId;
	
	private Collection<TeamMember> team;
	
	
	
	public void setAssignmentService(IAssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	public void setTeamService(ITeamService teamService) {
		this.teamService = teamService;
	}


	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}	

	public Integer getTeamMemberId() {
		return teamMemberId;
	}

	public void setTeamMemberId(Integer teamMemberId) {
		this.teamMemberId = teamMemberId;
	}

	public Collection<TeamMember> getTeam() {
		return team;
	}

	public void setTeam(Collection<TeamMember> team) {
		this.team = team;
	}

	@Override
	public Assignment getModel() {
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
	
	/*
	 * getId() returns assignment id.
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() throws Exception {
		
		//fill team collection
		Project project = projectService.findById(projectId);
		setTeam(teamService.getProjectTeam(project));
		
		//create assignment
		if (getId() == null) {
			model = new Assignment();
		} else {
			model = assignmentService.findById(getId());
			teamMemberId = model.getMember().getId();
		}
	}

	
	@Override
	public String save() {
		getModel().setMember(findTeamMember());
		return super.save();
	}	

	@Override
	public String update() {
		getModel().setMember(findTeamMember());
		return super.update();
	}
	
	private TeamMember findTeamMember() {
		for (TeamMember member : team) {
			if (member.getId().equals(teamMemberId)) {
				return member;
			}
		}
		TeamMember result = new TeamMember();
		result.setId(teamMemberId);
		return result;
	}

	@Override
	public String getDestination() {
		return DESTINATION;
	}

	public void validate() {
		if (getModel().getName().length() == 0) {
			addFieldError("name", getText("assignment.name.required"));
		}
		if (getModel().getDescription().length() == 0) {
			addFieldError("description", getText("assignment.description.required"));
		}
		if (getModel().getPlanStart() == null) {
			addFieldError("planStart", getText("assignment.planStart.required"));
		}
		if (getModel().getPlanDuration() == null) {
			addFieldError("planDuration", getText("assignment.planDuration.required"));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IModelService<IModel<Integer>, Integer> getService() {
		return (IModelService)assignmentService;
	}


}
