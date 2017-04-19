function askConfirm(msg, url, confirmLabel, cancelLabel) {
	bootbox.confirm({
		message : '<div class="text-center">' + msg + '</div>',
		buttons : {
			confirm : {
				label : confirmLabel,
				className : 'btn btn-danger'
			},
			cancel : {
				label : cancelLabel,
				className : 'btn btn-default'
			}
		},
		callback : function(result) {
			if (result != null && result) {
				window.location.replace(url);
			}
		}
	});
}

function relativeRedir(loc) {
	var b = document.getElementsByTagName('base');
	if (b && b[0] && b[0].href) {
		if (b[0].href.substr(b[0].href.length - 1) == '/' && loc.charAt(0) == '/')
			loc = loc.substr(1);
		loc = b[0].href + loc;
	}
	window.location.replace(loc);
}
