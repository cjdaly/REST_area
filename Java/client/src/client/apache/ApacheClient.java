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

	private CloseableHttpClient _httpClient;

	public ApacheClient(String urlBase) {
		super(urlBase);
		_httpClient = HttpClientBuilder.create().build();
	}

	public void doGet(String endpoint) {
		System.out.println("ApacheClient: doGet(" + endpoint + ")");
		
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
