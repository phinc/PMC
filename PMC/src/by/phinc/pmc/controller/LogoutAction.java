/*
 * LogoutAction
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 *
 * Invalidate user session.
 */
package by.phinc.pmc.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

public class LogoutAction extends BaseAction 
	implements ServletRequestAware{
	
	private static final long serialVersionUID = 27L;
	
	private HttpServletRequest request;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String execute() throws Exception {
		request.getSession().invalidate();
		return SUCCESS;
	}
}
