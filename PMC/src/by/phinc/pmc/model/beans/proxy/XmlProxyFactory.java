package by.phinc.pmc.model.beans.proxy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.model.beans.IModel;
import by.phinc.pmc.model.beans.Project;
import by.phinc.pmc.model.beans.Task;
import by.phinc.pmc.model.beans.TeamMember;
import by.phinc.pmc.model.dao.GenericDAO;
import by.phinc.pmc.service.IAssignmentService;
import by.phinc.pmc.service.IEmployeeService;
import by.phinc.pmc.service.IProjectService;
import by.phinc.pmc.service.ITaskService;
import by.phinc.pmc.service.ITeamService;

public class XmlProxyFactory implements ProxyFactory, ApplicationContextAware {
	
	private ApplicationContext appContext;
	
	@Override
	public Project getProjectProxy(Integer id) {
		ProjectProxy proxy = new ProjectProxy(id);
		proxy.setService((IProjectService)appContext.getBean("projectService"));
		return proxy;
		
//		GenericDAO<IModel<Integer>, Integer> dao = (GenericDAO)appContext.getBean("projectDAO");
//		return (IProject)ModelProxy.newInstance(dao, new Project());
		
//		Project project = (Project)appContext.getBean("projectEntity");
//		project.setId(id);
//		System.out.println(project.getId());
//		GenericDAO<IModel<Integer>, Integer> dao = (GenericDAO)appContext.getBean("projectDAO");
//		((ILazyLoading)project).setDAO(dao);
//		return project;
	}

	@Override
	public Task getTaskProxy(Integer id) {
		TaskProxy proxy = new TaskProxy(id);
		proxy.setService((ITaskService)appContext.getBean("taskService"));
		return proxy;
	}

	@Override
	public Assignment getAssignmenmtProxy(Integer id) {
		AssignmentProxy proxy = new AssignmentProxy(id);
		proxy.setService((IAssignmentService)appContext.getBean("assignmentService"));
		return proxy;
	}

	@Override
	public Employee getEmployeeProxy(Integer id) {
		EmployeeProxy proxy = new EmployeeProxy(id);
		proxy.setService((IEmployeeService)appContext.getBean("employeeService"));
		return proxy;
	}

	@Override
	public TeamMember getTeamMemberProxy(Integer id) {
		TeamMemberProxy proxy = new TeamMemberProxy(id);
		proxy.setService((ITeamService)appContext.getBean("teamService"));
		return proxy;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.appContext = arg0;
	}

}
