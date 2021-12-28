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

	private HttpURLConnection initConnection(String urlText, String httpMethod, boolean doOutput) throws IOException {
		URL url = new URL(urlText);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(httpMethod);
		con.setRequestProperty("accept", "text/plain");
		con.setRequestProperty("content-type", "text/plain");
		con.setDoOutput(doOutput);
		con.connect();
		return con;
	}

	public void doGet(String endpoint) {
		System.out.println("HttpURLClient: doGet(" + endpoint + ")");
		try {
			HttpURLConnection con = initConnection(_urlBase + endpoint, "GET", false);

			if (checkResponseCode(con.getResponseCode())) {
				showResponse(con.getInputStream());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doPut(String endpoint, String value) {
		System.out.println("HttpURLClient: doPut(" + endpoint + ")");
		try {
			HttpURLConnection con = initConnection(_urlBase + endpoint, "PUT", true);

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

	public void doPost(String endpoint, String value) {
		System.out.println("HttpURLClient: doPost(" + endpoint + ")");
		try {
			HttpURLConnection con = initConnection(_urlBase + endpoint, "POST", true);

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

	public void doDelete(String endpoint) {
		System.out.println("HttpURLClient: doDelete(" + endpoint + ")");
		try {
			HttpURLConnection con = initConnection(_urlBase + endpoint, "DELETE", false);

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
