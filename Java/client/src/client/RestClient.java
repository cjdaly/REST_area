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

/**
 * RestClient is the abstract base class for specific REST client
 * implementations.
 */
public abstract class RestClient {

	protected String _urlBase;

	protected RestClient(String urlBase) {
		_urlBase = urlBase;
	}

	/**
	 * @return a unique type name for the RestClient implementation
	 */
	protected abstract String getType();

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

	protected void showCommand(String endpoint, String method) {
		System.out.println();
		System.out.println("--------------------");
		System.out.println("! " + method + " /" + endpoint);
	}

	protected void showResponse(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		System.out.println("----[Response]------");
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
		}
		System.out.println("--------------------");
	}

	/**
	 * Performs an HTTP GET with the specified <code>endpoint</code>.
	 */
	public abstract void doGet(String endpoint);

	/**
	 * Performs an HTTP PUT on the specified <code>endpoint</code> with
	 * <code>value</code>.
	 */
	public abstract void doPut(String endpoint, String value);

	/**
	 * Performs an HTTP POST to the specified <code>endpoint</code> with
	 * <code>value</code>.
	 */
	public abstract void doPost(String endpoint, String value);

	/**
	 * Performs an HTTP DELETE of the specified <code>endpoint</code>.
	 */
	public abstract void doDelete(String endpoint);

}
