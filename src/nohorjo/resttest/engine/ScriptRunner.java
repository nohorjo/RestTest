package nohorjo.resttest.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptRunner {

	private static final String BASE_SCRIPT;
	
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

	public ScriptRunner(String script) throws ScriptException {
		this.script = script;
		engine.eval(BASE_SCRIPT);
	}

	/**
	 * Run the script
	 * 
	 * @throws Throwable
	 */
	public void run() throws Throwable {
		engine.eval(script);
	}

}
