package nohorjo.resttest.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {
	public static void writeToFile(String filename, String data, boolean append) throws IOException {
		Files.write(Paths.get(filename), data.getBytes(), StandardOpenOption.CREATE,
				append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
	}

	public static String readFromFile(String filename) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filename)));
	}

	public static boolean fileExists(String filename) {
		return new File(filename).exists();
	}

}
