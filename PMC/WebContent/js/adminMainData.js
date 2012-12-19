var dialog;
$(document).ready(function() {
	dialog = initDialog();
	showTable(projectAdminTable);
	showTable(employeeAdminTable);
	//project table buttons
	$("#openProject").on("click", onOpenProjectClick);
	$("#addProject").on("click", onAddProjectClick);
	$("#deleteProject").on("click", onDeleteProjectClick);
	//employee table buttons
	$("#openEmployee").on("click", onOpenEmployeeClick);
	$("#addEmployee").on("click", onAddEmployeeClick);
	$("#deleteEmployee").on("click", onDeleteEmployeeClick);
	
});


function onTabLoad(event, ui) {
	//ui.index - current selected tab
	showTabTable(tabs.tabSet[ui.index].tableOpt);
}

var projectAdminTable = {
		dataUrl : function(){ return "json/ProjectsJSON.action"; },
		tableId : "projectTable",
		pagerId : "projectTablePager",
		colNames : ["Id", "Name", "Status", "Planned start date"],
		colModel : [
		            { name: "id",			index: "id", 			editable:false,	hidden: true },
		            { name: "name",			index: "name",	 		editable:false, width: 250, align: "left" },
		            { name: "status",		index: "status",		editable:false, width: 150, align: "left" },
		            { name: "planStart",	index: "planStart",		editable:false, width: 200, align: "left", formatter:'date' }],
		jsonReader : {
        	root: "projects",        	
        	page: "page",
            total: "total",
            records: "records",
            id: "id",
        	repeatitems: false
        },
		sortName : "name",
		ondblClickRow: onOpenProjectClick	
};

var employeeAdminTable = {
		dataUrl : function(){ return "json/EmployeesJSON.action"; },
		tableId : "employeeTable",
		pagerId : "employeeTablePager",
		colNames : ["Id", "First Name", "Last Name", "Post"],
		colModel : [
		            { name: "id",			index: "id", 			hidden: true },
		            { name: "firstName",	index: "firstName",	 	width: 250, align: "left" },
		            { name: "lastName",		index: "lastName",		width: 150, align: "left" },
		            { name: "post",	        index: "post",		    width: 200, align: "left" }],
		jsonReader : {
        	root: "employees",
        	page: "page",
            total: "total",
            records: "records",
            id: "id",
        	repeatitems: false
        },
		sortName : "lastName",
		ondblClickRow: onOpenEmployeeClick
};

//PROJECT BUTTONS HANDLERS

function onOpenProjectClick() {
	var table = $("#projectTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) {
		var ret = table.jqGrid('getRowData',selRow);
		window.location = "UserProject.action?id=" + ret.id;
	} else {
		alert("Please select row");
	}
}

function onAddProjectClick() {
	var table = $("#projectTable");
	dialog.dialog("option", "buttons", { 
		"OK":    function(evt) { $("#projectTabId form").ajaxSubmit({
									target: "#projectTabId",
									success: function() {table.trigger("reloadGrid");}}); },
		"Close": function() { dialog.dialog("close"); }
	});
	dialog.dialog( "option", "title", "Add" );
	loadContent = function fd() {
		$.ajax({
			url: "crud/ProjectCRUD_add.action",  
			success: function(result){ $("#contentD").empty(); $("#contentD").append(result);},
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
}

function onDeleteProjectClick() {
	var table = $("#projectTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#projectTabId form").ajaxSubmit({
						target: "#projectTabId",
						success: function() { dialog.dialog("close"); 
											  table.trigger("reloadGrid");}
						}); 
					},
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Delete" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/ProjectCRUD_destroy.action?id=" + ret.id, 
				success: function(result){
					$("#contentD").empty(); $("#contentD").append(result);
				},
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

//EMPLOYEE BUTTONS HANDLERS

function onOpenEmployeeClick() {
	var table = $("#employeeTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#employeeTabId form").ajaxSubmit({
				target: "#employeeTabId",
				success: function() {table.trigger("reloadGrid");}
				}); },
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Edit" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/EmployeeCRUD_edit.action?id=" + ret.id, 
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

function onAddEmployeeClick() {
	var table = $("#employeeTable");
	dialog.dialog("option", "buttons", { 
		"OK":    function(evt) { $("#employeeTabId form").ajaxSubmit({
									target: "#employeeTabId",
									success: function() {table.trigger("reloadGrid");}}); },
		"Close": function() { dialog.dialog("close"); }
	});
	dialog.dialog( "option", "title", "Add" );
	loadContent = function fd() {
		$.ajax({
			url: "crud/EmployeeCRUD_add.action",  
			success: function(result){ $("#contentD").empty(); $("#contentD").append(result);},
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
}

function onDeleteEmployeeClick() {
	var table = $("#employeeTable");
	var selRow = table.jqGrid('getGridParam','selrow');
	if (selRow) { 
		var ret = table.jqGrid('getRowData',selRow);
		dialog.dialog("option", "buttons", { 
			"OK":    function() { $("#employeeTabId form").ajaxSubmit({
						target: "#employeeTabId",
						success: function() { dialog.dialog("close"); 
											  table.trigger("reloadGrid");}
						}); 
					},
			"Close": function() { dialog.dialog("close"); }
		});
		dialog.dialog( "option", "title", "Delete" );
		loadContent = function fd() {
	    	$.ajax({
				url: "crud/EmployeeCRUD_destroy.action?id=" + ret.id, 
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