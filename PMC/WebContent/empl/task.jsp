<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Task</title>
<link href="css/project.css" rel="stylesheet" type="text/css" />
<link href="css/ui-lightness/jquery-ui-1.8.20.custom.css" rel="stylesheet" type="text/css" />
<link href="css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker-ru.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/tabs.js"></script>
<script type="text/javascript" src="js/dialog.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/taskData.js"></script>
<script type="text/javascript" src="js/table.js"></script>
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
			</div>
			<div class="rightPanel">					
				<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
					<s:hidden name="currentProjectId" value="%{#session['project'].id}"/>
					<h3>Task</h3>
					<%-- <s:if test="#security.projectManager || #security.admin">--%>
						<s:action name="TaskCRUD_edit" namespace="/crud" executeResult="true"/>
						<button id="editTask" type="button">Save</button>
					<%-- </s:if>
					<s:elseif test="#security.projectMember">
						<s:action name="TaskCRUD_show" namespace="/crud" executeResult="true"/>
					</s:elseif>--%>
				</div>
				<div id="userTaskTabs">
					<div>
						<ul class="ulTabs">
							<li><a href="#AssignmentsTabDiv">Assignments</a></li>
							<li><a href="#DocsTabDiv">Documents</a></li>
						</ul>
						<div id="AssignmentsTabDiv">
							<table id="assignmentTable" class="scroll"></table>
							<div id="assignmentTablePager" ></div>
							<button id="openAssignment" type="button">Open</button>
							<s:if test="#security.projectManager || #security.admin">
								<button id="addAssignment" type="button">Add</button>
								<button id="deleteAssignment" type="button">Delete</button>	
							</s:if>				
							<div id="errorAssignmentTable" class="ui-state-error-text"></div>
						</div>
						<div id="DocsTabDiv">
							<table id="documentTable" class="scroll"></table>
							<div id="documentTablePager" ></div>
							<button id="addDocument" type="button">Add</button>
							<s:if test="#security.projectManager || #security.admin">
								<button id="deleteDocument" type="button">Delete</button>
							</s:if>
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