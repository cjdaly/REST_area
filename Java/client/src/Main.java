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

import client.apache.ApacheClient;
import client.httpurl.HttpURLClient;

public class Main {

	static String SERVER_BASE_URL = "http://localhost:5000/";

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Hello from RestClient!");
		for (String arg : args) {
			System.out.println("- arg: " + arg);
		}

		ApacheClient apacheClient = new ApacheClient(SERVER_BASE_URL);
		apacheClient.doGet("msg");

		Thread.sleep(1000);

		HttpURLClient httpUrlClient = new HttpURLClient(SERVER_BASE_URL);
		httpUrlClient.doGet("msg");

		Thread.sleep(1000);

		httpUrlClient.doGet("props/hello");

		Thread.sleep(1000);

		httpUrlClient.doPut("props/test", "Mello World!");

	}

}
