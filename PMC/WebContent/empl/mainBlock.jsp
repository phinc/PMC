<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	<s:bean name="by.phinc.pmc.util.SecurityBean" var="security">
		<s:param name="employee" value="#session['employee']"/>
		<s:param name="project" value="#session['project']"/>
	</s:bean><br/>
	<s:if test="#security.admin">
		<s:url action="AdminIndex" var="mainUrl"/>
	</s:if>
	<s:else>
		<s:url action="Index" var="mainUrl"/>
	</s:else>	
	<a href="<s:property value='#mainUrl'/>">
		Main
	</a>
</div>