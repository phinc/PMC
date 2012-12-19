function showTable(opt) {	 
	if (opt == null) {
		throw new Error("opt is null");
	}
    var jqTable = $("#"+opt.tableId);
	// Set up the jquery grid
    jqTable.jqGrid({
        // Ajax related configurations
        url: opt.dataUrl(),
        datatype: "json",
        mtype: "POST",

        // Specify the column names
        colNames: opt.colNames,

        // Configure the columns
        colModel: opt.colModel,
        
        jsonReader : opt.jsonReader,

        // Grid total width and height
        width: "100%",
        height: 200,

        // Paging
        pager: $("#"+opt.pagerId),
        rowNum: 10,
        rowList: [10, 25, 50],
        viewrecords: true, // Specify if "total number of records" is displayed
        
        sortname: opt.sortName,
        sortorder: "asc",
        
        ondblClickRow: opt.ondblClickRow,
        
        loadError: function(xhr,status,error){
			var jsonObj = $.parseJSON(xhr.responseText);
			if (jsonObj.redirect) {
				top.location.href="/PMC/Logout.action"; //redirect to login page
			}
			jqTable.before("<div class='error'>Error message: " + 
				jsonObj.exception.message + "</div>");}
    });
    return jqTable;
}