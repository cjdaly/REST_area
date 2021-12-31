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

import client.apache.ApacheClient;
import client.httpurl.HttpURLClient;

/**
 * RestClientDriver takes a series of options and commands and uses them to
 * drive a REST client to perform HTTP operations (GET, PUT, etc).
 */
public class RestClientDriver {

	static final String DEFAULT_SERVER = "http://localhost:5000/";

	static final Pattern OPTION_REGEX = Pattern.compile("^-(\\w+)=(.*)$");
	static final Pattern COMMAND_REGEX = Pattern.compile("^(\\w+)([(]([a-zA-Z0-9,;=/_~ ]*)[)])?$");

	private String[] _args;
	private String _serverUrlBase = DEFAULT_SERVER;
	private String _clientType = "httpurl";
	private RestClient _client;

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
		default:
			System.out.println("??? Unknown option: " + name + " = " + value);
		}
	}

	/**
	 * Creates a new RestClientDriver with the supplier program arguments.
	 */
	public RestClientDriver(String[] args) {
		_args = args;

		for (String arg : _args) {
			Matcher matcher = OPTION_REGEX.matcher(arg);
			if (matcher.matches()) {
				processOption(matcher.group(1), matcher.group(2));
			}

			if ("apache".equals(_clientType)) {
				_client = new ApacheClient(_serverUrlBase);
			} else {
				_client = new HttpURLClient(_serverUrlBase);
			}
		}

		System.out.println();
		System.out.println("RESTstop Java Client (type: " + _client.getType() + ", server: " + _serverUrlBase + ")");
	}

	/**
	 * Process commands from the argument list. Commands begin with a name and are
	 * optionally followed by a parenthetical list of parameters.
	 * 
	 * Example: <code>name(arg1,arg2=foo,arg/3=foo/bar)</code>
	 */
	public void processCommands() {
		for (String arg : _args) {
			Matcher matcher = COMMAND_REGEX.matcher(arg);
			if (matcher.matches()) {
				processCommand(matcher.group(1), matcher.group(3));
			}
		}
	}

	private void processCommand(String name, String paramList) {
		String[] params;
		if (paramList == null) {
			params = new String[0];
		} else {
			params = paramList.split(",");
		}

		switch (name.toLowerCase()) {
		case "get":
			_client.doGet(params.length == 0 ? "" : params[0]);
			break;
		case "put":
			if (params.length >= 2) {
				_client.doPut(params[0], params[1]);
			}
			break;
		case "post":
			if (params.length >= 2) {
				_client.doPost(params[0], params[1]);
			}
			break;
		case "delete":
			if (params.length >= 1) {
				_client.doDelete(params[0]);
			}
			break;
		case "sleep":
			int millis = params.length == 0 ? 1000 : parseParamInt(params[0], 1000);
			System.out.println();
			System.out.println("! SLEEP: " + millis);
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				// OK
			}
			break;
		default:
			System.out.println("Unknown command: " + name);
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

}
