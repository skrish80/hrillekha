function validateDecimal(textBox, event) {
	// Allow only backspace and delete
	if (event.keyCode == 46) {
		if ($(textBox).val().indexOf(".") > 0) {
			event.preventDefault();
		}
	}
	if (event.keyCode == 46 || event.keyCode == 8) {
		// let it happen, don't do anything
	} else {
		// Ensure that it is a number and stop the keypress
		if (event.keyCode < 48 || event.keyCode > 57) {
			event.preventDefault();
		}
	}
}

function validateInteger(event) {
	// Allow only backspace and delete
	if (event.keyCode == 8) {
		// let it happen, don't do anything
	} else {
		// Ensure that it is a number and stop the keypress
		if (event.keyCode < 48 || event.keyCode > 57) {
			event.preventDefault();
		}
	}
}
function disableF5(e) {
	var keycode = e.which;
	if (window.event)
		keycode = window.event.keyCode;
	else if (e)
		keycode = e.which;

	// Mozilla firefox

	// IE
	if ($.browser.msie) {
		if (keycode == 116 || (window.event.ctrlKey && (keycode == 82))) {
			window.event.returnValue = false;
			window.event.keyCode = 0;
			window.status = "Refresh is disabled";
			alert("Refresh is disabled");
		}
	} else {
		if (keycode == 116 || (e.ctrlKey && (keycode == 82))) {
			if (e.preventDefault) {
				e.preventDefault();
				e.stopPropagation();
			}
			alert("Refresh is disabled");
		}
	}
};

function click() {
	if (event.button == 2) {
		alert("Right-Click is disabled");
	}
};
