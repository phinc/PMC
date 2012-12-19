<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	<s:url var="enUrl">
		<s:param name="request_locale" value="%{'en'}"/>
	</s:url>
	<s:url var="ruUrl">
		<s:param name="request_locale" value="%{'ru'}"/>
	</s:url>
	<s:a href="%{enUrl}">English</s:a>
	<s:a href="%{ruUrl}">Русский</s:a>
</div>