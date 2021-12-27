import client.apache.ApacheClient;
import client.httpurl.HttpURLClient;

public class Main {

	static String SERVER_BASE_URL = "http://localhost:5000/";

	public static void main(String[] args) {
		System.out.println("Hello from RestClient!");
		for (String arg : args) {
			System.out.println("- arg: " + arg);
		}

		ApacheClient apacheClient = new ApacheClient(SERVER_BASE_URL);
		apacheClient.doGet("msg");

		HttpURLClient httpUrlClient = new HttpURLClient(SERVER_BASE_URL);
		httpUrlClient.doGet("msg");

	}

}
