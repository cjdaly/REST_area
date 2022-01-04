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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

	static final Pattern COMMAND_REGEX = Pattern.compile("^(\\w+)([(]([a-zA-Z0-9.,;/?=!_~ ]*)[)])?$");

	/**
	 * Create a new Command for this RestClient with the provided argument.
	 */
	public Command newCommand(String arg) {
		return new Command(arg);
	}

	/**
	 * Encapsulates details of a command, including response information from
	 * invoking REST methods.
	 */
	public class Command {

		private String _arg;

		// generic details
		private String _name;
		private String _paramText;
		private String[] _params;

		// REST details
		private String _restMethod;
		private String _restEndpoint;
		private boolean _isRestBodyFile;

		// response info
		private int _statusCode;
		private ArrayList<String> _responseLines = new ArrayList<String>();

		// errors
		private ArrayList<String> _errorList = new ArrayList<String>();

		public Command(String arg) {
			_arg = arg;

			Matcher matcher = COMMAND_REGEX.matcher(arg);
			if (matcher.matches()) {
				_name = matcher.group(1);

				_paramText = matcher.group(3);
				if (_paramText != null) {
					_params = _paramText.split(",");
				}
				initRestDetails();
			} else {
				writeError("Constructed malformed command: " + _arg);
			}
		}

		public String toString() {
			StringBuilder sb = new StringBuilder("RestClient.Command: name=" + _name);
			if (isRest()) {
				sb.append(", method=" + getRestMethod());
				sb.append(", endpoint=" + getRestEndpoint());
			}
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
				_restEndpoint = _params == null ? "" : _params[0];
			}
		}

		public String getArg() {
			return _arg;
		}

		public boolean isMalformed() {
			return _name == null;
		}

		public boolean isRest() {
			return _restMethod != null;
		}

		public String getRestMethod() {
			return _restMethod;
		}

		public String getRestEndpoint() {
			return _restEndpoint;
		}

		public boolean isRestBodyFile() {
			return _isRestBodyFile;
		}

		public String getRestBody() {
			if (_params.length > 1) {
				return _params[1];
			} else {
				return null;
			}
		}

		public boolean expectRestBody() {
			return "PUT".equals(getRestMethod()) || "POST".equals(getRestMethod());
		}

		public void saveStatusCode(int statusCode) {
			_statusCode = statusCode;
		}

		public int getStatusCode() {
			return _statusCode;
		}

		public String[] getResponseLines() {
			return _responseLines.toArray(new String[0]);
		}

		public void invoke() {
			if (isMalformed()) {
				writeError("Attempt to invoke malformed command: " + _arg);
				return;
			}

			if (isRest()) {
				if (expectRestBody() && getRestBody() == null) {
					writeError("REST command missing expected body parameter!");
					return;
				}
				_logger.writeOutputs("", this.toString());
				RestClient.this.invoke(this);
			} else {
				if ("sleep".equals(_name)) {
					int millis = _params.length == 0 ? 1000 : parseParamInt(_params[0], 1000);

					_logger.writeOutputs("", "!!! SLEEP: " + millis);
					try {
						Thread.sleep(millis);
					} catch (InterruptedException e) {
						// OK
					}
				} else {
					writeError("Attempt to invoke unknown command: " + _arg);
				}
			}
		}

		public void saveResponse(InputStream input) throws IOException {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = reader.readLine();
			while (line != null) {
				_responseLines.add(line);
				line = reader.readLine();
			}
			_logger.writeOutputs(_responseLines.toArray(new String[0]));
		}

		public void saveResponse(Stream<String> input) {
			input.forEach(line -> {
				_responseLines.add(line);
			});
			_logger.writeOutputs(_responseLines.toArray(new String[0]));
		}

		private int parseParamInt(String param, int defaultValue) {
			int value = defaultValue;
			try {
				value = Integer.parseInt(param);
			} catch (NumberFormatException ex) {
				// use default
			}
			return value;
		}

		// errors

		public String[] getErrors() {
			return _errorList.toArray(new String[0]);
		}

		public void writeError(String message) {
			_errorList.add(message);
			_logger.writeError(message);
		}

	}

}
