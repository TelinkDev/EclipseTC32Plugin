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
 * $Id: ProgrammerConfig.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.tcdb;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Container class for all Programmer specific options of TCDB.
 * <p>
 * This class also acts as an Interface to the preference store. It knows how to save and delete
 * configurations.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * @since 2.3 added invocation delay option
 * 
 */
public class ProgrammerConfig {

	/** The unique identifier for this configuration */
	private final String		fId;
	public final static String	KEY_ID						= "id";

	/** The unique name of this configuration */
	private String				fName;
	public final static String	KEY_NAME					= "name";

	/** A custom description of this configuration */
	private String				fDescription;
	public final static String	KEY_DESCRIPTION				= "description";
	public final static String	DEFAULT_DESCRIPTION			= "Default Telink Flash Programmer Configuration.";

	/** The TCDB id of the programmer for this configuration */
	private String				fProgrammer;
	public final static String	KEY_PROGRAMMER				= "programmer";
	public final static String	DEFAULT_PROGRAMMER			= "tcdb.exe";


	/** Flag to mark modifications of this config */
	private boolean				fDirty;

	/**
	 * Constructs a ProgrammerConfig with the given id and set the default values.
	 * 
	 * @param id
	 *            Unique id of the configuration.
	 */
	protected ProgrammerConfig(String id) {
		fId = id;
		fDirty = false;
		defaults();
	}

	/**
	 * Constructs a ProgrammerConfig with the given id and load its values from the given
	 * <code>Preferences</code>.
	 * 
	 * @param id
	 *            Unique id of the configuration.
	 * @param prefs
	 *            <code>Preferences</code> node from which to load.
	 */
	protected ProgrammerConfig(String id, Preferences prefs) {
		fId = id;
		fDirty = false;
		loadFromPrefs(prefs);
	}

	/**
	 * Make a copy of the given <code>ProgrammerConfig</code>.
	 * <p>
	 * The copy does not reflect any changes of the original or vv.
	 * </p>
	 * Note: This copy can be saved, even when the given original has been deleted.
	 * </p>
	 * 
	 * @param config
	 */
	protected ProgrammerConfig(ProgrammerConfig config) {
		fId = config.fId;
		loadFromConfig(config);
	}

	/**
	 * Persist this configuration to the preference storage.
	 * <p>
	 * This will not do anything if the configuration has not been modified.
	 * </p>
	 * 
	 * @throws BackingStoreException
	 *             If this configuration cannot be written to the preference storage area.
	 */
	protected synchronized void save(Preferences prefs) throws BackingStoreException {

		if (fDirty) {
			// write all values to the preferences
			prefs.put(KEY_NAME, fName);
			prefs.put(KEY_DESCRIPTION, fDescription);
			prefs.put(KEY_PROGRAMMER, fProgrammer);
			// flush the Preferences to the persistent storage
			prefs.flush();
		}
	}

	/**
	 * @return A <code>List&lt;Strings&gt;</code> with all TCDB options as defined by this
	 *         configuration
	 */
	public List<String> getArguments() {

		List<String> args = new ArrayList<String>();
		
		// PS does nothing and leave it to the argument processing later!!  Important
		//String arg = "-b -e -i";
		//String arg = "-i";
		//args.add(arg);

		return args;
	}
	


	/**
	 * Gets the ID of this configuration.
	 * 
	 * @return <code>String</code> with the ID.
	 */
	public String getId() {
		return fId;
	}

	/**
	 * Sets the name of this configuration.
	 * <p>
	 * The name must not contain any slashes ('/'), as this would cause problems with the preference
	 * store.
	 * </p>
	 * 
	 * @param name
	 *            <code>String</code> with the new name.
	 */
	public void setName(String name) {
		Assert.isTrue(!name.contains("/"));
		fName = name;
		fDirty = true;
	}

	/**
	 * @return The current name of this configuration.
	 */
	public String getName() {
		return fName;
	}

	/**
	 * Sets the description of this configuration.
	 * 
	 * @param name
	 *            <code>String</code> with the new description.
	 */
	public void setDescription(String description) {
		fDescription = description;
		fDirty = true;
	}

	/**
	 * @return The current description of this configuration.
	 */
	public String getDescription() {
		return fDescription;
	}

	/**
	 * Sets the TCDB programmer id of this configuration.
	 * <p>
	 * The programmer id is not checked for validity. It is up to the caller to ensure that the
	 * given id is valid.
	 * </p>
	 * 
	 * @param name
	 *            <code>String</code> with the new programmer id.
	 */
	public void setProgrammer(String programmer) {
		fProgrammer = programmer;
		fDirty = true;
	}

	/**
	 * @return The current TCDB programmer id of this configuration.
	 */
	public String getProgrammer() {
		return fProgrammer;
	}





	/**
	 * Load the values of this Configuration from the preference storage area.
	 * 
	 * @param prefs
	 *            <code>Preferences</code> node for this configuration
	 */
	private void loadFromPrefs(Preferences prefs) {
		fName = prefs.get(KEY_NAME, "");
		fDescription = prefs.get(KEY_DESCRIPTION, "");
		fProgrammer = prefs.get(KEY_PROGRAMMER, "");
	}

	/**
	 * Load the values of this Configuration from the given <code>ProgrammerConfig</code>.
	 * 
	 * @param prefs
	 *            Source <code>ProgrammerConfig</code>.
	 */
	protected void loadFromConfig(ProgrammerConfig config) {
		fName = config.fName;
		fDescription = config.fDescription;
		fProgrammer = config.fProgrammer;
		fDirty = config.fDirty;

	}

	/**
	 * Reset this Configuration to the default values.
	 * <p>
	 * The ID and the Name of this Configuration are not changed.
	 * </p>
	 */
	public void defaults() {
		// Set the defaults
		fDescription = DEFAULT_DESCRIPTION;
		if(System.getProperty("os.name").toLowerCase().indexOf("win")<0) 
			fProgrammer =  "tcdb";
		else
		    fProgrammer = DEFAULT_PROGRAMMER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// for the debugger
		return fName + " (" + fDescription + "): " + getArguments();
	}

}
