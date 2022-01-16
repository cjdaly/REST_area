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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.RestClient;
import client.output.Output;

/**
 * Encapsulates details of a command, including response information from
 * invoking REST methods.
 */
public abstract class Command {

	static final Pattern COMMAND_REGEX = Pattern.compile("^(\\w+)([(]([^)]*)[)])?$");

	protected final RestClient _client;
	protected String _name;
	protected String[] _params;

	private ArrayList<String> _errorList = new ArrayList<String>();

	public static Command New(RestClient client, String arg) {
		Matcher matcher = COMMAND_REGEX.matcher(arg);
		if (matcher.matches()) {
			String name = matcher.group(1);

			String paramText = matcher.group(3);
			String[] params = new String[0];
			if (paramText != null) {
				params = paramText.split(",");
			}

			switch (name) {
			case "get":
			case "put":
			case "putfile":
			case "post":
			case "postfile":
			case "delete":
				return new HttpCommand(client, name, params);
			case "sleep":
				return new SleepCommand(client, name, params);
			default:
				client.getOutput().Error.writeln("Constructed unknown command: " + arg);
				return new IllegalCommand(client, name, params);
			}
		} else {
			client.getOutput().Error.writeln("Constructed malformed command: " + arg);
			return new IllegalCommand(client, null);
		}
	}

	/**
	 * construct a Command with the provided name and parameters
	 */
	public Command(RestClient client, String name, String[] params) {
		_client = client;
		_name = name;
		_params = params == null ? new String[0] : params;
	}

	public Command(RestClient client, String name) {
		this(client, name, null);
	}

	protected Output output() {
		return _client.getOutput();
	}

	public String toString() {
		return "Command: name=" + _name;
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
		return false;
	}

	/**
	 * Invokes the command.
	 */
	public abstract void invoke();

	protected int parseParamInt(String param, int defaultValue) {
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
		output().Error.writeln(message);
	}

}