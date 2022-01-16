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

import client.RestClient;

public class IllegalCommand extends Command {

	public IllegalCommand(RestClient client, String name, String[] params) {
		super(client, name, params);
	}

	public IllegalCommand(RestClient client, String name) {
		super(client, name);
	}

	public void invoke() {
		output().Error.writeln("Invoked illegal command: " + _name);
	}
}
