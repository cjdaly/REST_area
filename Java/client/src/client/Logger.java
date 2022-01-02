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
public class Logger {

	private boolean _writeStdOut = true;
	private boolean _writeStdErr = true;

	private String _outputPrefix = "";
	private String _errorPrefix = "??? ";

	public void enableStdOut(boolean isEnabled) {
		_writeStdOut = isEnabled;
	}

	public void enableStdErr(boolean isEnabled) {
		_writeStdErr = isEnabled;
	}

	//

	public void writeOutput(String message) {
		message = _outputPrefix + message;
		if (_writeStdOut) {
			System.out.println(message);
		}
	}

	public void writeOutputs(String... messages) {
		String message = combineMessages(_outputPrefix, messages);
		if (_writeStdOut) {
			System.out.print(message);
		}
	}

	public void writeError(String message) {
		message = _errorPrefix + message;
		if (_writeStdErr) {
			System.err.println(message);
		}
	}

	public void writeErrors(String... messages) {
		String message = combineMessages(_errorPrefix, messages);
		if (_writeStdErr) {
			System.err.print(message);
		}
	}

	private String combineMessages(String prefix, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(prefix);
			sb.append(message);
			sb.append("\n");
		}
		return sb.toString();
	}

}
