package nohorjo.resttest.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLUtils {
	public static String evalXPath(String xml, String xpath)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		Document doc = loadXMLFromString(xml);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xp = xPathfactory.newXPath();
		XPathExpression expr = xp.compile(xpath);
		return expr.evaluate(doc);
	}

	private static Document loadXMLFromString(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new ByteArrayInputStream(xml.getBytes()));
	}
}
