package client;

public abstract class RestClient {

	protected String _urlBase;

	protected RestClient(String urlBase) {
		_urlBase = urlBase;
	}

	public abstract void doGet(String endpoint);

}
