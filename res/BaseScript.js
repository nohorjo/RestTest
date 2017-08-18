var base = Java.type("nohorjo.resttest.BaseScript");

function makeRequest(payload) {
	var isFile = false;
	if (payload.file) {
		isFile = true;
	}
	return base.makeRequest(payload.url, payload.method.toUpperCase(),
			payload.headers || {}, payload.data || null, isFile);
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

function global(name, value) {
	if (value !== undefined) {
		base.setGlobal(name, value);
	} else {
		return base.getGlobal(name);
	}
}