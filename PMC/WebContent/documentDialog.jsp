<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="documentDialog" title="Task">
	<s:actionerror/>
	<s:form action="%{actionMethod}" method="post" enctype="multipart/form-data">
		<s:hidden key="id"/>
		<s:hidden key="actionMethod" value="%{actionMethod}"/>
		<!--<s:hidden key="owner.id"/>-->
		<s:textfield name="description" label="Description" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:file name="file" label="File" cssClass="text ui-widget-content ui-corner-all"/>
		<s:submit></s:submit>
	</s:form>
	<p><s:property value="message" default=""/></p>
</div>