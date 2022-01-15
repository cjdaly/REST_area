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

public class RestClientRepl {

	public static void main(String[] args) throws IOException {
		RestClientRepl repl = new RestClientRepl(args);
		repl.go();
	}

	private RestClientDriver _driver;
	private Terminal _terminal;
	private LineReader _reader;

	public RestClientRepl(String[] args) throws IOException {
		_driver = new RestClientDriver(args, new ReplLogger());
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
//			else if ("color".equals(line)) {
//				System.out.println(new AttributedStringBuilder() //
//						.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("hmm") //
//						.style(AttributedStyle.DEFAULT.background(AttributedStyle.CYAN)).append("foo!") //
//						.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("!bar") //
//						.toAnsi());
//			} else if ("get".equals(line)) {
//				Command command = _driver.processSingleCommand(line);
//				for (String error : command.getErrors()) {
//					String errOut = new AttributedStringBuilder() //
//							.style(AttributedStyle.DEFAULT.background(AttributedStyle.RED)) //
//							.append("ERROR: ") //
//							.style(AttributedStyle.DEFAULT) //
//							.append(error) //
//							.toAnsi();
//					System.out.println(errOut);
//				}
//				for (String lineOut : command.getResponseLines()) {
//					String respOut = new AttributedStringBuilder() //
//							.style(AttributedStyle.DEFAULT.background(AttributedStyle.GREEN)) //
//							.append("[" + command.getStatusCode() + "] ") //
//							.style(AttributedStyle.DEFAULT) //
//							.append(lineOut) //
//							.toAnsi();
//					System.out.println(respOut);
//				}
//			} else {
//				System.out.println("!!! Unknown: " + line);
//			}
		}

		_terminal.close();
	}

	private static class ReplLogger extends Logger.DefaultLogger {

	}

//	private static class ReplLogger implements Logger {
//
//		public void enableStdOut(boolean isEnabled) {
//
//		}
//
//		public void enableStdErr(boolean isEnabled) {
//
//		}
//
//		public void writeOutputs(String... messages) {
//
//		}
//
//		public void writeErrors(String... messages) {
//
//		}
//	}
}
