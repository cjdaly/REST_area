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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import client.command.Command;
import client.command.HttpCommand;

public class TestClient {

	@Test
	void SleepCommand() {
		long beforeTimeMillis = System.currentTimeMillis();
		RestClientDriver driver = new RestClientDriver(new String[] { "sleep(1234)" });
		driver.processAllCommands();
		long afterTimeMillis = System.currentTimeMillis();
		assertTrue(beforeTimeMillis + 1200 < afterTimeMillis);
	}

	@Test
	void ServerURL() {
		RestClientDriver driver = new RestClientDriver(new String[] { "-server=" + RestClientDriver.DEFAULT_SERVER });
		Command c = driver.processSingleCommand("get");
		assertTrue(c instanceof HttpCommand);
		HttpCommand hc = (HttpCommand) c;
		assertEquals(200, hc.getStatusCode());
	}

	@Test
	void ProcessNextCommand() {
		RestClientDriver driver = new RestClientDriver(new String[] { //
				"sleep(123)", "get", "sleep(123)", "get" //
		});

		int count = 0;
		Command command = driver.processNextCommand();
		while (command != null) {
			count++;
			if (count % 2 == 0) {
				assertTrue(command instanceof HttpCommand);
				HttpCommand hc = (HttpCommand) command;
				assertEquals(200, hc.getStatusCode());
			}
			command = driver.processNextCommand();
		}
		assertEquals(4, count);
	}

	protected void testPutGetDeleteProp(RestClientDriver driver, String body) {
		Command c;
		HttpCommand hc;

		// get missing property
		c = driver.processSingleCommand("get(props/testPutGetDel_" + driver.getClientType() + ")");
		assertTrue(c instanceof HttpCommand);
		hc = (HttpCommand) c;
		assertEquals(404, hc.getStatusCode());

		// put the property
		c = driver.processSingleCommand("put(props/testPutGetDel_" + driver.getClientType() + "," + body + ")");
		assertTrue(c instanceof HttpCommand);
		hc = (HttpCommand) c;
		assertEquals(200, hc.getStatusCode());

		// get property (now present)
		c = driver.processSingleCommand("get(props/testPutGetDel_" + driver.getClientType() + ")");
		assertTrue(c instanceof HttpCommand);
		hc = (HttpCommand) c;
		assertEquals(200, hc.getStatusCode());
		assertEquals(1, hc.getResponseLines().length);
		assertEquals(body, hc.getResponseLines()[0]);

		// delete property
		c = driver.processSingleCommand("delete(props/testPutGetDel_" + driver.getClientType() + ")");
		assertTrue(c instanceof HttpCommand);
		hc = (HttpCommand) c;
		assertEquals(200, hc.getStatusCode());

		// get (now) missing property
		c = driver.processSingleCommand("get(props/testPutGetDel_" + driver.getClientType() + ")");
		assertTrue(c instanceof HttpCommand);
		hc = (HttpCommand) c;
		assertEquals(404, hc.getStatusCode());
	}

	private static final Pattern MSG_NUM_REGEX = Pattern.compile("^New message #(\\d+).*");

	protected void testPostGetMsg(RestClientDriver driver, String body) {
		Command c;
		HttpCommand hc;

		// post the body text
		c = driver.processSingleCommand("post(msgs," + body + ")");
		assertTrue(c instanceof HttpCommand);
		hc = (HttpCommand) c;
		assertEquals(200, hc.getStatusCode());
		assertEquals(1, hc.getResponseLines().length);

		// get the message number from the response
		String responseBody = hc.getResponseLines()[0];
		assertTrue(responseBody.startsWith("New message #"));
		Matcher matcher = MSG_NUM_REGEX.matcher(responseBody);
		assertTrue(matcher.matches());
		String msgNum = matcher.group(1);

		// get the message
		c = driver.processSingleCommand("get(msgs/" + msgNum + ")");
		assertTrue(c instanceof HttpCommand);
		hc = (HttpCommand) c;
		assertEquals(200, hc.getStatusCode());
		assertEquals(1, hc.getResponseLines().length);
		assertEquals(body, hc.getResponseLines()[0]);
	}

}
