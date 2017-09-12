var content = "Hello World!"
var xml =
	"<html>" +
		"<head><title>Example</title></head>" +
		"<body>" +
			"<h1>"+content+"</h1>" +
		"</body>" +		
	"</html>";

var xpath = "/html/body/h1";

var e = evalXPath(xml, xpath);

assert(content == e)