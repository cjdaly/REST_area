package client.apache;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import client.RestClientDriver;
import client.RestClient.Command;

class TestApacheClient {

	@Test
	void GetRootApache() {
		RestClientDriver driver = new RestClientDriver(new String[] { "-client=apache" });
		Command command = driver.processSingleCommand("get");
		assertNotNull(command);
		assertEquals("GET", command.getRestMethod());
	}

}
