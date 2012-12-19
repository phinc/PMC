<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="taskDialog" title="Task">
	<s:actionerror/>
	<s:form action="%{actionMethod}" method="post">
		<s:hidden key="id"/>
		<s:hidden key="actionMethod" value="%{actionMethod}"/>
		<s:hidden key="project.id"/>
		<s:textfield key="name" readonly="%{readOnly}"
					 cssClass="text ui-widget-content ui-corner-all" />
		<s:textarea key="description" readonly="%{readOnly}" cols="18"
					cssClass="text ui-widget-content ui-corner-all" />
		<s:select key="status" cssClass="text ui-widget-content ui-corner-all"
					list="#application['statuses']" readonly="%{readOnly}"/>
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
	</s:form>
	<p id="taskDialogMessage"><s:property value="message" default=""/></p>
</div>