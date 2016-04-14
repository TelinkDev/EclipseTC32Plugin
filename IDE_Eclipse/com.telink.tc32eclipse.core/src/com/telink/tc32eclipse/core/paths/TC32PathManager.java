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
 * $Id: TC32PathManager.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.paths;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

import com.telink.tc32eclipse.core.preferences.TC32PathsPreferences;

public class TC32PathManager implements IPathProvider {

	public enum SourceType {
		Bundled, System, Custom
	}

	private IPreferenceStore	fPrefs;
	private final TC32Path		fTC32Path;

	private String				fPrefsValue	= null;

	/**
	 * Creates a PathProvider for the instance Preference Store and TC32Path.
	 * 
	 */
	public TC32PathManager(TC32Path TC32path) {
		this(TC32PathsPreferences.getPreferenceStore(), TC32path);
	}

	/**
	 * Creates a PathProvider for the given Preference Store and TC32Path.
	 * 
	 */
	public TC32PathManager(IPreferenceStore store, TC32Path TC32path) {
		fPrefs = store;
		fTC32Path = TC32path;
	}

	/**
	 * Creates a copy of the given TC32PathManager.
	 * 
	 * @param pathmanager
	 */
	public TC32PathManager(TC32PathManager pathmanager) {
		this(pathmanager.fPrefs, pathmanager.fTC32Path);
		fPrefsValue = pathmanager.fPrefsValue;
	}

	/**
	 * Gets the UI name of the underlying TC32Path.
	 * 
	 * @return String with the name
	 */
	public String getName() {
		return fTC32Path.toString();
	}

	/**
	 * Gets a description from the underlying TC32Path.
	 * 
	 * @return String with the description of the path
	 */
	public String getDescription() {
		return fTC32Path.getDescription();
	}

	/**
	 * Gets the current path.
	 * 
	 * This is different from IPathProvider.getPath() because the returned path is cached internally
	 * and can be modified with the setPath() method.
	 * 
	 * 
	 * @return <code>IPath</code>
	 */
	public IPath getPath() {
		// get the path from the preferences store and returns its value,
		// depending on the selected path source

		if (fPrefsValue == null) {
			fPrefsValue = fPrefs.getString(fTC32Path.name());
		}

		if (fPrefsValue.equals(TC32PathManager.SourceType.System.name())) {
			// System path
			return getSystemPath(false);
		}

		if (fPrefsValue.startsWith(TC32PathManager.SourceType.Bundled.name())) {
			// Bundle path
			String bundleid = fPrefsValue.substring(fPrefsValue.indexOf(':') + 1);
			return getBundlePath(bundleid);
		}
		// else: a custom path
		IPath path = new Path(fPrefsValue);
		return path;
	}

	/**
	 * Gets the default path.
	 * 
	 * @return <code>IPath</code> to the default source directory
	 */
	public IPath getDefaultPath() {
		// Don't want to duplicate the parsing done in getPath() so
		// just set the current value to the default, call getPath and
		// restore the current value afterward.
		String defaultvalue = fPrefs.getDefaultString(fTC32Path.name());
		String oldPrefsValue = fPrefsValue;
		fPrefsValue = defaultvalue;
		IPath defaultpath = getPath();
		fPrefsValue = oldPrefsValue;
		return defaultpath;
	}

	/**
	 * Gets the system path.
	 * 
	 * This is the path as determined by system path / windows registry.
	 * 
	 * @param force
	 *            If <code>true</code> reload the system path directly, without using any cached
	 *            values.
	 * 
	 * @return <code>IPath</code> to the system dependent source directory
	 */
	public IPath getSystemPath(boolean force) {
		return SystemPathHelper.getPath(fTC32Path, force);
	}

	/**
	 * Gets the path from the Eclipse bundle with the given id.
	 * 
	 * @param bundleid
	 *            ID of the source bundle
	 * @return <code>IPath</code> to the source directory within the bundle.
	 */
	public IPath getBundlePath(String bundleid) {
		return new Path(""); //BundlePathHelper.getPath(fTC32Path, bundleid);
	}

	/**
	 * Sets the path in the preference store.
	 * 
	 * @param newpath
	 * @param context
	 */
	public void setPath(String newpath, SourceType source) {
		String newvalue = null;
		switch (source) {
			case System:
				newvalue = source.name();
				break;
			case Bundled:
				newvalue = source.name() + ":" + newpath;
				break;
			case Custom:
				newvalue = newpath;
		}
		fPrefsValue = newvalue;
	}

	/**
	 * Sets the path back to the default value.
	 */
	public void setToDefault() {
		fPrefsValue = fPrefs.getDefaultString(fTC32Path.name());
	}

	/**
	 * Gets the source of this path.
	 * 
	 * This can be one of the {@link SourceType} values
	 * <ul>
	 * <li><code>Bundled</code> if the path points to a bundled tc32-elf-gcc toolchain.</li>
	 * <li><code>System</code> if the system default path is used.</li>
	 * <li><code>Custom</code> if the path is selected by the user.</li>
	 * </ul>
	 * 
	 * @return
	 */
	public TC32PathManager.SourceType getSourceType() {
		if (fPrefsValue == null) {
			// get the path source from the preferences store
			fPrefsValue = fPrefs.getString(fTC32Path.name());
		}
		if (fPrefsValue.equals(TC32PathManager.SourceType.System.name())) {
			return TC32PathManager.SourceType.System;
		}
		if (fPrefsValue.startsWith(TC32PathManager.SourceType.Bundled.name())) {
			return TC32PathManager.SourceType.Bundled;
		}
		// else: a custom path
		return TC32PathManager.SourceType.Custom;
	}

	/**
	 * Checks if the path managed by this manager is optional. 
	 * 
	 * @return <code>true</code> if path is not required for basic plugin operation.
	 */
	public boolean isOptional() {
		return fTC32Path.isOptional();
	}
	
	/**
	 * Checks if the current path is valid.
	 * <p>
	 * Some paths are required, some are optional.
	 * </p>
	 * <p>
	 * For required paths this method returns <code>true</code> if a internally defined testfile
	 * exists in the given path.
	 * </p>
	 * <p>
	 * For optional paths this method also returns true if - and only if - the path is empty ("").
	 * </p>
	 * 
	 * @return <code>true</code> if the path points to a valid source folder.
	 */
	public boolean isValid() {
		IPath path = getPath();
		// Test if the file is optional. If optional,
		// then an empty Path is also valid
		if (fTC32Path.isOptional()) {
			if (path.isEmpty()) {
				return true;
			}
		}

		// Test if the testfile exists in the given folder
		IPath testpath = path.append(fTC32Path.getTest());
		File file = testpath.toFile();
		if (file.canRead()) {
			return true;
		}

		// try with ".exe" appended, as otherwise on Windows
		// file.canRead() will fail
		testpath = path.append(fTC32Path.getTest() + ".exe");
		file = testpath.toFile();
		if (file.canRead()) {
			return true;
		}

		return false;
	}

	/**
	 * Sets the PreferenceStore the PathManager should work on.
	 * 
	 * By default the PathManager will work on the Instance Preference store.
	 * 
	 * @param store
	 */
	public void setPreferenceStore(IPreferenceStore store) {
		fPrefs = store;
	}

	/**
	 * Stores the path in the PreferenceStore.
	 * 
	 * Until <code>store()</code> is called, all modifications to the path are only internal to
	 * this IPathManager and not visible outside.
	 */
	public void store() {
		if (fPrefsValue != null) {
			fPrefs.setValue(fTC32Path.name(), fPrefsValue);
		}
	}
}
