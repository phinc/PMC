/*
 * MainAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 * 
 * Remove assignment, project, team member and task objects from the session map.
 */
package by.phinc.pmc.controller;

import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements SessionAware	{

	private static final long serialVersionUID = 27L;
	
	@SuppressWarnings("rawtypes")
	private Map session;
	
	
	public String execute() {
		session.remove(Constants.PROJECT);
		session.remove(Constants.TASK);
		session.remove(Constants.ASSIGNMENT);
		session.remove(Constants.TEAM_MEMBER);
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
