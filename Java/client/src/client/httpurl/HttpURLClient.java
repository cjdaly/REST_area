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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import client.RestClient;

public class HttpURLClient extends RestClient {

	public HttpURLClient(String urlBase) {
		super(urlBase);

	}

	public void doGet(String endpoint) {
		System.out.println("HttpURLClient: doGet(" + endpoint + ")");
		try {
			// setup HTTP GET connection
			URL url = new URL(_urlBase + endpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("accept", "text/plain");
			con.connect();

			if (checkResponseCode(con.getResponseCode())) {
				showResponse(con.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doPut(String endpoint, String value) {
		System.out.println("HttpURLClient: doPut(" + endpoint + ")");
		try {
			// setup HTTP PUT connection
			URL url = new URL(_urlBase + endpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");
			con.setRequestProperty("accept", "text/plain");
			con.setDoOutput(true);
			con.connect();

			sendContent(con.getOutputStream(), value);

			if (checkResponseCode(con.getResponseCode())) {
				showResponse(con.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
