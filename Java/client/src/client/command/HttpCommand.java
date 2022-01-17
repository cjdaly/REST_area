/*****************************************************************************
 * Copyright (c) 2022 Chris J Daly (github user cjdaly)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   cjdaly - initial API and implementation
 ****************************************************************************/

package client.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Stream;

import client.RestClient;
import client.output.Style;
import client.output.Style.Attr;
import client.output.Style.Color;

public class HttpCommand extends Command {

	// request details
	private String _restMethod;
	private String _restEndpoint;
	private boolean _isRestBodyFile;

	// response details
	private int _statusCode;
	private ArrayList<String> _responseLines = new ArrayList<String>();

	// styles
	private final Style _stylePrefix = new Style(Color.Blue, Attr.Underline);
	private final Style _styleMethod = new Style(Color.White, Color.Blue, Attr.Bold);
	private final Style _styleStatusGreen = new Style(Color.White, Color.Green, Attr.Bold);
	private final Style _styleStatusRed = new Style(Color.White, Color.Red, Attr.Bold);

	public HttpCommand(RestClient client, String name, String[] params) {
		super(client, name, params);
		initRestDetails();
	}

	public void invoke() {
		if (expectRestBody() && getRestBody() == null) {
			writeError("REST method " + _restMethod + " missing expected body parameter!");
			return;
		}

		output().Info.write(_stylePrefix, "~ ~ ~ ");
		output().Info.write(_styleMethod, _restMethod);
		output().Info.writeln(" " + _restEndpoint);
		_client.invoke(this);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("HttpCommand: name=" + _name);
		sb.append(", method=" + getRestMethod());
		sb.append(", endpoint=" + getRestEndpoint());
		return sb.toString();
	}

	/**
	 * Set REST details, if applicable.
	 */
	private void initRestDetails() {
		switch (_name.toLowerCase()) {
		case "get":
			_restMethod = "GET";
			break;
		case "put":
			_restMethod = "PUT";
			break;
		case "post":
			_restMethod = "POST";
			break;
		case "delete":
			_restMethod = "DELETE";
			break;
		case "putfile":
			_restMethod = "PUT";
			_isRestBodyFile = true;
			break;
		case "postfile":
			_restMethod = "POST";
			_isRestBodyFile = true;
			break;
		}

		if (_restMethod != null) {
			if (_params == null || _params.length == 0) {
				_restEndpoint = "";
			} else {
				_restEndpoint = _params[0];
			}
		}
	}

	/**
	 * @return true if the Command is a REST method (e.g. GET, PUT, etc.), otherwise
	 *         false (e.g. sleep command)
	 */
	public boolean isRest() {
		return true;
	}

	/**
	 * @return the REST method name (e.g. GET, PUT, etc.)
	 */
	public String getRestMethod() {
		return _restMethod;
	}

	/**
	 * @return the REST endpoint passed as the first command parameter
	 */
	public String getRestEndpoint() {
		return _restEndpoint;
	}

	/**
	 * @return true if the second command parameter represents a filename (for PUT
	 *         or POST), false if the second parameter is literal text to send.
	 */
	public boolean isRestBodyFile() {
		return _isRestBodyFile;
	}

	/**
	 * @return the second command parameter, representing the REST body to PUT/POST,
	 *         or null if there is no second parameter.
	 */
	public String getRestBody() {
		if (_params.length > 1) {
			return _params[1];
		} else {
			return null;
		}
	}

	/**
	 * @return true if this command is a PUT or POST method, with body text to send.
	 */
	public boolean expectRestBody() {
		return "PUT".equals(getRestMethod()) || "POST".equals(getRestMethod());
	}

	/**
	 * RestClient subclasses call this to save HTTP response details after method
	 * invocation
	 */
	public void saveResponseDetails(int statusCode, InputStream input) throws IOException {
		_statusCode = statusCode;

		output().Info.write(_stylePrefix, "~ ~ ~ ");
		if (_statusCode >= 200 && _statusCode < 300) {
			output().Info.write(_styleStatusGreen, Integer.toString(_statusCode), true);
		} else {
			output().Info.write(_styleStatusRed, Integer.toString(_statusCode), true);
		}

		if (input != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = reader.readLine();
			while (line != null) {
				_responseLines.add(line);
				line = reader.readLine();
			}
			output().Info.writeln(_responseLines.toArray(new String[0]));
		}
	}

	/**
	 * RestClient subclasses call this to save HTTP response details after method
	 * invocation
	 */
	public void saveResponseDetails(int statusCode, Stream<String> input) {
		_statusCode = statusCode;

		output().Info.write(_stylePrefix, "~ ~ ~ ");
		if (_statusCode >= 200 && _statusCode < 300) {
			output().Info.write(_styleStatusGreen, Integer.toString(_statusCode), true);
		} else {
			output().Info.write(_styleStatusRed, Integer.toString(_statusCode), true);
		}

		input.forEach(line -> {
			_responseLines.add(line);
		});
		output().Info.writeln(_responseLines.toArray(new String[0]));
	}

	/**
	 * @return the status code from the HTTP method invocation
	 */
	public int getStatusCode() {
		return _statusCode;
	}

	/**
	 * @return an array of lines in the HTTP method invocation response text
	 */
	public String[] getResponseLines() {
		return _responseLines.toArray(new String[0]);
	}

}
