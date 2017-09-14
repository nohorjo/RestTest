var cookie = "random=cookie";
var postString = "somedata";
var filename = "testfile.txt";

function check(resp, post) {
	assert(resp.code == 200);
	assert(resp.message == "OK");
	assert(resp.headers["Content-Type"][0] == "application/json");

	var body = JSON.parse(resp.body);
	assert(body.headers.Cookie == cookie);
	if (post) {
		assert(body.method == "POST");
		assert(body.data == postString);
	} else {
		assert(body.method == "GET");
	}
}

var resp = makeRequest({
	url : "http://httpbin.org/anything",
	method : "GET",
	headers : {
		Cookie : cookie
	}
});

check(resp);

resp = makeRequest({
	url : "http://httpbin.org/anything",
	headers : {
		Cookie : cookie
	}
});

check(resp);

resp = makeRequest({
	url : "http://httpbin.org/anything",
	method : "post",
	headers : {
		Cookie : cookie,
		"Content-Type" : "text/plain"
	},
	data : postString
});

check(resp, true);

writeToFile(filename, postString);

resp = makeRequest({
	url : "http://httpbin.org/anything",
	method : "post",
	headers : {
		Cookie : cookie,
		"Content-Type" : "application/octet-stream"
	},
	data : filename,
	file : true
});

check(resp, true);

deleteFile(filename);
