package com.theos.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

public class MyEventDispatcher {

	public static String httpPost(String urlStr, Map<String, String> queryParams) {
		try {
			URL url = new URL(urlStr);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			PrintStream ps = new PrintStream(conn.getOutputStream());
			for (Entry<String, String> entry : queryParams.entrySet()) {
				ps.print(entry.getKey());
				ps.print("=");
				ps.print(entry.getValue());
				ps.print("&");
			}
			ps.close();
			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}

			// Buffer the result into a string
			BufferedReader buffRdr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = buffRdr.readLine()) != null) {
				sb.append(line);
			}
			buffRdr.close();
			conn.disconnect();
			log(sb);
			return sb.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	static void log(Object... obj) {
		/*for (Object o : obj) {
			System.out.println(o);
		}*/
	}
}
