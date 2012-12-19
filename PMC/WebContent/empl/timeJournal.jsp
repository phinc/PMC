<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Project list</title>
<link href="css/project.css" rel="stylesheet" type="text/css" />
<link href="css/ui-lightness/jquery-ui-1.8.20.custom.css" rel="stylesheet" type="text/css" />
<link href="css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker-ru.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/table.js"></script>
<script type="text/javascript" src="js/dialog.js"></script>
<script type="text/javascript" src="js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="js/grid.locale-en.js"></script>
</head>
<body>
	<div class="container">
		<div class="header">
			<s:form>
				<s:property value="locale"/>
				<s:radio name="request_locale" list="#application['locales']" value="locale"/>
				<s:submit/>
			</s:form>
		</div>
		<div class="content">
			<div class="leftPanel">
				<s:include value="loginBlock.jsp"/>
				<s:include value="mainBlock.jsp"/>
			</div>
			<div class="rightPanel">
				<s:if test="exception!=null" >
					<div class="error">
						<s:property value="exception"/>
					</div>
				</s:if>
				<div>
					<h3><s:text name="time.journal"/></h3>
					<s:form action="TimeJournal">
						<s:textfield key="date" cssClass="text ui-widget-content ui-corner-all"/>
						<s:if test="#security.admin">
							<s:select key="employee.id" list="employees" listKey="id"
							listValue="lastName + ' ' + firstName" 
							cssClass="text ui-widget-content ui-corner-all"/>
						</s:if>
						<s:else>
							<s:hidden key="employee.id"/>
						</s:else>
						<s:submit/>
					</s:form>
					<table>
						<thead>
							<tr>
								<th>Assignment Id</th>
								<th>Assignment</th>
								<s:iterator value="weekDays">
									<th><s:property value="shortDayName"/><br/>
									<s:property value="curDate"/></th>
								</s:iterator>
							</tr>
						</thead>
						<s:iterator value="result" status="state" var="el">
						
						<tbody>
						<tr>
							<td><s:property value="assignmentId"/></td>
							<td><s:property value="assignmentName"/></td>
							<s:iterator value="weekDays" status="status" var="weekDay">								
								<td><s:property value="#el.workingWeek[#weekDay.curDate]"/></td>								
							</s:iterator>
						</tr>
					</s:iterator>
					</tbody>
					</table>
					<table id="timeTable" class="scroll"></table>
					<div id="timeTablePager" ></div>
				</div>
			</div>
			<div class="footer">
				<a>phinc</a>
			</div>
		</div>
	</div>	
</body>
</html>