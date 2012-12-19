/*
 * RedirectAware
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 03.08.2012
 * 
 * This interface allows actions to have the redirect string automatically 
 * injected into a setter method.
 */
package by.phinc.pmc.action.utils;

public interface RedirectAware {
	
	void setRedirect(boolean redirect);
}
