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

	public static class Color {
		public static final Color Black = new Color(AttributedStyle.BLACK);
		public static final Color Red = new Color(AttributedStyle.RED);
		public static final Color Green = new Color(AttributedStyle.GREEN);
		public static final Color Yellow = new Color(AttributedStyle.YELLOW);
		public static final Color Blue = new Color(AttributedStyle.BLUE);
		public static final Color Magenta = new Color(AttributedStyle.MAGENTA);
		public static final Color Cyan = new Color(AttributedStyle.CYAN);
		public static final Color White = new Color(AttributedStyle.WHITE);

		public Color(int r, int g, int b) {
			_r = r;
			_g = g;
			_b = b;
			_3bit = -1;
		}

		Color(int val) {
			_3bit = val;
			_r = -1;
			_g = -1;
			_b = -1;
		}

		boolean is3Bit() {
			return _3bit >= 0 && _3bit < 8;
		}

		final int _3bit;
		final int _r, _g, _b;
	}

	public enum Attr {
		Bold, Faint, Italic, Underline, Strikethrough
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
			if (_fg.is3Bit()) {
				style = style.foreground(_fg._3bit);
			} else {
				style = style.foreground(_fg._r, _fg._g, _fg._b);
			}
		}

		if (_bg != null) {
			if (_bg.is3Bit()) {
				style = style.background(_bg._3bit);
			} else {
				style = style.background(_bg._r, _bg._g, _bg._b);
			}
		}

		if (_attrs != null) {
			for (Attr attr : _attrs) {
				switch (attr) {
				case Bold:
					style = style.bold();
					break;
				case Faint:
					style = style.faint();
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

		return new AttributedStringBuilder().style(style);
	}
}
