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

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Style {

	public enum Color {
		Black(AttributedStyle.BLACK), //
		Red(AttributedStyle.RED), //
		Green(AttributedStyle.GREEN), //
		Yellow(AttributedStyle.YELLOW), //
		Blue(AttributedStyle.BLUE), //
		Magenta(AttributedStyle.MAGENTA), //
		Cyan(AttributedStyle.CYAN), //
		White(AttributedStyle.WHITE); //

		Color(int val) {
			_3bit = val;
		}

		final int _3bit;
	}

	public enum Attr {
		Bold, Italic, Underline, Strikethrough
	}

	private Color _fg;
	private Color _bg;
	private Attr[] _attrs;

	public Style(Color fg, Color bg, Attr... attrs) {
		_fg = fg;
		_bg = bg;
		_attrs = attrs;
	}

	public Style(Color fg, Attr... attrs) {
		this(fg, null, attrs);
	}

	public Style(Attr... attrs) {
		this(null, null, attrs);
	}

	public AttributedStringBuilder builder() {
		AttributedStyle style = AttributedStyle.DEFAULT;
		if (_fg != null) {
			style = style.foreground(_fg._3bit);
		}
		if (_bg != null) {
			style = style.background(_bg._3bit);
		}
		if (_attrs != null) {
			for (Attr attr : _attrs) {
				switch (attr) {
				case Bold:
					style = style.bold();
					break;
				case Italic:
					style = style.italic();
					break;
				case Underline:
					style = style.underline();
					break;
				case Strikethrough:
					style = style.crossedOut();
					break;
				}
			}
		}

		AttributedStringBuilder asb = new AttributedStringBuilder();
		return asb.style(style);
	}
}
