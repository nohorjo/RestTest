package nohorjo.resttest;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;

public class Main {

	private static FilenameFilter jsOnly = new FilenameFilter() {
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
		try {
			File[] scripts = new File(args[0]).listFiles(jsOnly);
			for (File script : scripts) {
				String contents = new String(Files.readAllBytes(script.toPath()));
				System.out.println("Executing " + script.getName());
				new ScriptRunner(contents).run();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("\n\nDone!");
		System.exit(0);
	}

}
