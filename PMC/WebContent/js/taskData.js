var dialog;

$(document).ready(function() {	
	dialog = initDialog();
	new CustomTabs("userTaskTabs", onUserTaskTabLoad);
	
	//save task button handler
	$("#editTask").click(function(){
		$("#taskDialog input[name='project.id']").val(getProjectId());
		ajaxSaveForm("#taskDialog form", "#taskDialog", "#taskDialogMessage");
	});
	
	$("#openAssignment").on("click", onOpenAssignmentClick);
	$("#addAssignment").on("click", onAddAssignmentClick);
	$("#deleteAssignment").on("click", onDeleteAssignmentClick);
	
	$("#addDocument").on("click", onAddDocumentClick);
	$("#deleteDocument").on("click", onDeleteDocumentClick);
});

function onUserTaskTabLoad(event, ui) {
	//ui.index - current selected tab
	if (ui.index < 2) {
		showTable(userTaskTables[ui.index]);
	}
}

function getProjectId() {
	return $("input[name='currentProjectId']").val();
}

function getTaskId() {
	return $("#taskDialog input[name='id']").val();
}


//option for displaying tables in the tabs
var userTaskTables = new Array();
userTaskTables.push(	{
		dataUrl: function(){return "json/TaskAssignmentsJSON.action?id=" + getTaskId();},
		tableId: "assignmentTable",
		pagerId: "assignmentTablePager",
		colNames : ["Id", "Name", "Status", "Planned start date", "Employee", "Role"],
		colModel : [
		            { name: "id",			index: "id", 			hidden: true },
		            { name: "name",			index: "name",	 		width: 200, align: "left" },
		            { name: "status",		index: "status",		width: 110, align: "left" },
		            { name: "planStart",	index: "planStart",		width: 180, align: "left", formatter:'date' },
		            { name: "member.employee.name",	index: "member.employee.name",	width: 160, align: "left" },
		            { name: "member.role",	index: "member.role",	width: 150, align: "left" }],
		jsonReader : {
        	root: "assignments",
        	page: "page",
            total: "total",
            records: "records",
            id: "id",
        	repeatitems: false
        },
		sortName : "planStart",
		ondblClickRow: onOpenAssignmentClick
	});
userTaskTables.push(	{
	dataUrl: function(){return "json/TaskDocumentsJSON.action?id=" + getTaskId();},
	tableId: "documentTable",
	pagerId: "documentTablePager",
	colNames: ["Id", "Name", "Description", "Download"],
	colModel: [
	     { name: "id",			index: "id", 			hidden: true},
	     { name: "name",	    index: "name",			width: 150, align: "left" },
	     { name: "description",	index: "description",	width: 550, align: "left" },
	     { name: "link",		index: "link",			width: 150, align: "left", formatter: 'link' }],
	jsonReader : {
	     root: function(obj){
	    	 if (obj.documents != null) {
		    	 for(var i=0; i < obj.documents.length; i++) {                
		    		 obj.documents[i].link = "<a href='DocumentDownload.action?id=" + 
		    		 						obj.documents[i].id + "'>download</a>";
		    	 }
	    	 }
	    	 return obj.documents;},
	     page: "page",
	     total: "total",
	     records: "records",
	     id: "id",
	     repeatitems: false },
	sortname: "name",
	ondblClickRow: function(){}
});

//ASSIGNMENT BUTTONS HANDLERS

function onOpenAssignmentClick() {
	var table = $("#assignmentTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) {
		var ret = table.jqGrid('getRowData',selRow);
		window.location = "UserAssignment.action?id=" + ret.id + "&projectId=" + getProjectId();
	} else {
		alert("Please select row");
	}
}

function onAddAssignmentClick() {
	var table = $("#assignmentTable");
	dialog.dialog("option", "buttons", { 
		"OK":    function(evt) { $("#assignmentDialog form").ajaxSubmit({
									target: "#assignmentDialog",
									success: function() {table.trigger("reloadGrid");}}); },
		"Close": function() { dialog.dialog("close"); }
	});
	dialog.dialog( "option", "title", "Add" );
	loadContent = function fd() {
		$.ajax({
			url: "crud/AssignmentCRUD_add.action?projectId=" + getProjectId(),  
			success: function(result){ 
				$("#contentD").empty(); $("#contentD").append(result);
				$("#assignmentDialog input[name='projectId']").val(getProjectId());
				$("#assignmentDialog input[name='task.id']").val(getTaskId());},
			error: function(jqXHR, textStatus, errorThrown){
    			try {
					var jsonObj = jQuery.parseJSON(jqXHR.responseText);
					if (jsonObj.redirect) {
						top.location.href="/PMC/Logout.action"; //redirect to login page
					}
    			} catch(e) {
    				$("#contentD").empty(); 
    				$("#contentD").append("Error: " + errorThrown);
    			}
    		}
		});
	};		
	dialog.dialog("open");
}

function onDeleteAssignmentClick() {
	var table = $("#assignmentTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#assignmentDialog form").ajaxSubmit({
						target: "#assignmentDialog",
						success: function() { dialog.dialog("close"); 
											  table.trigger("reloadGrid");}
						}); 
					},
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Delete" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/AssignmentCRUD_destroy.action?id=" + ret.id + "&projectId=" + getProjectId(), 
				success: function(result){$("#contentD").empty(); $("#contentD").append(result);},
				error: function(jqXHR, textStatus, errorThrown){
	    			try {
						var jsonObj = jQuery.parseJSON(jqXHR.responseText);
						if (jsonObj.redirect) {
							top.location.href="/PMC/Logout.action"; //redirect to login page
						}
	    			} catch(e) {
	    				$("#contentD").empty(); 
	    				$("#contentD").append("Error: " + errorThrown);
	    			}
	    		}
	    	});
	    };		
	    dialog.dialog("open");			
	} else { 
		alert("Please select row");
	}
}

//DOCUMENT TAB BUTTONS HANDLERS

function onAddDocumentClick() {
	window.location = "UserTaskDocument.action";
}

function onDeleteDocumentClick() {
	var table = $("#documentTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#documentDialog form").ajaxSubmit({
						target: "#documentDialog",
						success: function() { dialog.dialog("close"); 
											  table.trigger("reloadGrid");}
						}); 
					},
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Delete" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/TaskDocumentCRUD_destroy.action?id=" + ret.id, 
				success: function(result){
					$("#contentD").empty(); 
					$("#contentD").append(result);},
				error: function(jqXHR, textStatus, errorThrown){
	    			try {
						var jsonObj = jQuery.parseJSON(jqXHR.responseText);
						if (jsonObj.redirect) {
							top.location.href="/PMC/Logout.action"; //redirect to login page
						}
	    			} catch(e) {
	    				$("#contentD").empty(); 
	    				$("#contentD").append("Error: " + errorThrown);
	    			}
	    		}
	    	});
	    };		
	    dialog.dialog("open");			
	} else { 
		alert("Please select row");
	}
}