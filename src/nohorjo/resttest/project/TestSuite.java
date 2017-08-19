package nohorjo.resttest.project;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {

	private String name;
	private List<TestCase> testCases = new ArrayList<>();

	public TestSuite(String name) {
		this.name = name;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public void addTestCase(TestCase testCase) {
		testCases.add(testCase);
	}

	public String getName() {
		return name;
	}
}
