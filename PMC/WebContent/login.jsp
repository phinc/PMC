<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login page</title>
</head>
<body>
	<s:actionerror/>
	<s:form action="Login" namespace="/">
		<s:textfield key="login"
					 cssClass="text ui-widget-content ui-corner-all" />
		<s:password key="password"
					cssClass="text ui-widget-content ui-corner-all"/>
		<s:submit label="Login"/>
	</s:form>
</body>
</html>