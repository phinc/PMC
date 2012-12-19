package by.phinc.pmc.service;

import java.util.Collection;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;

public interface ITeamService extends IModelService<TeamMember, Integer> {
	
	/*
	 * Fetch the collection of TeamMembers by the given project id.
	 * Perform the pagination of the result.
	 */
	void findWithPagination(Pagination<TeamMember, Project> pagination);
	
	/*
	 * Fetch the collection of TeamMembers of the given project.
	 */
	Collection<TeamMember> getProjectTeam(Project project);
}
