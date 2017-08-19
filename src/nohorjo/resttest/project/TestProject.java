package nohorjo.resttest.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestProject {

	private final Map<String, Object> globalVariables = new HashMap<>();

	private List<TestSuite> testSuites = new ArrayList<>();
	private String name;

	public TestProject(File projectDir) throws FileNotFoundException, IOException {
		this.name = projectDir.getName();

		File propertiesFile = new File(projectDir, "project.properties");
		if (propertiesFile.exists()) {
			Properties props = new Properties();
			try (InputStream is = new FileInputStream(propertiesFile)) {
				props.load(is);
			}
			for (String property : props.stringPropertyNames()) {
				setGlobal(property, props.getProperty(property));
			}
		}
	}

	public void setGlobal(String name, Object value) {
		globalVariables.put(name, value);
	}

	public Object getGlobal(String name) {
		Object var = globalVariables.get(name);
		if (var == null) {
			var = System.getProperty(name);
			if (var == null) {
				var = System.getenv(name);
			}
		}
		return var;
	}

	public Map<String, Object> getAllGlobals() {
		Map<String, Object> globals = new HashMap<>();
		for (String prop : System.getenv().keySet()) {
			globals.put(prop, System.getenv(prop));
		}
		for (String prop : System.getProperties().stringPropertyNames()) {
			globals.put(prop, System.getProperty(prop));
		}
		for (String prop : globalVariables.keySet()) {
			globals.put(prop, globalVariables.get(prop));
		}
		return globals;
	}

	public List<TestSuite> getTestSuites() {
		return testSuites;
	}

	public void setTestSuites(List<TestSuite> testSuites) {
		this.testSuites = testSuites;
	}

	public void addTestSuite(TestSuite testSuite) {
		testSuites.add(testSuite);
	}

	public String getName() {
		return name;
	}

	public void executeTestSuites() {
		for (TestSuite suite : testSuites) {
			// TODO
		}

	}
}
