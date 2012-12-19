var projectTabs;
var dialog;

$(document).ready(function() {	
	dialog = initDialog();
	projectTabs = new CustomTabs("userProjectTabs", onUserProjectTabShow);
	
	$("#saveProject").on("click", onSaveProjectClick);
	
	$("#openTask").on("click", onOpenTaskClick);
	$("#addTask").on("click", onAddTaskClick);
	$("#deleteTask").on("click", onDeleteTaskClick);
	
	$("#openTeamMember").on("click", onOpenTeamMemberClick);
	$("#addTeamMember").on("click", onAddTeamMemberClick);
	$("#deleteTeamMember").on("click", onDeleteTeamMemberClick);
	
	$("#addDocument").on("click", onAddDocumentClick);
	$("#deleteDocument").on("click", onDeleteDocumentClick);
});

function onUserProjectTabShow(event, ui) {
	//ui.index - current selected tab
	if (ui.index < 3) {
		showTable(userProjectTables[ui.index]);
	}
}

function getProjectId() {
	return $("#projectTabId input[name='id']").val();
}


//option for displaying tables in the tabs
var userProjectTables = new Array();
userProjectTables.push( {
	dataUrl : function(){return "json/ProjectTasksJSON.action?id=" + getProjectId();},
	tableId : "taskTable",
	pagerId : "taskTablePager",
	colNames : ["Id", "Project Id", "Name", "Status", "Planned start date"],
	colModel : [
	            { name: "id",			index: "id", 		hidden: true },
	            { name: "projectId",	index: "projectId",	hidden: true },
	            { name: "name",			index: "name",	 	width: 250, align: "left" },
	            { name: "status",		index: "status",	width: 150, align: "left" },
	            { name: "planStart",	index: "planStart",	width: 200, align: "left", formatter:'date' }],
	jsonReader : {
    	root: "tasks",
    	page: "page",
        total: "total",
        records: "records",
        id: "id",
    	repeatitems: false
    },
	sortName : "planStart",
	ondblClickRow: onOpenTaskClick
});
userProjectTables.push(	{
		dataUrl: function(){return "json/ProjectTeamJSON.action?id=" + getProjectId();},
		tableId: "teamTable",
		pagerId: "teamTablePager",
		colNames: ["Id", "Project Id", "First Name", "Last Name", "Post", "Role"],
		colModel: [
		     { name: "id",			index: "id", 		hidden: true},
		     { name: "projectId",	index: "projectId",	hidden: true },
		     { name: "firstName",	index: "firstName",	width: 120, align: "left" },
		     { name: "lastName",	index: "lastName",	width: 150, align: "left" },
		     { name: "post",		index: "post",		width: 200, align: "left" },
		     { name: "role",		index: "role",		width: 200, align: "left"}],
		jsonReader : {
		     root: "team",
		     page: "page",
		     total: "total",
		     records: "records",
		     id: "id",
		     repeatitems: false },
		sortname: "lastName",
		ondblClickRow: onOpenTeamMemberClick
	});
userProjectTables.push(	{
	dataUrl: function(){return "json/ProjectDocumentsJSON.action?id=" + getProjectId();},
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

//PROJECT SAVE BUTTON HANDLER

function onSaveProjectClick() {
	ajaxSaveForm("#projectTabId form", "#projectTabId", "#projectTabMessage");
}


//TASK TAB BUTTONS HANDLERS

function onOpenTaskClick() {
	var table = $("#taskTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) {
		var ret = table.jqGrid('getRowData',selRow);
		window.location = "UserTask.action?id=" + ret.id;
	} else {
		alert("Please select row");
	}
}

function onAddTaskClick() {
	var table = $("#taskTable");
	dialog.dialog("option", "buttons", { 
		"OK":    function(evt) { $("#taskDialog form").ajaxSubmit({
									target: "#taskDialog",
									success: function() {table.trigger("reloadGrid");}}); },
		"Close": function() { dialog.dialog("close"); }
	});
	dialog.dialog( "option", "title", "Add" );
	loadContent = function fd() {
		$.ajax({
			url: "crud/TaskCRUD_add.action",  
			success: function(result){ $("#contentD").empty(); $("#contentD").append(result);
									   $("#taskDialog input[name='project.id']").val(getProjectId());},
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

function onDeleteTaskClick() {
	var table = $("#taskTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#taskDialog form").ajaxSubmit({
						target: "#taskDialog",
						success: function() { dialog.dialog("close"); 
											  table.trigger("reloadGrid");}
						}); 
					},
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Delete" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/TaskCRUD_destroy.action?id=" + ret.id, 
				success: function(result){$("#contentD").empty(); $("#contentD").append(result);},
				error: function(jqXHR, textStatus, errorThrown){
	    			try {
						var jsonObj = jQuery.parseJSON(jqXHR.responseText);
						if (jsonObj.redirect) {
							top.location.href="/PMC/Logout.action"; //redirect to login page
						}
	    			}
	    			catch(e) {
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

//TEAM TAB BUTTONS HANDLERS

function onOpenTeamMemberClick() {
	var table = $("#teamTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) {
		var ret = table.jqGrid('getRowData',selRow);
		window.location = "UserTeam.action?id=" + ret.id;
	} else {
		alert("Please select row");
	}
}

function onAddTeamMemberClick() {
	var table = $("#teamTable");
	dialog.dialog("option", "buttons", { 
		"OK":    function(evt) { $("#teamMemberDialog form").ajaxSubmit({
									target: "#teamMemberDialog",
									success: function() {table.trigger("reloadGrid");}}); },
		"Close": function() { dialog.dialog("close"); }
	});
	dialog.dialog( "option", "title", "Add" );
	loadContent = function fd() {
		$.ajax({
			url: "crud/TeamCRUD_add.action",  
			success: function(result){ 
				$("#contentD").empty(); $("#contentD").append(result);
				$("#teamMemberDialog input[name='project.id']").val(getProjectId());},
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

function onDeleteTeamMemberClick() {
	var table = $("#teamTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#teamMemberDialog form").ajaxSubmit({
						target: "#teamMemberDialog",
						success: function() { dialog.dialog("close"); 
											  table.trigger("reloadGrid");}
						}); 
					},
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Delete" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/TeamCRUD_destroy.action?id=" + ret.id, 
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

//DOCUMENT TAB BUTTONS HANDLERS

function onAddDocumentClick() {
	window.location = "UserProjectDocument.action";
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
				url: "crud/ProjectDocumentCRUD_destroy.action?id=" + ret.id, 
				success: function(result){
					$("#contentD").empty(); 
					$("#contentD").append(result);},
				error: function(jqXHR, textStatus, errorThrown){
	    			try {
						var jsonObj = jQuery.parseJSON(jqXHR.responseText);
						if (jsonObj.redirect) {
							top.location.href="/PMC/Logout.action"; //redirect to login page
						}
	    			}
	    			catch(e) {
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
