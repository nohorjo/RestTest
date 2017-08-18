package nohorjo.resttest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class BaseScript {

	public static Map<String, ?> makeRequest(String url, String method, Map<String, String> headers, String data,
			boolean isFile) throws IOException {
		Map<String, ?> response = new HashMap<>();
		if (isFile) {
			data = new String(Files.readAllBytes(new File(data).toPath()));
		}

		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod(method);
		for (String headerName : headers.keySet()) {
			con.setRequestProperty(headerName, headers.get(headerName));
		}

		if(method.equals("POST")||method.equals("PUT")) {
			
		}
		
		return response;
	}

	public static void ajax(ScriptObjectMirror payload) {
		Thread request = new Thread() {
			@Override
			public void run() {
				int respCode = 500;
				StringBuffer respText = new StringBuffer("");
				Map<String, Object> response = new HashMap<>();
				try {
					URL url = new URL((String) payload.getMember("url"));

					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod((String) payload.getMember("method"));
					try {
						ScriptObjectMirror headers = (ScriptObjectMirror) payload.getMember("headers");
						for (String header : headers.getOwnKeys(true)) {
							con.setRequestProperty(header, headers.getMember(header).toString());
						}
					} catch (ClassCastException e1) {
						// no headers
					}

					try {
						String data = (String) payload.getMember("data");
						con.setDoOutput(true);
						DataOutputStream wr = new DataOutputStream(con.getOutputStream());
						wr.writeBytes(data);
						wr.flush();
						wr.close();
					} catch (ClassCastException e) {
						// no data
					}

					respCode = con.getResponseCode();

					try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
						String inputLine;
						while ((inputLine = in.readLine()) != null) {
							respText.append(inputLine);
						}
					}
					response.put("code", respCode);
					response.put("responseText", respText);
					if (payload.hasMember("success"))
						((ScriptObjectMirror) payload.getMember("success")).call(null, response);
				} catch (ProtocolException | ClassCastException e) {
					e.printStackTrace();
					response.put("code", 500);
					response.put("responseText", respText);
					if (payload.hasMember("error"))
						((ScriptObjectMirror) payload.getMember("error")).call(null, response);
				} catch (IOException e) {
					e.printStackTrace();
					response.put("code", respCode);
					response.put("responseText", respText);
					if (payload.hasMember("error"))
						((ScriptObjectMirror) payload.getMember("error")).call(null, response);
				} finally {
					if (payload.hasMember("complete"))
						((ScriptObjectMirror) payload.getMember("complete")).call(null, response);
				}
			}
		};

		if (payload.getMember("async") instanceof Boolean && !(Boolean) payload.getMember("async")) {
			request.run();
		} else {
			request.start();
		}
	}
}
