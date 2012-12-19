<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div id="loginBlock" class="ui-tabs ui-widget ui-widget-content ui-corner-all"><br/>
	<s:if test="#session['by.phinc.pmc.secure.Authentication']">
		Welcome <s:property value="#session['by.phinc.pmc.secure.Authentication'].userDetails.firstName"/>
		<s:bean name="by.phinc.pmc.util.SecurityBean" var="security">
			<s:param name="employee" value="#session['by.phinc.pmc.secure.Authentication'].userDetails"/>
			<s:param name="project" value="#session['project']"/>
		</s:bean><br/>		
		<s:if test="#security.projectManager">Your role is  project manager</s:if>
		<s:elseif test="#security.projectMember">Your role is  project member</s:elseif>
		<s:if test="#security.admin"><br/>Hi, admin</s:if>
		<s:form action="Logout"><s:submit key="button.logout"/></s:form>
	</s:if>
	<s:else>
		<s:actionerror/>
		<s:form action="Login">
			<s:textfield key="login"
						 cssClass="text ui-widget-content ui-corner-all" />
			<s:password key="password"
						cssClass="text ui-widget-content ui-corner-all"/>
			<s:submit key="button.login"/>
		</s:form>
	</s:else>
</div>