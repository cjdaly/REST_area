
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

import client.RestClientDriver;

public class Main {

	public static void main(String[] args) {
		RestClientDriver driver = new RestClientDriver(args);
		driver.processCommands();
	}

}
