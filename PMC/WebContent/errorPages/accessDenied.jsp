<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	Access denies<br/>
	<s:if test="exception!=null" >
		<s:property value="exception"/><br/>
		<s:property value="exceptionStack"/>
	</s:if>
	<s:actionerror/>
</div>