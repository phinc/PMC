
$(document).ready(function() {
	showTable(projectTable);
	showTable(assignmentTable);
	
	//project table buttons
	$("#openSelectedProject").on("click", onSelectedProjectClick);
	
	//assignment table buttons
	$("#openSelectedAssignment").on("click", onSelectedAssignmentClick);
	
});


//function onTabLoad(event, ui) {
//	//ui.index - current selected tab
//	showTabTable(tabs.tabSet[ui.index].tableOpt);
//}

var projectTable = {
		dataUrl : function(){ return "json/UserProjectsJSON.action"; },
		tableId : "projectTable",
		pagerId : "projectTablePager",
		colNames : ["Id", "Name", "Status", "Planned start date"],
		colModel : [
		            { name: "id",			index: "id", 			hidden: true },
		            { name: "name",			index: "name",	 		width: 250, align: "left" },
		            { name: "status",		index: "status",		width: 150, align: "left" },
		            { name: "planStart",	index: "planStart",		width: 200, align: "left", formatter:'date' }],
		jsonReader : {
        	root: "projects",
        	page: "page",
            total: "total",
            records: "records",
            id: "id",
        	repeatitems: false
        },
		sortName : "planStart",
		ondblClickRow: onSelectedProjectClick
};

var assignmentTable = {
		dataUrl : function(){ return "json/UserAssignmentsJSON.action"; },
		tableId : "assignmentTable",
		pagerId : "assignmentTablePager",
		colNames : ["Id", "Project Id", "Project", "Name", "Status", "Planned start date"],
		colModel : [
		            { name: "id",			    index: "id", 			hidden: true },
		            { name: "task.project.id",	index: "projectId",		hidden: true },
		            { name: "task.project.name",index: "projectName",	width: 250, align: "left" },
		            { name: "name",				index: "name",	 		width: 250, align: "left" },
		            { name: "status",			index: "status",		width: 150, align: "left" },
		            { name: "planStart",		index: "planStart",		width: 200, align: "left", formatter:'date' }],
		jsonReader : {
        	root: "assignments",
        	page: "page",
            total: "total",
            records: "records",
            id: "id",
        	repeatitems: false
        },
		sortName : "planStart",
		ondblClickRow: onSelectedAssignmentClick
};


function onSelectedProjectClick() {
	var table = $("#projectTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) {
		var ret = table.jqGrid('getRowData',selRow);
		window.location = "UserProject.action?id=" + ret.id;
	} else {
		alert("Please select row");
	}
}


function onSelectedAssignmentClick() {
	var table = $("#assignmentTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) {
		var ret = table.jqGrid('getRowData',selRow);
		window.location = "UserAssignment.action?id=" + ret.id + "&projectId=" + ret['task.project.id'];
	} else {
		alert("Please select row");
	}
}