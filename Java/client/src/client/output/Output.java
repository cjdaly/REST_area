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

package client.output;

import java.io.PrintStream;

public class Output {

	public final Mode Info = new Mode(System.out);
	public final Mode Error = new Mode(System.out);
	public final Mode Debug = new Mode(System.err);

	public class Mode {
		private PrintStream _output;
		private boolean _enabled = true;
		private boolean _ansi = false;
		private Style _defaultStyle = null;

		Mode(PrintStream output) {
			_output = output;
		}

		public void write(Style style, String message, boolean eol) {
			if (style == null) {
				style = _defaultStyle;
			}

			if (_enabled) {
				if (_ansi && style != null) {
					if (eol) {
						_output.println(style.builder().append(message).toAnsi());
					} else {
						_output.print(style.builder().append(message).toAnsi());
					}
				} else {
					if (eol) {
						_output.println(message);
					} else {
						_output.print(message);
					}
				}
			}
		}

		public void write(Style style, String message) {
			write(style, message, false);
		}

		public void write(String message) {
			write(null, message, false);
		}

		public void writeln(Style style, String... messages) {
			if (style == null) {
				style = _defaultStyle;
			}

			if (_enabled && messages != null) {
				if (_ansi && style != null) {
					for (String message : messages) {
						_output.println(style.builder().append(message).toAnsi());
					}
				} else {
					for (String message : messages) {
						_output.println(message);
					}
				}
			}
		}

		public void writeln(String... messages) {
			writeln(null, messages);
		}

		public boolean enabled() {
			return _enabled;
		}

		public void enabled(boolean enabled) {
			_enabled = enabled;
		}

		public boolean ansi() {
			return _ansi;
		}

		public void ansi(boolean ansi) {
			_ansi = ansi;
		}

		public Style defaultStyle() {
			return _defaultStyle;
		}

		public void defaultStyle(Style style) {
			_defaultStyle = style;
		}

	}

}
