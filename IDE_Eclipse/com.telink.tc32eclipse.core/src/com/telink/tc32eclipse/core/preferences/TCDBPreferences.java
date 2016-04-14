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
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.Assert;
//import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.telink.tc32eclipse.TC32Plugin;

/**
 * This class handles access to the TCDB preferences.
 * <p>
 * These preferences are stored per instance.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public class TCDBPreferences {

	private static final String		CLASSNAME				= "TCDB";
	private static final String		QUALIFIER				= TC32Plugin.PLUGIN_ID + "/" + CLASSNAME;
	private static final String		CONFIGQUALIFIER			= QUALIFIER + "/configs/";

	private static IPreferenceStore	fInstanceStore			= null;

	/** Set <code>true</code> to use a custom configuration file */
	public static final String		KEY_USECUSTOMCONFIG		= "customconfigfile";
	public static final boolean		DEFAULT_USECUSTOMCONFIG	= false;

	/** Path to a custom TCDB.conf configuration file */
	public static final String		KEY_CONFIGFILE			= "TCDBconf";

	public static final String		KEY_USECONSOLE			= "TCDBUseConsole";
	public static final boolean		DEFAULT_USECONSOLE		= true;

	/**
	 * Gets the instance TCDB preferences.
	 * 
	 * @return IPreferenceStore with the properties
	 */
	public static IPreferenceStore getPreferenceStore() {
		// The instance Path PreferenceStore is cached
		if (fInstanceStore != null) {
			return fInstanceStore;
		}

		IScopeContext scope = InstanceScope.INSTANCE; //new InstanceScope();
		IPreferenceStore store = new ScopedPreferenceStore(scope, QUALIFIER);

		fInstanceStore = store;
		return store;
	}

	/**
	 * Gets the instance root node Preferences for all Programmer Configurations.
	 * 
	 * @return
	 */
	public static IEclipsePreferences getConfigPreferences() {
		IScopeContext scope = InstanceScope.INSTANCE ; //new InstanceScope();
		IEclipsePreferences root = scope.getNode(CONFIGQUALIFIER);
		return root;
	}

	/**
	 * Saves a changed PreferencesStore.
	 * <p>
	 * This has to be called to make any changes to the given PreferenceStore persistent.
	 * </p>
	 * 
	 * @param store
	 * @throws IOException
	 *             if the PreferenceStore could not be written to the storage.
	 */
	public static void savePreferences(IPreferenceStore store) throws IOException {
		Assert.isTrue(store instanceof IPersistentPreferenceStore);
		((IPersistentPreferenceStore) store).save();
	}

	/**
	 * Gets the default TCDB preferences
	 * 
	 * @return
	 */
	public static IEclipsePreferences getDefaultPreferences() {
		IScopeContext scope = InstanceScope.INSTANCE ; //new DefaultScope();
		return scope.getNode(QUALIFIER);
	}

	/**
	 * Initialize the default TCDB preference values.
	 * 
	 * This is called from {@link de.innot.avreclipse.core.preferences.PreferenceInitializer}.
	 * Clients are not supposed to call this method.
	 */
	public static void initializeDefaultPreferences() {
		IEclipsePreferences prefs = getDefaultPreferences();

		// currently only one global property: The name of a TCDB config file
		// other than the build-in.
		// And the default is to not use it.

		prefs.putBoolean(KEY_USECUSTOMCONFIG, DEFAULT_USECUSTOMCONFIG);
		prefs.put(KEY_CONFIGFILE, "");
		prefs.putBoolean(KEY_USECONSOLE, DEFAULT_USECONSOLE);

	}

}
