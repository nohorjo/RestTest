package nohorjo.resttest.project;

public class TestCase {

	private String name;
	private String testStepsScript;

	public TestCase(String name, String testStepsScript) {
		this.name = name;
		this.testStepsScript = testStepsScript;
	}

	public String getName() {
		return name;
	}

	public String getTestStepsScript() {
		return testStepsScript;
	}
}
