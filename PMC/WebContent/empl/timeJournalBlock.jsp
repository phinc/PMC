<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	<s:url action="TimeJournal" var="timeJournalUrl">
		<s:param name="employee.id" value="%{#session['employee'].id}"/>
	</s:url>
	<a href="<s:property value='#timeJournalUrl'/>"><s:text name="time.journal"/></a>
</div>