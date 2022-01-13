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

	/**
	 * Construct a RestClient with a provided REST_area server URL.
	 * 
	 * @param urlBase the root level URL for the REST_area server
	 */
	protected RestClient(String urlBase) {
		_urlBase = urlBase;
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
	 * Called by RestClientDriver set logger during initialization.
	 */
	void setLogger(Logger logger) {
		_logger = logger;
	}

	//
	// Command nested class and helpers

	static final Pattern COMMAND_REGEX = Pattern.compile("^(\\w+)([(]([^)]*)[)])?$");

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
		int _statusCode;
		ArrayList<String> _responseLines = new ArrayList<String>();

		// errors
		private ArrayList<String> _errorList = new ArrayList<String>();

		/**
		 * construct a Command with the provided argument text
		 */
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

		/**
		 * @return the raw arg used to instantiate the Command
		 */
		public String getArg() {
			return _arg;
		}

		/**
		 * @return true if the Command arg contained syntax errors
		 */
		public boolean isMalformed() {
			return _name == null;
		}

		/**
		 * @return true if the Command is a REST method (e.g. GET, PUT, etc.), otherwise
		 *         false (e.g. sleep command)
		 */
		public boolean isRest() {
			return _restMethod != null;
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

			if (input != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				String line = reader.readLine();
				while (line != null) {
					_responseLines.add(line);
					line = reader.readLine();
				}
				_logger.writeOutputs(_responseLines.toArray(new String[0]));
			}
		}

		/**
		 * RestClient subclasses call this to save HTTP response details after method
		 * invocation
		 */
		public void saveResponseDetails(int statusCode, Stream<String> input) {
			_statusCode = statusCode;

			input.forEach(line -> {
				_responseLines.add(line);
			});
			_logger.writeOutputs(_responseLines.toArray(new String[0]));
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

		/**
		 * Invoke the REST method described by this Command
		 */
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

		/**
		 * @return a list of errors produced when processing the command
		 */
		public String[] getErrors() {
			return _errorList.toArray(new String[0]);
		}

		/**
		 * logs the provided error message and save it for later querying
		 */
		public void writeError(String message) {
			_errorList.add(message);
			_logger.writeError(message);
		}

	}

}
