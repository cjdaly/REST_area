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

package client.apache;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import client.RestClient;

public class ApacheClient extends RestClient {

	private CloseableHttpClient _httpClient;

	public ApacheClient(String urlBase) {
		super(urlBase);
		_httpClient = HttpClientBuilder.create().build();
	}

	public void doGet(String endpoint) {
		System.out.println("ApacheClient: doGet(" + endpoint + ")");

		HttpGet get = new HttpGet(_urlBase + endpoint);
		get.addHeader("accept", "text/plain");
		try {
			CloseableHttpResponse response = _httpClient.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (checkResponseCode(statusCode)) {
				showResponse(response.getEntity().getContent());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doPut(String endpoint, String value) {
		System.out.println("ApacheClient: doPut(" + endpoint + ")");

	}

}
