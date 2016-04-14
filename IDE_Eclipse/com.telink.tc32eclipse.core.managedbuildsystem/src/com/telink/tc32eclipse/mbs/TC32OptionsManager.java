/******************************************************************************
 * Copyright (c) 2009-2016 Telink Semiconductor Co., LTD.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * -----------------------------------------------------------------------------
 * Module:
 * Purpose:
 * Reference :     
 * $Id: TC32OptionsManager.java 851 20.1.08-07 19:37:00Z innot $
 *******************************************************************************/
package com.telink.tc32eclipse.mbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TC32OptionsManager {

	private static Map<String, String> options = new HashMap<String, String>();
	private static List<IOptionsChangeListener> changeListeners = new ArrayList<IOptionsChangeListener>();

	public static String getOption(String name) {

		synchronized (options) {
			return options.get(name);
		}
	}

	public static void setOption(String option, String value) {
		synchronized (options) {
			options.put(option, value);
		}
		for (IOptionsChangeListener listener : changeListeners) {
			listener.optionChanged(option, value);
		}
	}

	public static void addOptionChangeListener(IOptionsChangeListener listener) {
		changeListeners.add(listener);
	}

}
