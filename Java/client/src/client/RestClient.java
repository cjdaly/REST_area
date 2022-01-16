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

import client.command.Command;
import client.command.HttpCommand;
import client.output.Output;

/**
 * RestClient is the abstract base class for specific REST client
 * implementations.
 */
public abstract class RestClient {

	protected String _urlBase;
	protected Output _output;

	/**
	 * Construct a RestClient with a provided REST_area server URL.
	 * 
	 * @param urlBase the root level URL for the REST_area server
	 */
	protected RestClient(String urlBase) {
		this(urlBase, null);
	}

	/**
	 * Construct a RestClient with a provided REST_area server URL and output.
	 * 
	 * @param urlBase the root level URL for the REST_area server
	 * @param output
	 */
	protected RestClient(String urlBase, Output output) {
		_urlBase = urlBase;
		_output = output != null ? output : new Output();
	}

	/**
	 * @return a unique type name for the RestClient implementation
	 */
	protected abstract String getType();

	/**
	 * Invokes the supplied command with the HTTP client implementation.
	 */
	public abstract void invoke(HttpCommand command);

	/**
	 * @return the Output for this client
	 */
	public Output getOutput() {
		return _output;
	}

	/**
	 * Called by RestClientDriver set output during initialization.
	 */
	void setOutput(Output output) {
		_output = output;
	}

	/**
	 * Create a new Command for this RestClient with the provided argument.
	 */
	public Command newCommand(String arg) {
		return Command.New(this, arg);
	}

}
