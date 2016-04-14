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
 * $Id: TC32PathProvider.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.paths;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

import com.telink.tc32eclipse.core.preferences.TC32PathsPreferences;

public class TC32PathProvider implements IPathProvider {

	private final IPreferenceStore	fPrefs;
	private final TC32Path			fTC32Path;

	/**
	 * Creates a PathProvider for the instance Preference Store and TC32Path.
	 * 
	 */
	public TC32PathProvider(TC32Path TC32path) {
		this(TC32PathsPreferences.getPreferenceStore(), TC32path);
	}

	/**
	 * Creates a PathProvider for the given Preference Store and TC32Path.
	 * 
	 */
	public TC32PathProvider(IPreferenceStore store, TC32Path TC32path) {
		fPrefs = store;
		fTC32Path = TC32path;
	}

	public String getName() {
		return fTC32Path.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.paths.IPathProvider#getPath()
	 */
	public IPath getPath() {
		// get the path from the preferences store and returns its value,
		// depending on the selected path source

		String pathvalue = fPrefs.getString(fTC32Path.name());

		if (pathvalue.equals(TC32PathManager.SourceType.System.name())) {
			// System path
			return SystemPathHelper.getPath(fTC32Path, false);
		}

		if (pathvalue.startsWith(TC32PathManager.SourceType.Bundled.name())) {
			// Bundle path
			//String bundleid = pathvalue.substring(pathvalue.indexOf(':') + 1);
			return new Path(""); //BundlePathHelper.getPath(fTC32Path, bundleid);
		}
		// else: a custom path
		IPath path = new Path(pathvalue);
		return path;
	}

}
