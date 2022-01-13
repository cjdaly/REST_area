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

/**
 * Logger allows for both direct output and buffering of messages and
 * distinguishes between normal output and error type messages.
 */
public interface Logger {

	/**
	 * Controls whether standard output messages are displayed or suppressed.
	 */
	void enableStdOut(boolean isEnabled);

	/**
	 * Controls whether standard error messages are displayed or suppressed.
	 */
	void enableStdErr(boolean isEnabled);

	/**
	 * Write messages to standard output.
	 */
	void writeOutputs(String... messages);

	/**
	 * Write messages to standard error.
	 */
	void writeErrors(String... messages);

	/**
	 * Default logger, just writes lines through to stdout or stderr.
	 */
	public static class DefaultLogger implements Logger {

		private boolean _writeStdOut = true;
		private boolean _writeStdErr = true;

		public void enableStdOut(boolean isEnabled) {
			_writeStdOut = isEnabled;
		}

		public void enableStdErr(boolean isEnabled) {
			_writeStdErr = isEnabled;
		}

		//

		public void writeOutputs(String... messages) {
			if (_writeStdOut) {
				for (String message : messages) {
					System.out.println(message);
				}
			}
		}

		public void writeErrors(String... messages) {
			if (_writeStdErr) {
				for (String message : messages) {
					System.err.println(message);
				}
			}
		}

	}
}
