package by.phinc.pmc.util;

import static by.phinc.pmc.util.Constants.RESORCE_BUNDLE;

import java.util.Collection;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import by.phinc.pmc.factory.DAOFactory;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.model.dao.ITeamDAO;

public class SecurityBean {
	
	private static final String ADMIN_LOGIN;
	
	private static final String ADMIN_PASSWORD;
	
	public static final String PROJECT_MANAGER_ROLE;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(
				RESORCE_BUNDLE);
		ADMIN_LOGIN = bundle.getString("admin.login");
		ADMIN_PASSWORD = bundle.getString("admin.password");
		PROJECT_MANAGER_ROLE = bundle.getString("project.manager.role");
	}
	
	private Employee employee;
	
	//the project object should be  supplied with a team field filled in
	private Project project;
	
//	@Autowired
//	@Qualifier("daoFactory")
//	private DAOFactory daoFactory;

	
	public SecurityBean() {
		super();
	}	

	public SecurityBean(Employee employee, Project project) {
		super();
		this.employee = employee;
		this.project = project;
	}


	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
//	public DAOFactory getDaoFactory() {
//		return daoFactory;
//	}
//
//	public void setDaoFactory(DAOFactory daoFactory) {
//		this.daoFactory = daoFactory;
//	}

	public boolean isAdmin() {
		return ADMIN_LOGIN.equals(employee.getLogin()) &&
				ADMIN_PASSWORD.equals(employee.getPassword());
	}
	
	public boolean isProjectManager() {
		if ((project == null) || (employee == null)) {
			return false;
		}
//		Collection<TeamMember> team = getProjectTeam();
		if (project.getTeam() == null || project.getTeam().isEmpty()) {
			return false;
		}
		TeamMember member = getTeamMember(project.getTeam());
		return (member != null) && PROJECT_MANAGER_ROLE.equalsIgnoreCase(member.getRole());
	}
	
	public boolean isProjectMember() {
		if ((project == null) || (employee == null)) {
			return false;
		}
//		Collection<TeamMember> team = getProjectTeam();
		if (project.getTeam() == null || project.getTeam().isEmpty()) {
			return false;
		}
		TeamMember member = getTeamMember(project.getTeam());
		return member != null;
	}
	
//	private Collection<TeamMember> getProjectTeam() {
//		if (project.getTeam() == null || project.getTeam().isEmpty()) {
//			ITeamDAO teamDAO = daoFactory.getTeamDAO();
//			teamDAO.fillProjectWithTeam(project);
//		}
//		return project.getTeam();
//	}
	
	private TeamMember getTeamMember(Collection<TeamMember> team) {
		for (TeamMember member : team) {
			if(employee.equals(member.getEmployee())) {
				return member;
			}
		}
		return null;
	}
}
