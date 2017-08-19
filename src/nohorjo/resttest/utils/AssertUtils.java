package nohorjo.resttest.utils;

public class AssertUtils {
	public static void assertTrue(boolean True, String message) {
		if (!True) {
			throw new AssertionError(message);
		}
	}
}
