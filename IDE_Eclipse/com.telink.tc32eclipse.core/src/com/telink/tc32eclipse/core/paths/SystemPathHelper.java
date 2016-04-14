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
 * $Id: SystemPathHelper.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.paths;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

import com.telink.tc32eclipse.core.paths.posix.SystemPathsPosix;
import com.telink.tc32eclipse.core.paths.win32.SystemPathsWin32;
import com.telink.tc32eclipse.core.preferences.TC32PathsPreferences;

/**
 * Convenience class to get the current operating system dependent path for a given resource.
 * 
 * This class acts as a switch to the the operating system dependent </code>IPathProvider</code>s.
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public final class SystemPathHelper {

	private final static String					CACHE_TAG	= "syspath/";

	private final static Map<TC32Path, IPath>	fPathCache	= new HashMap<TC32Path, IPath>(TC32Path.values().length);

	/**
	 * Get the path to a resource, depending on the operating system.
	 * 
	 * @param TC32path
	 *            TC32Path for which to get the system path.
	 * @param force
	 *            If <code>true</code> reload the system path directly, without using any cached
	 *            values.
	 * 
	 * @return IPath with the current system path.
	 */
	public synchronized static IPath getPath(TC32Path TC32path, boolean force) {

		// if force flag not set check the caches first
		if (!force) {
			// Check if the path is already in the instance cache
			if (fPathCache.containsKey(TC32path)) {
				IPath cachedpath = fPathCache.get(TC32path);
				if (cachedpath != null && !cachedpath.isEmpty()) {
					return cachedpath;
				}
			}

			// Check if the path is in the persistent cache

			// If there is an entry in the preference store named "cache_..." and its value is a
			// valid directory path and it contains the test file, then we use it instead of
			// re-searching the system.
			String cachedpath = TC32PathsPreferences.getPreferenceStore().getString(
					CACHE_TAG + TC32path.name());
			if (cachedpath.length() > 0) {
				// Test if the path contains the required test file
				IPath testpath = new Path(cachedpath).append(TC32path.getTest());
				File file = testpath.toFile();
				if (file.canRead()) {
					IPath path = new Path(cachedpath);
					fPathCache.put(TC32path, path);
					return path;
				}
				// Test with ".exe" appended for Windows systems
				testpath = new Path(cachedpath).append(TC32path.getTest() + ".exe");
				file = testpath.toFile();
				if (file.canRead()) {
					IPath path = new Path(cachedpath);
					fPathCache.put(TC32path, path);
					return path;
				}
			}
		}

		// If either the force flag was set or the path was not found in either cache, then
		// search
		// for the path.

		IPath path = null;
		if (isWindows()) {
			path = SystemPathsWin32.getSystemPath(TC32path);
		} else {
			// posix path provider
			path = SystemPathsPosix.getSystemPath(TC32path);
		}

		// if a path was found then store it in both caches
		if (path.getDevice() != null) {
			// instance cache
			fPathCache.put(TC32path, path);

			// persistent cache
			TC32PathsPreferences.getPreferenceStore().putValue(CACHE_TAG + TC32path.name(),
					// PS path.toOSString());
			        path.toPortableString());
		}


		return path;

	}

	/**
	 * Clear both the instance and the persistent system path cache.
	 * <p>
	 * This method is currently not used in the plugin.
	 * </p>
	 */
	public synchronized static void clearCache() {

		// Clear the instance cache
		fPathCache.clear();

		// Clear the persistent cache
		IPreferenceStore prefs = TC32PathsPreferences.getPreferenceStore();
		for (TC32Path TC32path : TC32Path.values()) {
			if (prefs.contains(CACHE_TAG + TC32path.name())) {
				prefs.setToDefault(CACHE_TAG + TC32path.name());
			}
		}
	}

	/**
	 * @return true if running on windows
	 */
	private static boolean isWindows() {
		return (Platform.getOS().equals(Platform.OS_WIN32));
	}

}
