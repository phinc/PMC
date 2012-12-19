<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document</title>
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
				<s:include value="timeJournalBlock.jsp"/>
				<s:include value="mainBlock.jsp"/>
				<s:include value="projectBlock.jsp"/>
			</div>
			<div class="rightPanel">					
				<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
					<s:hidden name="currentProjectId" value="%{#session['project'].id}"/>
					<h3>Document</h3>
					<s:if test="#security.projectManager || #security.admin || #security.projectMember">
						<s:action name="ProjectDocumentCRUD_add" namespace="/crud" executeResult="true"/>
					</s:if>
				</div>					
			</div>
		</div>
		<div class="footer">
			<a>phinc</a>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {	
			//add new item button handler
			$("#editDocument").click(function(){
				$("#documentDialog form").ajaxSubmit({
					target: "#documentDialog"}); 
			});
		});
	</script>
</body>
</html>