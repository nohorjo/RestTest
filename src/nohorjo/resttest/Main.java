package nohorjo.resttest;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;

import javax.script.ScriptException;

public class Main {

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
			File[] scripts = new File(args[0]).listFiles(JS_ONLY);
			for (File script : scripts) {
				String name = script.getName();
				String contents = new String(Files.readAllBytes(script.toPath()));
				System.out.println("Executing " + name);
				try {
					new ScriptRunner(contents).run();
					System.out.println("Finished " + name);
				} catch (AssertionError e) {
					returnCode = 1;
					boolean foundSecond = true;
					for (StackTraceElement ste : e.getStackTrace()) {
						if (ste.getFileName().equals("<eval>") && (foundSecond = !foundSecond)) {
							System.err.printf("Assertion failed in %s:%d\t%s\n", name, ste.getLineNumber(),
									e.getMessage());
						}
					}
				} catch (ScriptException e) {
					System.err.println(e.getMessage().replace("<eval>", name));
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			returnCode = 1;
		}

		System.out.println("\n\nDone!");
		System.exit(returnCode);
	}

}
