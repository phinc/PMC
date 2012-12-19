package by.phinc.pmc.model.dao;

import java.util.Date;
import java.util.Set;

import by.phinc.pmc.action.utils.Pagination;
import by.phinc.pmc.model.beans.Activity;
import by.phinc.pmc.model.beans.Assignment;
import by.phinc.pmc.model.beans.Employee;
import by.phinc.pmc.util.TimeJournalBean;

public interface IActivityDAO extends GenericDAO<Activity, Integer> {
	
	/*
	 * Fill in the assignment's collection of activities
	 */
	void fillAssignmentWithActivities(Assignment assignment);
	
	
	/*
	 * Fetch the collection of activities by the assignment id.
	 * Perform the pagination of the result
	 */
	void findWithPagination(Pagination<Activity, Assignment> pagination);
	
	/*
	 * Perform search of the employee's activity time in the given period
	 */
	Set<TimeJournalBean> findUserActivityByDate(Date date, Employee employee);
}
