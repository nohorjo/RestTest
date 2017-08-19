package nohorjo.resttest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nohorjo.resttest.engine.BaseScript;

public class PropertiesUtils {

	private static final Map<String, Object> globalVariables = new HashMap<>();

	public static void loadEnvAndProperties(String project) throws IOException, FileNotFoundException {
		File propertiesFile = new File(project + File.separator + "project.properties");
		if (propertiesFile.exists()) {
			Properties props = new Properties(System.getProperties());
			try (InputStream is = new FileInputStream(propertiesFile)) {
				props.load(is);
			}
			for (String property : props.stringPropertyNames()) {
				BaseScript.setGlobal(property, props.getProperty(property));
			}
		}
		for (String var : System.getenv().keySet()) {
			BaseScript.setGlobal(var, System.getenv(var));
		}
	}

	public static void setGlobal(String name, Object value) {
		globalVariables.put(name, value);
	}

	public static Object getGlobal(String name) {
		return globalVariables.get(name);
	}

	public static Map<String, Object> getAllGlobals() {
		return globalVariables;
	}

}