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
import java.util.ArrayList;

/**
 * RestClient is the abstract base class for specific REST client
 * implementations.
 */
public abstract class RestClient {

	protected String _urlBase;
	protected Logger _logger;

	protected RestClient(String urlBase) {
		_urlBase = urlBase;
	}

	protected void setLogger(Logger logger) {
		_logger = logger;
	}

	/**
	 * @return a unique type name for the RestClient implementation
	 */
	protected abstract String getType();

	/**
	 * Invokes the supplied command with the HTTP client implementation.
	 */
	protected abstract void invoke(Command command);

	/**
	 * Performs an HTTP GET with the specified <code>endpoint</code>.
	 */
	public Command Get(String endpoint) {
		Command c = new Command("GET", endpoint);
		invoke(c);
		return c;
	}

	/**
	 * Performs an HTTP PUT on the specified <code>endpoint</code> with
	 * <code>value</code>.
	 */
	public Command Put(String endpoint, String value) {
		Command c = new Command("PUT", endpoint, value, false);
		invoke(c);
		return c;
	}

	/**
	 * Performs an HTTP POST to the specified <code>endpoint</code> with
	 * <code>value</code>.
	 */
	public Command Post(String endpoint, String value) {
		Command c = new Command("POST", endpoint, value, false);
		invoke(c);
		return c;
	}

	/**
	 * Performs an HTTP DELETE of the specified <code>endpoint</code>.
	 */
	public Command Delete(String endpoint) {
		Command c = new Command("DELETE", endpoint);
		invoke(c);
		return c;
	}

	/**
	 * Performs an HTTP PUT to the specified <code>endpoint</code> with the contents
	 * of the file specified by <code>pathname</code>.
	 */
	public Command PutFile(String endpoint, String pathname) {
		Command c = new Command("PUT", endpoint, pathname, true);
		invoke(c);
		return c;
	}

	/**
	 * Performs an HTTP POST to the specified <code>endpoint</code> with the
	 * contents of the file specified by <code>pathname</code>.
	 */
	public Command PostFile(String endpoint, String pathname) {
		Command c = new Command("POST", endpoint, pathname, true);
		invoke(c);
		return c;
	}

	protected boolean checkResponseCode(int code) {
		if (code != 200) {
			_logger.writeError("HTTP Error: " + code);
			return false;
		}
		return true;
	}

	protected void showCommand(String endpoint, String method) {
		_logger.writeOutputs("", "!!! " + method + " /" + endpoint);
	}

	protected void showResponse(InputStream input) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = reader.readLine();
		while (line != null) {
			lines.add(line);
			line = reader.readLine();
		}
		_logger.writeOutputs(lines.toArray(new String[0]));
	}

	public class Command {
		public final String _Method;
		public final String _Endpoint;
		public final String _Body;
		public final boolean _File;

		Command(String method, String endpoint, String body, boolean isFile) {
			_Method = method;
			_Endpoint = endpoint;
			_Body = body;
			_File = isFile;
		}

		Command(String method, String endpoint) {
			this(method, endpoint, null, false);
		}

		public boolean doOutput() {
			return _Body != null;
		}
	}

}
