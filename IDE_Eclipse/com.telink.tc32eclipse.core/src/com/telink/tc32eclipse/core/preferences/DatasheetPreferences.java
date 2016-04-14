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
 * $Id: DatasheetPreferences.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.preferences;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;

import com.telink.tc32eclipse.TC32Plugin;

/**
 * This class handles access to the datasheet preferences.
 * 
 * These preferences are stored per instance.
 * 
 * @author Peter Shieh
 * @since 2.2
 */
public class DatasheetPreferences {

	// paths to the default and instance properties files
	private final static IPath DEFAULTPROPSFILE = new Path("properties/datasheet.properties");

	private static final String CLASSNAME = "datasheets";
	private static final String QUALIFIER = TC32Plugin.PLUGIN_ID + "/" + CLASSNAME;

	private static IPreferenceStore fInstanceStore = null;

	// This is a list that keeps track of all available MCU ids.
	// this is necessary because IPreferenceStore has no methods to
	// get a list of all available keys.
	private static List<String> fAllKeys = new ArrayList<String>();

	/**
	 * Gets the instance Datasheet preferences.
	 * 
	 * @return IPreferenceStore with the properties
	 */
	public static IPreferenceStore getPreferenceStore() {
		// The instance Path PreferenceStore is cached
		if (fInstanceStore != null) {
			return fInstanceStore;
		}

		IScopeContext scope = InstanceScope.INSTANCE;//new InstanceScope();
		IPreferenceStore store = new ScopedPreferenceStore(scope, QUALIFIER);

		fInstanceStore = store;
		return store;
	}

	/**
	 * Gets a list of all MCU IDs currently available in the Datasheet
	 * Preferences.
	 * 
	 * @return Array of <code>String</code> with all MCU IDs or
	 *         <code>null</code> if the preferences could not be read.
	 */
	public static Set<String> getAllMCUs() {

		// Make a new Set, add all default mcu ids to it
		// then add / overwrite all instance mcu ids.
		try {
			Set<String> allmcus = new HashSet<String>();
			IScopeContext defaultscope = InstanceScope.INSTANCE ; //new DefaultScope();
			IEclipsePreferences defaultnode = defaultscope.getNode(QUALIFIER);
			allmcus.addAll(Arrays.asList(defaultnode.keys()));

			IScopeContext instancescope = InstanceScope.INSTANCE;// new InstanceScope();
			IEclipsePreferences instancenode = instancescope.getNode(QUALIFIER);
			allmcus.addAll(Arrays.asList(instancenode.keys()));
			return allmcus;
		} catch (BackingStoreException e) {
			TC32Plugin.getDefault().log(
			        new Status(Status.ERROR, TC32Plugin.PLUGIN_ID,
			                "Can't access Datasheet preferences", e));
			return null;
		}
	}

	/**
	 * Saves the changed preferences.
	 * 
	 * This has to be called to make any changes to the PreferenceStore
	 * persistent.
	 * 
	 * @param store
	 * @throws IOException
	 */
	public static void savePreferences(IPreferenceStore store) throws IOException {
		Assert.isTrue(store instanceof ScopedPreferenceStore);
		((ScopedPreferenceStore) store).save();
	}

	/**
	 * Gets the default Datasheet preferences
	 * 
	 * @return
	 */
	public static IEclipsePreferences getDefaultPreferences() {
		IScopeContext scope = InstanceScope.INSTANCE ; //new DefaultScope();
		return scope.getNode(QUALIFIER);
	}

	/**
	 * Initialize the default datasheet values.
	 * 
	 * The default values are taken from the plugin supplied properties file
	 * <code>properties/datasheet.properties</code>.
	 * 
	 * This is called from
	 * {@link de.innot.avreclipse.core.preferences.PreferenceInitializer}.
	 * Clients are not supposed to call this method.
	 */
	public static void initializeDefaultPreferences() {
		IEclipsePreferences prefs = getDefaultPreferences();

		// Load the list of signatures from the datasheet.properties file
		// as the default values.
		Properties mcuDefaultProps = new Properties();
		Bundle tc32plugin = TC32Plugin.getDefault().getBundle();
		InputStream is = null;
		try {
			is = FileLocator.openStream(tc32plugin, DEFAULTPROPSFILE, false);
			mcuDefaultProps.load(is);
			is.close();
		} catch (IOException e) {
			// this should not happen because the datasheet.properties is
			// part of the plugin and always there.
			TC32Plugin.getDefault().log(
			        new Status(Status.ERROR, TC32Plugin.PLUGIN_ID,
			                "Can't find datasheet.properties", e));
			return;
		}

		Enumeration<?> allnames = mcuDefaultProps.propertyNames();
		while (allnames.hasMoreElements()) {
			String mcuid = (String) allnames.nextElement();
			prefs.put(mcuid, mcuDefaultProps.getProperty(mcuid));
			fAllKeys.add(mcuid);
		}
	}

}
