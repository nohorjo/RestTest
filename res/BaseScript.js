var base = Java.type("nohorjo.resttest.engine.BaseScript");

function makeMultiPartRequest() {
	return base.makeMultiPartRequest(payload.url, payload.method.toUpperCase(),
			payload.headers || {}, payload.data, payload.timeout || 60000);
}

function makeRequest(payload) {
	return base.makeRequest(payload.url, payload.method.toUpperCase(),
			payload.headers || {}, payload.data || null, payload.file || false,
			payload.timeout || 60000);
}

function assert(bool, message) {
	base.assertTrue(bool || false, message || "");
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

function deleteFile(filename) {
	base.deleteFile(filename);
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

function currentTimestamp(){
	return base.currentTimestamp();
}
