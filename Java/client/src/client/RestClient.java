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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public abstract class RestClient {

	protected String _urlBase;

	protected RestClient(String urlBase) {
		_urlBase = urlBase;
	}

	protected void sendContent(OutputStream output, String content) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(output);
		writer.write(content);
		writer.close();
	}

	protected boolean checkResponseCode(int code) {
		if (code != 200) {
			System.out.println("HTTP Error: " + code);
			return false;
		}
		return true;
	}

	protected void showResponse(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		System.out.println("---Response:");
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
		}
		System.out.println("------------");
	}

	public abstract void doGet(String endpoint);

	public abstract void doPut(String endpoint, String value);

}
