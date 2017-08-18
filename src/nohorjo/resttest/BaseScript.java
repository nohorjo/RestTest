package nohorjo.resttest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class BaseScript {

	public static Map<String, ?> makeRequest(String url, String method, Map<String, String> headers, String data,
			boolean isFile) throws IOException {
		Map<String, Object> response = new HashMap<>();
		if (isFile) {
			data = new String(Files.readAllBytes(new File(data).toPath()));
		}

		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod(method);
		for (String headerName : headers.keySet()) {
			con.setRequestProperty(headerName, headers.get(headerName));
		}

		if (method.equals("POST") || method.equals("PUT")) {
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();
		}

		response.put("responseCode", con.getResponseCode());
		response.put("responseMessage", con.getResponseMessage());

		StringBuffer respText = new StringBuffer();

		BufferedReader responseReader = null;
		try {
			responseReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {
			responseReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}

		String inputLine;
		while ((inputLine = responseReader.readLine()) != null) {
			respText.append(inputLine);
		}

		response.put("responseText", respText.toString());

		response.put("responseHeaders", con.getHeaderFields());

		return response;
	}

	public static void assertTrue(boolean True, String message) {
		if (!True) {
			throw new AssertionError(message);
		}
	}

}
