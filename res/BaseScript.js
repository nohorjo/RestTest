var base = Java.type("nohorjo.resttest.BaseScript");

function makeRequest(payload) {
	var isFile = false;
	if(payload.file){
		isFile = true;
	}
	return base.makeRequest(payload.url, payload.method.toUpperCase(), payload.headers, payload.data, isFile);
}

function sendFile