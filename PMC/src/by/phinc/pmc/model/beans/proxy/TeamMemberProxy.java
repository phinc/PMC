package by.phinc.pmc.model.beans.proxy;

import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.service.ITeamService;

public class TeamMemberProxy extends TeamMember implements IProxy<ITeamService> {
	
	private TeamMember member;
	
	private ITeamService service;
	
	public TeamMemberProxy(Integer id) {
		super();
		setId(id);
	}

	

	@Override
	public void setService(ITeamService service) {
		this.service = service;
	}
	


	private TeamMember getTeamMember() {
		if (member == null) {
			member = service.findById(getId());
		}
		return member;
	}

	@Override
	public Employee getEmployee() {
		return getTeamMember().getEmployee();
	}

	@Override
	public void setEmployee(Employee employee) {
		getTeamMember().setEmployee(employee);
	}

	@Override
	public String getRole() {
		return getTeamMember().getRole();
	}

	@Override
	public void setRole(String role) {
		getTeamMember().setRole(role);
	}

	@Override
	public String getFirstName() {
		return getTeamMember().getFirstName();
	}

	@Override
	public String getLastName() {
		return getTeamMember().getLastName();
	}

	@Override
	public String getPost() {
		return getTeamMember().getPost();
	}

	@Override
	public void setProject(Project project) {
		getTeamMember().setProject(project);
	}

	@Override
	public Project getProject() {
		return getTeamMember().getProject();
	}
	
}
