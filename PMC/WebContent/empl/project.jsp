<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Project</title>
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
<script type="text/javascript" src="js/projectData.js"></script>
<script type="text/javascript" src="js/table.js"></script>
<script type="text/javascript" src="js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="js/grid.locale-en.js"></script>
</head>
<body>
	<s:bean name="by.phinc.pmc.util.SecurityBean" var="security">
		<s:param name="employee" value="#session['employee']"/>
		<s:param name="project" value="#session['project']"/>
	</s:bean>
	<div class="container">
			<div class="header"></div>
			<div class="content">
				<div class="leftPanel">
					<s:include value="loginBlock.jsp"/>
					<s:include value="timeJournalBlock.jsp"/>
					<s:include value="mainBlock.jsp"/>
				</div>
				<div class="rightPanel">
					<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
						<h3><s:text name="project"/></h3>
						<%-- 
						<s:if test="#security.projectManager || #security.admin">
						 --%>
							<s:action name="ProjectCRUD_edit" namespace="/crud" executeResult="true"/>
							<button id="saveProject" type="button">Save</button>
						<%--  
						</s:if>
						<s:elseif test="#security.projectMember">
							<s:action name="ProjectCRUD_show" namespace="/crud" executeResult="true"/>
						</s:elseif>
						--%>
					</div>
					<div id="userProjectTabs">
						<div>
							<ul class="ulTabs">
								<li><a href="#TaskTabDiv">Tasks</a></li>
								<li><a href="#TeamTabDiv">Team</a></li>
								<li><a href="#DocsTabDiv">Documents</a></li>
							</ul>
							<div id="TaskTabDiv">
								<table id="taskTable" class="scroll"></table>
								<div id="taskTablePager" ></div>								
								<button id="openTask" type="button">Open</button>
								<s:if test="#security.projectManager || #security.admin">
									<button id="addTask" type="button">Add</button>
									<button id="deleteTask" type="button">Delete</button>
								</s:if>
								<div id="errorTaskTable" class="ui-state-error-text"></div>
							</div>
							<div id="TeamTabDiv">
								<table id="teamTable" class="scroll"></table>
								<div id="teamTablePager" ></div>								
								<button id="openTeamMember" type="button">Open</button>
								<s:if test="#security.projectManager || #security.admin">
									<button id="addTeamMember" type="button">Add</button>
									<button id="deleteTeamMember" type="button">Delete</button>
								</s:if>						
								<div id="errorTeamTable" class="ui-state-error-text"></div>
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