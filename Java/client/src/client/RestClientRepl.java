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

package client;

import java.io.IOException;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import client.output.Output;

public class RestClientRepl {

	public static void main(String[] args) throws IOException {
		RestClientRepl repl = new RestClientRepl(args);
		repl.go();
	}

	private RestClientDriver _driver;
	private Terminal _terminal;
	private LineReader _reader;

	public RestClientRepl(String[] args) throws IOException {
		Output output = new Output();
		output.ansi(true);
		_driver = new RestClientDriver(args, output);
		_terminal = TerminalBuilder.terminal();

		StringsCompleter completer = new StringsCompleter("get", "put", "post", "delete", "exit");

		_reader = LineReaderBuilder.builder() //
				.terminal(_terminal) //
				.completer(completer) //
				.build();
	}

	public void go() throws IOException {
		String lineRaw = null;
		boolean done = false;
		while (!done) {
			lineRaw = _reader.readLine("R_a> ", null);
			String line = lineRaw.trim();
			_terminal.flush();
			if ("exit".equals(line)) {
				done = true;
			} else {
				_driver.processSingleCommand(line);
			}
		}

		_terminal.close();
	}

}
