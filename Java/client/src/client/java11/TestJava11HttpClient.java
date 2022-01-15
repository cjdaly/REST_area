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

package client.java11;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import client.RestClientDriver;
import client.TestClient;
import client.command.Command;
import client.command.HttpCommand;

class TestJava11HttpClient extends TestClient {

	@Test
	void GetRootJava11() {
		RestClientDriver driver = new RestClientDriver(new String[] { "-client=java11" });
		Command command = driver.processSingleCommand("get");
		assertNotNull(command);
		assertTrue(command instanceof HttpCommand);
		HttpCommand hc = (HttpCommand) command;
		assertEquals("GET", hc.getRestMethod());
		assertEquals(0, hc.getErrors().length);
	}

	@Test
	void PutGetDelete_Java11() {
		RestClientDriver driver = new RestClientDriver(new String[] { "-client=java11" });
		testPutGetDeleteProp(driver, "Hello!");
	}

	@Test
	void PostGet_Java11() {
		RestClientDriver driver = new RestClientDriver(new String[] { "-client=java11" });
		testPostGetMsg(driver, "Hello from " + driver.getClientType() + " client!");
	}
}
