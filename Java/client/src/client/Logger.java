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

import java.util.ArrayList;

/**
 * Logger allows for both direct output and buffering of messages and
 * distinguishes between normal output and error type messages.
 */
public class Logger {

	private boolean _writeStdOut = true;
	private boolean _writeStdErr = true;

	private boolean _bufferStdOut = false;
	private boolean _bufferStdErr = false;

	private ArrayList<String> _stdOutBuffer = new ArrayList<String>();
	private ArrayList<String> _stdErrBuffer = new ArrayList<String>();

	public Logger() {
		this(true, false);
	}

	public Logger(boolean enableWriting, boolean enableBuffering) {
		enableWriting(enableWriting, enableWriting);
		enableBuffering(enableBuffering, enableBuffering);
	}

	public void enableWriting(boolean stdOut, boolean stdErr) {
		_writeStdOut = stdOut;
		_writeStdErr = stdErr;
	}

	public void enableBuffering(boolean stdOut, boolean stdErr) {
		_bufferStdOut = stdOut;
		_bufferStdErr = stdErr;
	}

	//

	public void writeOutput(String message) {
		if (_writeStdOut) {
			System.out.println(message);
		}
		if (_bufferStdOut) {
			_stdOutBuffer.add(message);
		}
	}

	public void writeOutputs(String... messages) {
		String message = combineMessages(messages);
		if (_writeStdOut) {
			System.out.println(message);
		}
		if (_bufferStdOut) {
			_stdOutBuffer.add(message);
		}
	}

	public void writeError(String message) {
		if (_writeStdErr) {
			System.err.println(message);
		}
		if (_bufferStdErr) {
			_stdErrBuffer.add(message);
		}
	}

	public void writeErrors(String... messages) {
		String message = combineMessages(messages);
		if (_writeStdErr) {
			System.err.println(message);
		}
		if (_bufferStdErr) {
			_stdErrBuffer.add(message);
		}
	}

	private String combineMessages(String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message);
			sb.append("\n");
		}
		return sb.toString();
	}

	//

	public String popOutput() {
		if (_stdOutBuffer.size() == 0) {
			return null;
		} else {
			return _stdOutBuffer.remove(0);
		}
	}

	public String popError() {
		if (_stdErrBuffer.size() == 0) {
			return null;
		} else {
			return _stdErrBuffer.remove(0);
		}
	}
}
