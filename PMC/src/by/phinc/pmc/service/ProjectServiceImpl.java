package by.phinc.pmc.service;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.secure.Roles;

public class ProjectServiceImpl extends AbstractService implements IProjectService {
	
	
	@Override
	@Transactional(readOnly=true)
	@Roles(value="ROLE_ADMIN, ROLE_PROJECT_MANAGER, ROLE_PROJECT_MEMBER")
	public Project findById(Integer id) {
		return daoFactory.getProjectDAO().findById(id, false);
	}

	@Override
	@Transactional(readOnly=true)
	@Roles(value="ROLE_ADMIN, ROLE_PROJECT_MANAGER, ROLE_PROJECT_MEMBER")
	public Collection<Project> findAll() {
		return daoFactory.getProjectDAO().findAll();
	}

	@Override
	@Transactional(readOnly=true)
	@Roles(value="ROLE_ADMIN, ROLE_PROJECT_MANAGER, ROLE_PROJECT_MEMBER")
	public Collection<Project> findByExample(Project exmple) {
		return daoFactory.getProjectDAO().findByExample(exmple);
	}

	@Override
	@Transactional
	@Roles(value="ROLE_ADMIN, ROLE_PROJECT_MANAGER")
	public Project makePersistent(Project project) {
		return daoFactory.getProjectDAO().makePersistent(project);
	}

	@Override
	@Transactional
	@Roles(value="ROLE_ADMIN")
	public void makeTransient(Project project) {
		daoFactory.getProjectDAO().makeTransient(project);
	}

	@Override
	@Transactional(readOnly=true)
	@Roles(value="ROLE_ADMIN, ROLE_PROJECT_MANAGER, ROLE_PROJECT_MEMBER")
	public void findWithPagination(Pagination<Project, Project> pagination) {
		daoFactory.getProjectDAO().findWithPagination(pagination);
	}

	@Override
	@Transactional(readOnly=true)
	@Roles(value="ROLE_ADMIN, ROLE_PROJECT_MANAGER, ROLE_PROJECT_MEMBER")
	public void findUserProjectWithPagination(Pagination<Project, Employee> pagination) {
		daoFactory.getProjectDAO().findUserProjectWithPagination(pagination);
	}

	@Override
	@Transactional
	@Roles(value="ROLE_ADMIN")
	public void saveProjectWithProjectManager(Project project,
			TeamMember manager) {
		project.addTeamMember(manager);
		daoFactory.getProjectDAO().makePersistent(project);
		daoFactory.getTeamDAO().makePersistent(manager);
	}

}
