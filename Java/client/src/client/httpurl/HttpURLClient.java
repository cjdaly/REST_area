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

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import client.RestClient;

public class HttpURLClient extends RestClient {

	public HttpURLClient(String urlBase) {
		super(urlBase);
	}

	protected String getType() {
		return "HttpURL";
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

	protected void invoke(Command command) {
		showCommand(command._Endpoint, command._Method);

		try {
			HttpURLConnection con = initConnection(_urlBase + command._Endpoint, command._Method, command.doOutput());

			if (command.doOutput()) {
				if (command._File) {
					Files.copy(new File(command._Body).toPath(), con.getOutputStream());
				} else {
					OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
					writer.write(command._Body);
					writer.flush();
					writer.close();
				}
			}

			if (checkResponseCode(con.getResponseCode())) {
				showResponse(con.getInputStream());
			}
		} catch (IOException e) {
			_logger.writeError(e.getMessage());
		}
	}

}
