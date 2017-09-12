package nohorjo.resttest.utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class GenericUtils {
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void thread(ScriptObjectMirror function) {
		new Thread() {
			public void run() {
				function.call(null);
			};
		}.start();
	}

	public static long currentTimestamp() {
		return System.currentTimeMillis();
	}
}
