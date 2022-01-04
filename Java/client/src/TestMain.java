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

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class TestMain {

	@Test
	void MainNullArgs() {
		Main.main(null);
	}

	@Test
	void MainSleep() {
		long beforeTimeMillis = System.currentTimeMillis();
		Main.main(new String[] { "sleep(1234)" });
		long afterTimeMillis = System.currentTimeMillis();
		assertTrue(beforeTimeMillis + 1200 < afterTimeMillis);
	}

}
