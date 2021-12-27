package client.apache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import client.RestClient;

public class ApacheClient extends RestClient {

	private String _urlBase;
	private CloseableHttpClient _httpClient;

	public ApacheClient() {
		this("http://localhost:5000/");
	}

	public ApacheClient(String urlBase) {
		_urlBase = urlBase;
		HttpClientBuilder b = HttpClientBuilder.create();
		_httpClient = b.build();
	}

	public void doGet(String endpoint) {
		HttpGet get = new HttpGet(_urlBase + endpoint);
		get.addHeader("accept", "text/plain");
		try {
			CloseableHttpResponse response = _httpClient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				System.out.println("HTTP Error: " + statusCode);
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			System.out.println("GET Response:");
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
