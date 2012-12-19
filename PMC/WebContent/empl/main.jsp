<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="main.page"/></title>
<link href="css/project.css" rel="stylesheet" type="text/css" />
<link href="css/ui-lightness/jquery-ui-1.8.20.custom.css" rel="stylesheet" type="text/css" />
<link href="css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker-ru.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<script type="text/javascript" src="js/table.js"></script>
<script type="text/javascript" src="js/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="js/grid.locale-en.js"></script>
</head>
<body>
	<div class="container">
		<div class="header">
			<s:property value="locale"/>
		</div>
		<div class="content">
			<div class="leftPanel">
				<s:include value="loginBlock.jsp"/>
				<s:include value="timeJournalBlock.jsp"/>
			</div>
			<div class="rightPanel">
				<s:if test="exception!=null" >
					<div class="error">
						<s:property value="exception"/>
					</div>
				</s:if>
				<s:actionerror/>
				<div>
					<h3>Projects</h3>
					<table id="projectTable" class="scroll"></table>
					<div id="projectTablePager" ></div>						
					<button id="openSelectedProject" type="button">Open</button>
				</div>
				<div>
					<h3>Assignments</h3>
					<table id="assignmentTable" class="scroll"></table>
					<div id="assignmentTablePager" ></div>						
					<button id="openSelectedAssignment" type="button">Open</button>
				</div>
			</div>
		</div>
		<div class="footer">
			<a>phinc</a>
		</div>
	</div>	
</body>
</html>