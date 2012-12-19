//perform ajax form submit
//it is expected that return value will be filled form or json in case of error
function ajaxSaveForm(formId, targetId, messageId) {
	$(formId).ajaxSubmit({
		target: targetId,
		error: function(xhr,status,error) {
			var jsonObj = $.parseJSON(xhr.responseText);
			if (jsonObj.redirect) {
				top.location.href="/PMC/Logout.action"; //redirect to login page
			} else {
				$(messageId).replaceWith(
					"<div class='error'>Error message: " + status + "</div>");
			}
		}
	});
}