var dialog;

$(document).ready(function() {		
	dialog = initDialog();
	new CustomTabs("userAssignmentTabs", onUserAssignmentTabLoad);
	
	//button handler
	$("#editAssignment").click(function(){
		ajaxSaveForm("#assignmentDialog form", "#assignmentDialog", "#assignmentDialogMessage");
	});
	
	$("#openActivity").on("click", onOpenActivityClick);
	$("#addActivity").on("click", onAddActivityClick);
	$("#deleteActivity").on("click", onDeleteActivityClick);
});

function getProjectId() {
	return $("input[name='currentProjectId']").val();
}

function getTaskId() {
	return $("input[name='currentTaskId']").val();
}

function getAssignmentId() {
	return $("input[name='currentAssignmentId']").val();
}

function onUserAssignmentTabLoad(event, ui) {
	//ui.index - current selected tab
	if (ui.index == 0) {
		showTable(userAssignmentTables[ui.index]);
	}
}

//option for displaying tables in the tabs
var userAssignmentTables = new Array();
userAssignmentTables.push(	{
		dataUrl: function(){return "json/UserActivityJSON.action?id=" + getAssignmentId();},
		tableId: "activityTable",
		pagerId: "activityTablePager",
		colNames : ["Id", "Description", "Start date", "Reporter"],
		colModel : [
		            { name: "id",			index: "id", 			hidden: true },
		            { name: "description",	index: "description",	width: 250, align: "left" },
		            { name: "startDate",	index: "startDate",		width: 150, align: "left", formatter:'date' },
		            { name: "reporter.name",index: "reporter.name",	width: 250, align: "left"}],
		jsonReader : {
        	root: "activities",
        	page: "page",
            total: "total",
            records: "records",
            id: "id",
        	repeatitems: false
        },
		sortName : "startDate",
		ondblClickRow: onOpenActivityClick
	});

//ACTIVITY BUTTONS HANDLERS

function onOpenActivityClick() {
	var table = $("#activityTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#activityDialog form").ajaxSubmit({
								target: "#activityDialog",
								success: function() {table.trigger("reloadGrid");}
								}); },
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Edit" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/ActivityCRUD_edit.action?id=" + ret.id, 
				success: function(result){$("#contentD").empty(); 
										  $("#contentD").append(result);},
				error: function(jqXHR, textStatus, errorThrown){
		    			try {
							var jsonObj = jQuery.parseJSON(jqXHR.responseText);
							if (jsonObj.redirect) {
								top.location.href="/PMC/Logout.action"; //redirect to login page
							}
		    			}catch(e) {
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

function onAddActivityClick() {
	var table = $("#activityTable");
	dialog.dialog("option", "buttons", { 
		"OK":    function(evt) { $("#activityDialog form").ajaxSubmit({
									target: "#activityDialog",
									success: function() {
										table.trigger("reloadGrid");}
		}); },
		"Close": function() { dialog.dialog("close"); }
	});
	dialog.dialog( "option", "title", "Add" );
	loadContent = function fd() {
		$.ajax({
			url: "crud/ActivityCRUD_add.action?projectId=" + getProjectId(),  
			success: function(result){ $("#contentD").empty(); 
									   $("#contentD").append(result);
									   $("#activityDialog input[name='assignment.id']").val(getAssignmentId());},
		    error: function(jqXHR, textStatus, errorThrown){
    			try {
					var jsonObj = jQuery.parseJSON(jqXHR.responseText);
					if (jsonObj.redirect) {
						top.location.href="/PMC/Logout.action"; //redirect to login page
					}
    			}catch(e) {
    				$("#contentD").empty(); 
    				$("#contentD").append("Error: " + errorThrown);
    			}
    		}
		});
	};		
	dialog.dialog("open");
}

function onDeleteActivityClick() {
	var table = $("#activityTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#activityDialog form").ajaxSubmit({
						target: "#activityDialog",
						success: function() { dialog.dialog("close"); 
											  table.trigger("reloadGrid");}
						}); 
					},
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Delete" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/ActivityCRUD_destroy.action?id=" + ret.id, 
				success: function(result){$("#contentD").empty(); 
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
