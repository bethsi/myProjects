function doSomething() {
	var node;
	var showMessage = document.getElementById('erroMessage');
	document.write("<h1>This is a heading</h1>");
	alert("Hello from javascript!");
	if (showMessage == true) {
		document.getElementById('error-message').value = "*Please input valid username and password!";
		node = document.getElementById('error-message');
		node.style.visibility = 'visible';
	} else {
		document.getElementById('error-message').value = '';
		node = document.getElementById('error-message');
		node.style.visibility = 'hidden';
	}
}