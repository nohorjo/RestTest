var base = Java.type("nohorjo.resttest.engine.BaseScript");

function makeMultiPartRequest() {
	return base.makeMultiPartRequest(payload.url, payload.method.toUpperCase(),
			payload.headers || {}, payload.data, payload.timeout || 60000);
}

function makeRequest(payload) {
	var isFile = false;
	if (payload.file) {
		isFile = true;
	}
	return base.makeRequest(payload.url, payload.method.toUpperCase(),
			payload.headers || {}, payload.data || null, isFile,
			payload.timeout || 60000);
}

function assert(bool, message) {
	base.assertTrue(bool, message || "");
}

function writeToFile(filename, data, append) {
	base.writeToFile(filename, data, append || false);
}

function readFromFile(filename) {
	return base.readFromFile(filename);
}

function fileExists(filename) {
	return base.fileExists(filename);
}

function global(name, value) {
	if (value !== undefined) {
		base.setGlobal(name, value);
	} else if (name !== undefined) {
		return base.getGlobal(name);
	} else {
		return base.getAllGlobals();
	}
}

function evalXPath(xml, xpath) {
	return base.evalXPath(xml, xpath);
}

function sleep(millis) {
	base.sleep(millis);
}

function thread(func) {
	base.thread(func);
}

function timeout(func, millis) {
	thread(function() {
		sleep(millis);
		func();
	});
}
