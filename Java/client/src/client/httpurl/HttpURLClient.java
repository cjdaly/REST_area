package client.httpurl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import client.RestClient;

public class HttpURLClient extends RestClient {

	public HttpURLClient(String urlBase) {
		super(urlBase);

	}

	public void doGet(String endpoint) {
		System.out.println("HttpURLClient: doGet(" + endpoint + ")");
		try {
			URL url = new URL(_urlBase + endpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("accept", "text/plain");
			int statusCode = con.getResponseCode();
			if (statusCode != 200) {
				System.out.println("HTTP Error: " + statusCode);
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			System.out.println("GET Response:");
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
