<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	<s:if test="#session['task']">
		<s:url action="UserTask" var="taskUrl">
			<s:param name="id" value="#session['task'].id"/>
		</s:url>
		<b>Task:</b> <a href="<s:property value='#taskUrl'/>">
		<s:property value="#session['task'].name"/></a><br/>
		<b>Status:</b> <s:property value="#session['task'].status"/>
	</s:if>
</div>