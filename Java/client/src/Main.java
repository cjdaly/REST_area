import client.apache.ApacheClient;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello!");
		ApacheClient client = new ApacheClient();
		client.doGet("msg");
	}
	
	

}
