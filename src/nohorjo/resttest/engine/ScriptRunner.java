package nohorjo.resttest.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import nohorjo.resttest.utils.PropertiesUtils;

public class ScriptRunner {

	private static final String BASE_SCRIPT;
	private static String INIT_SCRIPT = "";

	/**
	 * Loads the base script from the classpath
	 */
	static {
		String script = "";
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(ScriptRunner.class.getClassLoader().getResourceAsStream("BaseScript.js")))) {
			String line;
			while ((line = reader.readLine()) != null) {
				script += line + "\n";
			}
		} catch (IOException e) {
			throw new Error(e);
		}
		BASE_SCRIPT = script;
	}

	private ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");

	private String script;
	private String name;

	public ScriptRunner(String name, String script) throws ScriptException {
		this.script = script;
		this.name = name;
		engine.eval(BASE_SCRIPT);
		engine.eval(INIT_SCRIPT);
	}

	/**
	 * Run the script
	 * 
	 * @throws Throwable
	 */
	public void run() throws Throwable {
		PropertiesUtils.setGlobal("current.test.case", name);
		engine.eval(script);
	}

	/**
	 * Prepares an init script in the root of the project file to be run before each
	 * test case
	 * 
	 * @param projectDir
	 * @throws IOException
	 * @throws ScriptException 
	 */
	public static void loadInitScript(File projectDir) throws IOException, ScriptException {
		File initScript = new File(projectDir, "init.js");
		if (initScript.exists()) {
			INIT_SCRIPT = new String(Files.readAllBytes(initScript.toPath()));
			new ScriptEngineManager(null).getEngineByName("nashorn").eval(INIT_SCRIPT);
		}
	}

}
