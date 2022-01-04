/*****************************************************************************
 * Copyright (c) 2021 Chris J Daly (github user cjdaly)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   cjdaly - initial API and implementation
 ****************************************************************************/

package client;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import client.RestClient.Command;

public class TestClient {

	@Test
	void SleepCommand() {
		long beforeTimeMillis = System.currentTimeMillis();
		RestClientDriver driver = new RestClientDriver(new String[] { "sleep(1234)" });
		driver.processAllCommands();
		long afterTimeMillis = System.currentTimeMillis();
		assertTrue(beforeTimeMillis + 1200 < afterTimeMillis);
	}

	protected void testPutGetDeleteProp(RestClientDriver driver, String body) {
		Command c;

		// get missing property
		c = driver.processSingleCommand("get(props/testPutGetDel_" + driver.getClientType() + ")");
		assertEquals(404, c.getStatusCode());

		// put the property
		c = driver.processSingleCommand("put(props/testPutGetDel_" + driver.getClientType() + "," + body + ")");
		assertEquals(200, c.getStatusCode());

		// get property (now present)
		c = driver.processSingleCommand("get(props/testPutGetDel_" + driver.getClientType() + ")");
		assertEquals(200, c.getStatusCode());
		assertEquals(1, c.getResponseLines().length);
		assertEquals(body, c.getResponseLines()[0]);

		// delete property
		c = driver.processSingleCommand("delete(props/testPutGetDel_" + driver.getClientType() + ")");
		assertEquals(200, c.getStatusCode());

		// get (now) missing property
		c = driver.processSingleCommand("get(props/testPutGetDel_" + driver.getClientType() + ")");
		assertEquals(404, c.getStatusCode());
	}

}
