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

package client.httpurl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import client.RestClient.Command;
import client.RestClientDriver;
import client.TestClient;

class TestHttpURLClient extends TestClient {

	@Test
	void GetRootHttpURL() {
		RestClientDriver driver = new RestClientDriver(null);
		Command command = driver.processSingleCommand("get");
		assertNotNull(command);
		assertEquals("GET", command.getRestMethod());
		assertEquals(0, command.getErrors().length);
	}

	@Test
	void PutGetDelete_HttpUrl() {
		RestClientDriver driver = new RestClientDriver(new String[] { "-client=classic" });
		testPutGetDeleteProp(driver, "Hello!");
	}

}
