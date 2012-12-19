<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="teamMemberDialog" title="Team member">
	<s:actionerror/>
	<s:form action="%{actionMethod}" method="post">
		<s:hidden key="id"/>
		<s:hidden key="actionMethod" value="%{actionMethod}"/>
		<s:hidden key="project.id"/>		
		<s:if test="actionMethod == 'TeamCRUD_update' || actionMethod == 'TeamCRUD_edit' 
					|| actionMethod == 'TeamCRUD_remove' || actionMethod == 'TeamCRUD_destroy'">
			<s:textfield key="employee.name" cssClass="text ui-widget-content ui-corner-all" readonly="true"/>
		</s:if>
		<s:else>
			<s:select key="employee.id" cssClass="text ui-widget-content ui-corner-all"
					list="employees" listKey="id" listValue="lastName + ' ' + firstName" readonly="%{readOnly}"/>
		</s:else>
		<s:textfield key="role" readonly="%{readOnly}" required="true"
					 cssClass="text ui-widget-content ui-corner-all"/>
	</s:form>
	<p><s:property value="message" default=""/></p>
</div>