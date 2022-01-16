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

import client.command.Command;
import client.impl.apache.ApacheClient;
import client.impl.httpurl.HttpURLClient;
import client.impl.java11.Java11HttpClient;
import client.output.Output;

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

	/**
	 * Creates a new RestClientDriver with the supplied program arguments.
	 */
	public RestClientDriver(String[] args) {
		this(args, null);
	}

	/**
	 * Creates a new RestClientDriver with the supplied program arguments.
	 */
	public RestClientDriver(String[] args, Output output) {
		_args = (args == null) ? new String[0] : args;
		output = output != null ? output : new Output();

		for (String arg : _args) {
			Matcher matcher = OPTION_REGEX.matcher(arg);
			if (matcher.matches()) {
				processOption(matcher.group(1), matcher.group(2), output);
			}
		}

		if (_clientType.toLowerCase().equals("apache")) {
			_client = new ApacheClient(_serverUrlBase);
		} else if (_clientType.toLowerCase().equals("java11")) {
			_client = new Java11HttpClient(_serverUrlBase);
		} else {
			_client = new HttpURLClient(_serverUrlBase);
		}
		_client.setOutput(output);

		output.Info.writeln("REST_area Java Client" + //
				" (type: " + _client.getType() + //
				", server: " + _serverUrlBase + ")" //
		);
	}

	/**
	 * @return the type name of the client implementation class
	 */
	public String getClientType() {
		return _client.getType();
	}

	/**
	 * Process <code>-key=value</code> formatted command line options from the
	 * argument list.
	 */
	private void processOption(String name, String value, Output output) {
		switch (name.toLowerCase()) {
		case "server":
			_serverUrlBase = value;
			break;
		case "client":
			_clientType = value;
			break;
		case "output":
			switch (value) {
			case "ansi":
				output.ansi(true);
				break;
			case "noansi":
				output.ansi(false);
				break;
			}
			break;
		default:
			output.Error.writeln("Unknown option: " + name + " = " + value);
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
