package by.phinc.pmc.controller;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.service.IActivityService;
import by.phinc.pmc.service.IEmployeeService;
import by.phinc.pmc.util.TimeJournalBean;
import by.phinc.pmc.util.WeekDays;

import com.opensymphony.xwork2.Preparable;

public class TimeJournalAction extends BaseAction implements 
			Preparable{

	private static final long serialVersionUID = 27L;
	
	private IEmployeeService employeeService;
	
	private IActivityService activityService;
	
	private Employee employee;
	
	private Date date;
	
	private Set<TimeJournalBean> result;
	
	private WeekDays weekDays;
	
	private Collection<Employee> employees;
	
	

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Set<TimeJournalBean> getResult() {
		return result;
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}	
	
	public WeekDays getWeekDays() {
		return weekDays;
	}

	@Override
	public void prepare() throws Exception {
		//current date is default value
		if (getDate() == null) {
			setDate(new Date());
		}
		
		//fill the employee list
		employees = employeeService.findAll();
	}
	
	public String execute() {
		result = activityService.findUserActivityByDate(getDate(), employee);
		weekDays = new WeekDays(date);
		return SUCCESS;
	}

	
}
