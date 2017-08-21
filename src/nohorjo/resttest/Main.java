package nohorjo.resttest;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;
import javax.xml.xpath.XPathExpressionException;

import nohorjo.resttest.engine.ScriptRunner;
import nohorjo.resttest.utils.PropertiesUtils;

public class Main {

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

	private static final Comparator<File> BY_NAME = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	public static void main(String[] args) {
		int returnCode = 0;
		try {
			String project = args[0];
			File projectDir = new File(project);
			PropertiesUtils.loadEnvAndProperties(projectDir);
			ScriptRunner.loadInitScript(projectDir);

			List<File> testSuites = Arrays.asList(projectDir.listFiles(DIRS));
			testSuites.sort(BY_NAME);
			for (File ts : testSuites) {
				List<File> scripts = Arrays.asList(ts.listFiles(JS_ONLY));
				scripts.sort(BY_NAME);
				for (File testCasescript : scripts) {
					int rc = processScript(ts.getName(), testCasescript);
					if (returnCode == 0) {// if any test fails return 1
						returnCode = rc;
					}
				}
			}
			System.out.println("\n\nDone!");
		} catch (Throwable e) {
			e.printStackTrace();
			returnCode = 1;
		}

		if (!results.isEmpty()) {
			System.out.println("\n\nSummary:");
			for (String testSuite : results.keySet()) {
				System.out.println(testSuite);
				Map<String, String> testSuiteResults = results.get(testSuite);
				for (String result : testSuiteResults.keySet()) {
					System.out.println("\t" + result + "\t" + testSuiteResults.get(result));
				}
			}
		}
		System.exit(returnCode);
	}

	private static int processScript(String testSuiteName, File script) {
		int returnCode = 0;
		String name = script.getName();
		System.out.printf("\nExecuting %s\n\n", name);
		long start = System.currentTimeMillis();
		try {
			String contents = new String(Files.readAllBytes(script.toPath()));
			try {
				new ScriptRunner(contents).run();
			} catch (RuntimeException e) {
				throw e.getCause();
			}
			System.out.printf("\nFinished %s\n\n", name);
		} catch (AssertionError | XPathExpressionException e) {
			returnCode = 1;
			printOffendingLineNumber(name, e);
		} catch (ScriptException e) {
			returnCode = 1;
			System.err.println(e.getMessage().replace("<eval>", name));
		} catch (Throwable e) {
			returnCode = 1;
			e.printStackTrace();
		}

		Map<String, String> m = results.get(testSuiteName);
		if (m == null) {
			m = new HashMap<>();
		}
		m.put(name, String.format("%s\t\t%d milllis", returnCode == 0 ? "PASSED" : "FAILED",
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
			if (ste.getFileName().equals("<eval>") && ste.getLineNumber() != -1 && (foundSecond = !foundSecond)) {
				System.err.printf("Error in %s:%s:%d\t%s\n", name, ste.getMethodName(), ste.getLineNumber(),
						e.getMessage());
				return;
			}
		}
		printOffendingLineNumber(name, e.getCause());
	}

}
