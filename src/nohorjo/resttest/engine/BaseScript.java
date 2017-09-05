package nohorjo.resttest.engine;

/**
 * Implements utils classes and provides an interface for the JavaScript
 */
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import nohorjo.resttest.utils.AssertUtils;
import nohorjo.resttest.utils.FileUtils;
import nohorjo.resttest.utils.GenericUtils;
import nohorjo.resttest.utils.HttpUtils;
import nohorjo.resttest.utils.PropertiesUtils;
import nohorjo.resttest.utils.XMLUtils;

public class BaseScript {

	public static Map<String, ?> makeMultiPartRequest(String url, String method, Map<String, String> headers,
			List<Map<String, ?>> data, int timeout) throws IOException {
		return HttpUtils.makeMultiPartRequest(url, method, headers, data, timeout);
	}

	public static Map<String, ?> makeRequest(String url, String method, Map<String, String> headers, String data,
			boolean isFile, int timeout) throws IOException {
		return HttpUtils.makeRequest(url, method, headers, data, isFile, timeout);
	}

	public static void assertTrue(boolean True, String message) {
		AssertUtils.assertTrue(True, message);
	}

	public static void writeToFile(String filename, String data, boolean append) throws IOException {
		FileUtils.writeToFile(filename, data, append);
	}

	public static String readFromFile(String filename) throws IOException {
		return FileUtils.readFromFile(filename);
	}

	public static boolean fileExists(String filename) {
		return FileUtils.fileExists(filename);
	}

	public static void setGlobal(String name, Object value) {
		PropertiesUtils.setGlobal(name, value);
	}

	public static Object getGlobal(String name) {
		return PropertiesUtils.getGlobal(name);
	}

	public static Map<String, Object> getAllGlobals() {
		return PropertiesUtils.getAllGlobals();
	}

	public static String evalXPath(String xml, String xpath)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		return XMLUtils.evalXPath(xml, xpath);
	}

	public static void sleep(long millis) {
		GenericUtils.sleep(millis);
	}

	public static void thread(ScriptObjectMirror function) {
		GenericUtils.thread(function);
	}
}
