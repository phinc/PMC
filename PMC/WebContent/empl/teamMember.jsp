<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Team Member</title>
<link href="css/project.css" rel="stylesheet" type="text/css" />
<link href="css/ui-lightness/jquery-ui-1.8.20.custom.css" rel="stylesheet" type="text/css" />
<link href="css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker-ru.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/tabs.js"></script>
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
				<s:include value="mainBlock.jsp"/>
				<s:include value="timeJournalBlock.jsp"/>
				<s:include value="projectBlock.jsp"/>
			</div>
			<div class="rightPanel">					
				<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
					<s:hidden name="currentProjectId" value="%{#session['project'].id}"/>
					<h3>Team member</h3>
					<s:if test="#security.projectManager || #security.admin">
						<s:action name="TeamCRUD_edit" namespace="/crud" executeResult="true"/>
						<button id="editTeamMember" type="button">Save</button>
					</s:if>
					<s:elseif test="#security.projectMember">
						<s:action name="TeamCRUD_show" namespace="/crud" executeResult="true"/>
					</s:elseif>
				</div>
				<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
					<h4>Employee info</h4>
					<s:if test="#security.admin">
						<s:action name="EmployeeCRUD_edit" namespace="/crud" ignoreContextParams="true" executeResult="true">
							<s:param name="id" value="#session['team member'].employee.id"/>
						</s:action>
						<button id="editEmployee" type="button">Save</button>
					</s:if>
					<s:elseif test="#security.projectManager || #security.projectMember">
						<s:action name="EmployeeCRUD_show" namespace="/crud" ignoreContextParams="true" executeResult="true">
							<s:param name="id" value="#session['team member'].employee.id"/>
						</s:action>
					</s:elseif>
				</div>
			</div>
		</div>
		<div class="footer">
			<a>phinc</a>
		</div>
	</div>
	<s:if test="#security.admin">
		<s:url action="EmployeeCRUD_edit" namespace="/crud" var="employeeUrl"/>
	</s:if>
	<s:elseif test="#security.projectManager || #security.projectMember">
		<s:url action="EmployeeCRUD_show" namespace="/crud" var="employeeUrl"/>
	</s:elseif>
	<script type="text/javascript">
	
	$(document).ready(function() {
		
		$("#editTeamMember").click(function(){
			$("#teamMemberDialog form").ajaxSubmit({
				target: "#teamMemberDialog",
			}); 
		});
		$("#editEmployee").click(function(){
			$("#employeeTabId form").ajaxSubmit({
				target: "#employeeTabId"
			});
		});
		
		$("#teamMemberDialog select").change(function(){
			$.ajax({
    			url: "<s:property value='#employeeUrl'/>" + "?id="+ $("select option:selected").val(),  
    			success: function(data){$("#employeeTabId").empty(); 
    									$("#employeeTabId").append(data);} 
    	   	});
		});
	});
	</script>
</body>
</html>