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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.RestClient.Command;
import client.apache.ApacheClient;
import client.httpurl.HttpURLClient;

/**
 * RestClientDriver takes a series of options and commands and uses them to
 * drive a REST client to perform HTTP operations (GET, PUT, etc).
 */
public class RestClientDriver {

	static final String DEFAULT_SERVER = "http://localhost:5000/";

	static final Pattern OPTION_REGEX = Pattern.compile("^-(\\w+)=(.*)$");

	private String[] _args;
	private int _argIndex = 0;
	private String _serverUrlBase = DEFAULT_SERVER;
	private String _clientType = "httpurl";
	private RestClient _client;
	private Logger _logger;

	/**
	 * Creates a new RestClientDriver with the supplier program arguments.
	 */
	public RestClientDriver(String[] args) {
		_args = (args == null) ? new String[0] : args;
		_logger = new Logger();

		for (String arg : _args) {
			Matcher matcher = OPTION_REGEX.matcher(arg);
			if (matcher.matches()) {
				processOption(matcher.group(1), matcher.group(2));
			}
		}

		if (_clientType.toLowerCase().equals("apache")) {
			_client = new ApacheClient(_serverUrlBase);
		} else {
			_client = new HttpURLClient(_serverUrlBase);
		}
		_client.setLogger(_logger);

		_logger.writeOutputs( //
				"", // blank line
				"REST_area Java Client (type: " + _client.getType() + ", server: " + _serverUrlBase + ")" //
		);
	}

	/**
	 * Process <code>-key=value</code> formatted command line options from the
	 * argument list.
	 */
	private void processOption(String name, String value) {
		switch (name.toLowerCase()) {
		case "server":
			_serverUrlBase = value;
			break;
		case "client":
			_clientType = value;
			break;
		case "logger":
			switch (value) {
			case "out":
				_logger.enableStdOut(true);
				break;
			case "noout":
				_logger.enableStdOut(false);
				break;
			case "err":
				_logger.enableStdErr(true);
				break;
			case "noerr":
				_logger.enableStdErr(false);
				break;
			}
			break;
		default:
			_logger.writeError("Unknown option: " + name + " = " + value);
		}
	}

	/**
	 * Process all commands from the argument list. Commands begin with a name and
	 * are optionally followed by a parenthetical list of parameters.
	 * 
	 * Example: <code>name(arg1,arg2=foo,arg/3=foo/bar)</code>
	 */
	public void processAllCommands() {
		Command c = processNextCommand();
		while (c != null) {
			c = processNextCommand();
		}
	}

	/**
	 * Construct and invoke the next command from the supplied arguments.
	 * 
	 * @return the invoked Command or null if there are no remaining commands
	 */
	public Command processNextCommand() {
		Command c = null;
		while (_argIndex < _args.length) {
			String arg = _args[_argIndex];
			_argIndex++;
			if (!arg.startsWith("-")) {
				c = _client.newCommand(arg);
				break;
			}
		}
		if (c != null && !c.isMalformed()) {
			c.invoke();
		}
		return c;
	}

	/**
	 * Construct and invoke a command from a single supplied argument.
	 * 
	 * @return the invoked Command or null if the argument is an option string.
	 */
	public Command processSingleCommand(String arg) {
		Command c = null;
		if (!arg.startsWith("-")) {
			c = _client.newCommand(arg);
		}
		if (c != null && !c.isMalformed()) {
			c.invoke();
		}
		return c;
	}

}
