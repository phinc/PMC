<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Assignment</title>
<link href="css/project.css" rel="stylesheet" type="text/css" />
<link href="css/ui-lightness/jquery-ui-1.8.20.custom.css" rel="stylesheet" type="text/css" />
<link href="css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker-ru.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/tabs.js"></script>
<script type="text/javascript" src="js/assignmentData.js"></script>
<script type="text/javascript" src="js/table.js"></script>
<script type="text/javascript" src="js/dialog.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="js/grid.locale-en.js"></script>
</head>
<body>
	<div class="container">
		<div class="header"></div>
		<div class="content">
			<div class="leftPanel">
				<s:include value="loginBlock.jsp"/>
				<s:include value="timeJournalBlock.jsp"/>
				<s:include value="mainBlock.jsp"/>
				<s:include value="projectBlock.jsp"/>
				<s:include value="taskBlock.jsp"/>
			</div>
			<div class="rightPanel">					
				<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
					<s:hidden name="currentProjectId" value="%{#session['project'].id}"/>
					<s:hidden name="currentTaskId" value="%{#session['task'].id}"/>
					<s:hidden name="currentAssignmentId" value="%{#session['assignment'].id}"/>
					<h3>Assignment</h3>
					<s:if test="#security.projectManager || #security.admin">
						<s:action name="AssignmentCRUD_edit" namespace="/crud" executeResult="true"/>
						<button id="editAssignment" type="button">Save</button>
					</s:if>
					<s:elseif test="#security.projectMember">
						<s:action name="AssignmentCRUD_show" namespace="/crud" executeResult="true"/>
					</s:elseif>
				</div>
				<div id="userAssignmentTabs">
					<div>
						<ul class="ulTabs">
							<li><a href="#ActivitiesTabDiv">Activity</a></li>
						</ul>
						<div id="ActivitiesTabDiv">
							<table id="activityTable" class="scroll"></table>
							<div id="activityTablePager" ></div>
							<button id="openActivity" type="button">Open</button>
							<button id="addActivity" type="button">Add</button>
							<s:if test="#security.projectManager || #security.admin">
								<button id="deleteActivity" type="button">Delete</button>	
							</s:if>				
							<div id="errorActivityTable" class="ui-state-error-text"></div>
						</div>
					</div>						
				</div>
			</div>
		</div>
		<div class="footer">
			<a>phinc</a>
		</div>
	</div>
</body>
</html>