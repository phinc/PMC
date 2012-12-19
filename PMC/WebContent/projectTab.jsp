<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="projectTabId">
	<s:actionerror/>
	<s:form action="%{actionMethod}" method="post">
		<s:hidden key="id"/>
		<s:hidden key="actionMethod" value="%{actionMethod}"/>
		<s:textfield name="name" label="Name" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textarea name="description" label="Description" cols="18" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="customer" label="Customer" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:select name="status" list="#application['statuses']" label="Status" 
				  readonly="%{readOnly}" cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="planStart" label="Planned start date" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="planDuration" label="Planned duration" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="actStart" label="Actual start date" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="actDuration" label="Actual duration" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>		
		<s:select key="projectManager.employee.id" cssClass="text ui-widget-content ui-corner-all"
					list="employees" listKey="id" listValue="lastName + ' ' + firstName" readonly="%{readOnly}"/>
	</s:form>
	<p id="projectTabMessage"><s:property value="message" default=""/></p>
</div>