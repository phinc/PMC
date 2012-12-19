<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="projectTabId">
	<s:form action="%{actionMethod}" method="post">
		<s:hidden key="id"/>
		<s:hidden key="actionMethod" value="%{actionMethod}"/>
		<s:hidden key="version"/>
		<s:textfield key="name" readonly="%{readOnly}"
					 cssClass="text ui-widget-content ui-corner-all"/>
		<s:textarea key="description" cols="18" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="customer" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:select key="status" list="#application['statuses']" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="planStart" readonly="%{readOnly}"
					value="%{getText('format.date',{planStart})}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="planDuration" readonly="%{readOnly}"
					value="%{getText('format.number',{planDuration})}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="actStart" readonly="%{readOnly}"
					value="%{getText('format.date',{actStart})}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="actDuration" readonly="%{readOnly}"
					value="%{getText('format.number',{actDuration})}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:if test="actionMethod == 'ProjectCRUD_add' || actionMethod == 'ProjectCRUD_save'">
			<s:select key="projectManager.employee.id" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"
					list="employees" listKey="id" listValue="lastName + ' ' + firstName" />
		</s:if>
	</s:form>
	<p id="projectTabMessage"><s:property value="message" default=""/></p>
</div>