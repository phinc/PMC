<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="activityDialog" title="Activity">
	<s:form action="%{actionMethod}" method="post">
		<s:hidden key="id"/>
		<s:hidden key="actionMethod" value="%{actionMethod}"/>
		<s:hidden key="assignment.id"/>
		<s:textfield key="description" readonly="%{readOnly}"
					 cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="startDate" readonly="%{readOnly}"
					value="%{getText('format.date',{startDate})}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="duration" readonly="%{readOnly}"
					value="%{getText('format.number',{duration})}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield key="reporter.name" readonly="true"
					label="Reporter" cssClass="text ui-widget-content ui-corner-all"/>
	</s:form>
	<p><s:property value="message" default=""/></p>
</div>