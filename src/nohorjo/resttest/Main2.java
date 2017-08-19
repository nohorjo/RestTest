package nohorjo.resttest;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;
import javax.xml.xpath.XPathExpressionException;

import nohorjo.resttest.engine.ScriptRunner;
import nohorjo.resttest.project.TestCase;
import nohorjo.resttest.project.TestProject;
import nohorjo.resttest.project.TestSuite;
import nohorjo.resttest.utils.PropertiesUtils;

public class Main2 {

	private static final Map<String, Map<String, String>> results = new HashMap<>();

	private static final FilenameFilter JS_ONLY = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			if (name.endsWith(".js")) {
				System.out.println("Found " + name);
				return true;
			} else {
				return false;
			}
		}
	};

	private static final FilenameFilter DIRS = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			if (new File(dir, name).isDirectory()) {
				System.out.println("Found " + name);
				return true;
			} else {
				return false;
			}
		}
	};

	public static void main(String[] args) throws IOException {
		int returnCode = 0;
		List<TestProject> projects = new ArrayList<>();
		for (String projectDirPath : args) {
			File projectDir = new File(projectDirPath);
			TestProject project = new TestProject(projectDir);
			for (File testSuiteDir : projectDir.listFiles(DIRS)) {
				TestSuite suite = new TestSuite(testSuiteDir.getName());
				for (File testCaseFile : testSuiteDir.listFiles(JS_ONLY)) {
					String testStepsScript = new String(Files.readAllBytes(testCaseFile.toPath()));
					TestCase testCase = new TestCase(testCaseFile.getName(), testStepsScript);
					suite.addTestCase(testCase);
				}
				project.addTestSuite(suite);
			}
			projects.add(project);
		}

		for (TestProject project : projects) {
			project.executeTestSuites();
		}

		try {
			String project = args[0];
			PropertiesUtils.loadEnvAndProperties(project);

			File[] testSuites = new File(project).listFiles(DIRS);
			for (File ts : testSuites) {
				File[] scripts = ts.listFiles(JS_ONLY);
				for (File testCasescript : scripts) {
					int rc = processScript(ts.getName(), testCasescript);
					if (returnCode == 0) {// if any test fails return 1
						returnCode = rc;
					}
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
			returnCode = 1;
		}

		System.out.println("\n\nDone!");
		System.out.println("\n\nSummary:");
		for (String testSuite : results.keySet()) {
			System.out.println(testSuite);
			Map<String, String> testSuiteResults = results.get(testSuite);
			for (String result : testSuiteResults.keySet()) {
				System.out.println("\t" + testSuiteResults.get(result));
			}
		}
		System.exit(returnCode);
	}

	private static int processScript(String testSuiteName, File script) throws Throwable {
		int returnCode = 0;
		String name = script.getName();
		String contents = new String(Files.readAllBytes(script.toPath()));
		System.out.printf("\nExecuting %s\n\n", name);
		long start = System.currentTimeMillis();
		try {
			new ScriptRunner(contents).run();
			System.out.printf("\nFinished %s\n\n", name);
		} catch (AssertionError | RuntimeException e) {
			Throwable ex = e;
			if (ex instanceof AssertionError
					|| (ex instanceof RuntimeException && (ex = ex.getCause()) instanceof XPathExpressionException)) {
				returnCode = 1;
				printOffendingLineNumber(name, ex);
			} else {
				throw e;
			}
		} catch (ScriptException e) {
			returnCode = 1;
			System.err.println(e.getMessage().replace("<eval>", name));
		}

		Map<String, String> m = results.get(testSuiteName);
		if (m == null) {
			m = new HashMap<>();
		}
		m.put(name, String.format("%s\t%d milllis", returnCode == 0 ? "passed" : "failed",
				System.currentTimeMillis() - start));
		results.put(testSuiteName, m);
		return returnCode;
	}

	/**
	 * Prints the line number that caused the exception
	 * 
	 * @param name
	 * @param e
	 */
	private static void printOffendingLineNumber(String name, Throwable e) {
		boolean foundSecond = true;// first one is BaseScript
		for (StackTraceElement ste : e.getStackTrace()) {
			if (ste.getFileName().equals("<eval>") && (foundSecond = !foundSecond)) {
				System.err.printf("Error in %s:%s:%d\t%s\n", name, ste.getMethodName(), ste.getLineNumber(),
						e.getMessage());
				return;
			}
		}
		printOffendingLineNumber(name, e.getCause());
	}

}
