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
import client.output.Style;
import client.output.Style.Attr;
import client.output.Style.Color;

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

		Style style_Err = new Style(Color.Yellow, Color.Red, Attr.Bold);
		String prefix_Err = style_Err.builder().append("ERROR").toAnsi() + "> ";
		output.Error.linePrefix("ERROR> ", prefix_Err);

		_driver = new RestClientDriver(args, output);
		_terminal = TerminalBuilder.terminal();

		StringsCompleter completer = new StringsCompleter( //
				"get", "put", "putfile", "post", "postfile", "delete", // REST
				"color", //
				"sleep", //
				"exit");

		_reader = LineReaderBuilder.builder() //
				.terminal(_terminal) //
				.completer(completer) //
				.build();
	}

	public void go() throws IOException {
		Style style_Ra = new Style(Color.White, Color.Green, Attr.Bold);
		String prompt_Ra = style_Ra.builder().append("R_a").toAnsi();
		String lineRaw = null;
		boolean done = false;
		while (!done) {
			lineRaw = _reader.readLine(prompt_Ra + "> ", null);
			String line = lineRaw.trim();
			_terminal.flush();
			if ("".equals(line)) {
				// ...
			} else if ("exit".equals(line)) {
				done = true;
			} else {
				_driver.processSingleCommand(line);
			}
		}

		_terminal.close();
	}

}
