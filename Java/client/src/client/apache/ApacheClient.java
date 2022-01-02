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

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import client.RestClient;

public class ApacheClient extends RestClient {

	private CloseableHttpClient _httpClient;

	public ApacheClient(String urlBase) {
		super(urlBase);
		_httpClient = HttpClientBuilder.create().build();
	}

	protected String getType() {
		return "Apache";
	}

	private void initRequest(HttpRequest req) {
		req.addHeader("accept", "text/plain");
		req.addHeader("content-type", "text/plain");
	}

	protected void invoke(Command command) {
		showCommand(command._Endpoint, command._Method);

		HttpUriRequest req = null;
		switch (command._Method) {
		case "GET":
			req = new HttpGet(_urlBase + command._Endpoint);
			break;
		case "PUT":
			req = new HttpPut(_urlBase + command._Endpoint);
			break;
		case "POST":
			req = new HttpPost(_urlBase + command._Endpoint);
			break;
		case "DELETE":
			req = new HttpDelete(_urlBase + command._Endpoint);
			break;
		}

		if (req == null) {
			_logger.writeError("ApacheClient.invoke: Unknown method: " + command._Method);
			return;
		}

		initRequest(req);

		try {
			if (command.doOutput()) {
				HttpEntityEnclosingRequest entReq = (HttpEntityEnclosingRequest) req;
				if (command._File) {
					entReq.setEntity(new FileEntity(new File(command._Body)));
				} else {
					entReq.setEntity(new StringEntity(command._Body));
				}
			}
			CloseableHttpResponse response = _httpClient.execute(req);

			int statusCode = response.getStatusLine().getStatusCode();
			if (checkResponseCode(statusCode)) {
				showResponse(response.getEntity().getContent());
			}
		} catch (IOException e) {
			_logger.writeError(e.getMessage());
		}
	}

}
