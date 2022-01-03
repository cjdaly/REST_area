package client.httpurl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import client.RestClientDriver;
import client.RestClient.Command;

class TestHttpURLClient {

	@Test
	void GetRootHttpURL() {
		RestClientDriver driver = new RestClientDriver(null);
		Command command = driver.processSingleCommand("get");
		assertNotNull(command);
		assertEquals("GET", command.getRestMethod());
	}

}
