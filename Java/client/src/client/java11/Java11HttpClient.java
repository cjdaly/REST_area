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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.util.stream.Stream;

import client.RestClient;

public class Java11HttpClient extends RestClient {

	private HttpClient _httpClient;

	public Java11HttpClient(String urlBase) {
		super(urlBase);
		_httpClient = HttpClient.newHttpClient();
	}

	protected String getType() {
		return "Java11HttpClient";
	}

	protected void invoke(Command command) {
		HttpRequest req = null;

		String[] headers = new String[] { //
				"accept", "text/plain", //
				"content-type", "text/plain" //
		};

		switch (command.getRestMethod()) {
		case "GET":
			req = HttpRequest.newBuilder(). //
					uri(URI.create(_urlBase + command.getRestEndpoint())). //
					headers(headers). //
					GET().build();
			break;
		case "PUT":
			if (command.isRestBodyFile()) {
				try {
					req = HttpRequest.newBuilder(). //
							uri(URI.create(_urlBase + command.getRestEndpoint())). //
							headers(headers). //
							PUT(BodyPublishers.ofFile(Path.of(command.getRestBody()))).build();
				} catch (FileNotFoundException e) {
					command.writeError(e.getMessage());
					return;
				}
			} else {
				req = HttpRequest.newBuilder(). //
						uri(URI.create(_urlBase + command.getRestEndpoint())). //
						headers(headers). //
						PUT(BodyPublishers.ofString(command.getRestBody())).build();
			}
			break;
		case "POST":
			if (command.isRestBodyFile()) {
				try {
					req = HttpRequest.newBuilder(). //
							uri(URI.create(_urlBase + command.getRestEndpoint())). //
							headers(headers). //
							POST(BodyPublishers.ofFile(Path.of(command.getRestBody()))).build();
				} catch (FileNotFoundException e) {
					command.writeError(e.getMessage());
					return;
				}
			} else {
				req = HttpRequest.newBuilder(). //
						uri(URI.create(_urlBase + command.getRestEndpoint())). //
						headers(headers). //
						POST(BodyPublishers.ofString(command.getRestBody())).build();
			}
			break;
		case "DELETE":
			req = HttpRequest.newBuilder(). //
					uri(URI.create(_urlBase + command.getRestEndpoint())). //
					headers(headers). //
					DELETE().build();
			break;
		}

		if (req == null) {
			command.writeError("Java11HttpClient.invoke: Unknown method: " + command.getRestMethod());
			return;
		}

		try {
			HttpResponse<Stream<String>> response = _httpClient.send(req, BodyHandlers.ofLines());
			int statusCode = response.statusCode();
			saveResponseDetails(command, statusCode, response.body());
		} catch (IOException e) {
			command.writeError(e.getMessage());
		} catch (InterruptedException e) {
			command.writeError(e.getMessage());
		}
	}

}
