var loadContent = null;

function initDialog(){
	var dial = $("<div><div id='contentD'></div></div>");
	dial.dialog({
		autoOpen: false,
		width: "auto",
		close: function(event, ui) {
			$("#contentD").empty();
			var buttons = $(this).dialog( "option", "buttons" );
			for (var b in buttons) {
				$(b).attr('disabled', false);
			}
	    },
	    open: function() {if (loadContent) {loadContent();}}
	});
	return dial;
}