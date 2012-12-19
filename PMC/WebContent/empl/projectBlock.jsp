<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	<s:if test="#session['project']">
		<s:url action="UserProject" var="projectUrl">
			<s:param name="id" value="#session['project'].id"/>
		</s:url>
		<b>Project:</b> <a href="<s:property value='#projectUrl'/>">
		<s:property value="#session['project'].name"/></a><br/>
		<b>Status:</b> <s:property value="#session['project'].status"/>
	</s:if>
</div>