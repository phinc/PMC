package by.phinc.pmc.model.dao;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Query;
import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.TeamMember;

public class TeamHibernateDAOImpl extends
		GenericHibernateDAO<TeamMember, Integer> implements ITeamDAO {
	
	public static final String POST = "post";
	
	public static final String FIRST_NAME = "firstName";
	
	public static final String LAST_NAME = "lastName";
	

	@Override
	public Collection<Project> getProjectsByEmployee(Employee employee) {
		String queryString = "select p from Project p join p.team t where " +
				"t.employee = :employee";
		Query query = getSession().createQuery(queryString)
						.setEntity("employee", employee);
		@SuppressWarnings("unchecked")
		SortedSet<Project> result = new TreeSet<Project>(query.list());
		return result;
	}

	@Override
	public void fillProjectWithTeam(Project project) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findWithPagination(Pagination<TeamMember, Project> pagination) {		
		String queryTeam = "select t from TeamMember t join t.employee e where " +
				"t.project=:project";
		if (pagination.getSidx() != null && !pagination.getSidx().isEmpty()) {
			if (POST.equals(pagination.getSidx()) 
					|| FIRST_NAME.equals(pagination.getSidx())
					|| LAST_NAME.equals(pagination.getSidx())) {
				queryTeam += " order by e." + pagination.getSidx() + " " + 
					pagination.getSord();
			} else {
				queryTeam += " order by t." + pagination.getSidx() + " " + 
						pagination.getSord();
			}
		}
		Query queryT = getSession().createQuery(queryTeam)
				.setEntity("project", pagination.getOwner())
				.setFirstResult((pagination.getPage() - 1) * pagination.getRows())
				.setMaxResults(pagination.getRows());
		Collection<TeamMember> list = queryT.list();
		pagination.setEntities(list);
		
		String queryString = "select count(p) from TeamMember p join p.employee " +
				"where p.project=:project";
		Query query = getSession().createQuery(queryString);
		query.setEntity("project", pagination.getOwner());
		List<Long> result = query.list();
		pagination.setRecords(result.get(0).intValue());
	}

	
	/*
	 * returns collection of employees who is not a member of the given project team
	 */
//	@Override
//	public Collection<Employee> getEmployeesOutOfTeam(Project project) {
//		String queryString = "select e from Employee e where e not in (select empl from " +
//				"TeamMember t left join t.employee empl where t.project=:project)";
//		Query query = getSession().createQuery(queryString)
//				.setEntity("project", project);
//		List list = query.list();
//		SortedSet<Employee> employees = new TreeSet<Employee>(list);
//		return employees;
//	}

}
