<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="employeeTabId">
	<s:actionerror/>
	<s:form action="%{actionMethod}" method="post">
		<s:hidden key="id"/>
		<s:hidden key="actionMethod" value="%{actionMethod}" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="firstName" label="First name" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="lastName" label="Last name" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="email" label="Email" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="login" label="Login" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:textfield name="post" label="Post" readonly="%{readOnly}"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:password name="password" label="Password"
					cssClass="text ui-widget-content ui-corner-all"/>
	</s:form>
	<p><s:property value="message" default=""/></p>
</div>