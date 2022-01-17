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

package client.command;

import client.RestClient;
import client.output.Style;
import client.output.Style.Attr;
import client.output.Style.Color;

public class ColorCommand extends Command {

	public ColorCommand(RestClient client, String name, String[] params) {
		super(client, name, params);
	}

	public void invoke() {
		if (_params.length != 3) {
			output().Error.writeln("Color command requires 3 parameters (r,b,g)!");
			return;
		}

		int r = max255(parseParamInt(_params[0], 0));
		int g = max255(parseParamInt(_params[1], 0));
		int b = max255(parseParamInt(_params[2], 0));

		Color c = new Color(r, g, b);
		Style sBG = new Style(null, c);
		Style sFG = new Style(c, Attr.Bold);

		output().Info.write("Color test: r=" + r + ", g=" + g + ", b=" + b + " -> ");
		output().Info.write(sBG, "   ");
		output().Info.write(sFG, " TEST ");
		output().Info.write(sBG, "   ", true);
	}

	private int max255(int i) {
		return i > 255 ? 255 : i;
	}
}
