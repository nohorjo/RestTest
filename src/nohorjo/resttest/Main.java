package nohorjo.resttest;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;
import javax.xml.xpath.XPathExpressionException;

import nohorjo.resttest.engine.ScriptRunner;
import nohorjo.resttest.utils.PropertiesUtils;

public class Main {

	private static final Map<String, Boolean> results = new HashMap<>();

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

	public static void main(String[] args) {
		int returnCode = 0;
		try {
			String project = args[0];
			PropertiesUtils.loadEnvAndProperties(project);
			File[] scripts = new File(project).listFiles(JS_ONLY);
			for (File script : scripts) {
				int rc = processScript(script);
				if (returnCode == 0) {// if any test fails return 1
					returnCode = rc;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			returnCode = 1;
		}

		System.out.println("\n\nDone!");
		System.out.println("\n\nSummary:");
		for (String test : results.keySet()) {
			System.out.println(test + "\t" + (results.get(test) ? "passed" : "failed"));
		}
		System.exit(returnCode);
	}

	private static int processScript(File script) throws Throwable {
		int returnCode = 0;
		String name = script.getName();
		String contents = new String(Files.readAllBytes(script.toPath()));
		System.out.printf("\nExecuting %s\n\n", name);
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
		results.put(name, returnCode == 0);
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
				System.err.printf("Error in %s:%d\t%s\n", name, ste.getLineNumber(), e.getMessage());
				return;
			}
		}
		printOffendingLineNumber(name, e.getCause());
	}

}
