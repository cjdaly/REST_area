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

public class SleepCommand extends Command {

	public SleepCommand(RestClient client, String name, String[] params) {
		super(client, name, params);
	}

	public void invoke() {
		int millis = _params.length == 0 ? 1000 : parseParamInt(_params[0], 1000);
		//
		logger().writeOutputs("", "!!! SLEEP: " + millis);
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// OK
		}
	}
}
