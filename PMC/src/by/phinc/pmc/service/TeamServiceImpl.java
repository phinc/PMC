package by.phinc.pmc.service;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.transaction.annotation.Transactional;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;

public class TeamServiceImpl extends AbstractService implements ITeamService {

	@Override
	@Transactional(readOnly=true)
	public TeamMember findById(Integer id) {
		return getDaoFactory().getTeamDAO().findById(id, false);
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<TeamMember> findAll() {
		return getDaoFactory().getTeamDAO().findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Collection<TeamMember> findByExample(TeamMember example) {
		return getDaoFactory().getTeamDAO().findByExample(example);
	}

	@Override
	@Transactional
	public TeamMember makePersistent(TeamMember model) {
		return getDaoFactory().getTeamDAO().makePersistent(model);
	}

	@Override
	@Transactional
	public void makeTransient(TeamMember model) {
		getDaoFactory().getTeamDAO().makeTransient(model);
	}

	@Override
	@Transactional(readOnly=true)
	public void findWithPagination(Pagination<TeamMember, Project> pagination) {
		getDaoFactory().getTeamDAO().findWithPagination(pagination);
	}

	@Override
	public Collection<TeamMember> getProjectTeam(Project project) {
		TeamMember memberExample = new TeamMember();
		memberExample.setProject(project);
		SortedSet<TeamMember> col = new TreeSet<TeamMember>(
				getDaoFactory().getTeamDAO().findByExample(memberExample));
		project.setTeam(col);
		return col;
	}

}
