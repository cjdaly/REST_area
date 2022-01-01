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
import java.io.File;
import java.io.FileReader;
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

	public Command Get(String endpoint) {
		Command c = new Command("GET", endpoint);
		doGet(c._Endpoint);
		return c;
	}

	protected abstract void invoke(Command command);

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

	/**
	 * Performs an HTTP PUT to the specified <code>endpoint</code> with the contents
	 * of the file specified by <code>pathname</code>.
	 */
	public void doPutFile(String endpoint, String pathname) {
		String filedata = getFileData(pathname);
		if (filedata != null) {
			doPut(endpoint, filedata);
		}
	}

	/**
	 * Performs an HTTP POST to the specified <code>endpoint</code> with the
	 * contents of the file specified by <code>pathname</code>.
	 */
	public void doPostFile(String endpoint, String pathname) {
		String filedata = getFileData(pathname);
		if (filedata != null) {
			doPost(endpoint, filedata);
		}
	}

	private String getFileData(String pathname) {
		StringBuilder filedata = new StringBuilder();
		File file = new File(pathname);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			while (line != null) {
				filedata.append(line);
				filedata.append("\n");
				line = reader.readLine();
			}
		} catch (IOException e) {
			_logger.writeError(e.getMessage());
			return null;
		}
		return filedata.toString();
	}

	protected boolean checkResponseCode(int code) {
		if (code != 200) {
			_logger.writeError("??? HTTP Error: " + code);
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
