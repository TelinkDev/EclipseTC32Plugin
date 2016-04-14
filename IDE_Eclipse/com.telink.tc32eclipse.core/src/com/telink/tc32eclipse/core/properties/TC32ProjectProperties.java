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
 * $Id: TC32ProjectProperties.java 851 2010-08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.properties;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Container for all TC32 Plugin specific properties of a project.
 * <p>
 * Upon instantiation, the properties are loaded from the given preference
 * store. All changes are local to the object until the {@link #save()} method
 * is called.
 * </p>
 * <p>
 * TC32ConfigurationProperties objects do not reflect changes made to other
 * TC32ConfigurationProperties for the same Project/Configuration, so they should
 * not be held on to and be reloaded every time the current values are required.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public class TC32ProjectProperties {

	private static final String NODE_TCDB = "TCDB";

	private static final String KEY_MCUTYPE = "MCUType";
	private static final String DEFAULT_MCUTYPE = "tc32";
	private String fMCUid = "tc32";
	
	private String fBinaryTargetName = null;
	


	private TCDBProperties fTCDBProperties;

	/**
	 * The source/target Preferences for the properties or <code>null</code>
	 * if default properties are represented.
	 */
	private IEclipsePreferences fPrefs;

	/** Flag if any properties have been changed */
	private boolean fDirty;

	/**
	 * Load the TC32 project properties from the given Preferences.
	 * 
	 * @param prefs
	 *            <code>IEclipsePreferences</code>
	 */
	public TC32ProjectProperties(IEclipsePreferences prefs) {
		fPrefs = prefs;
		loadData();
	}

	/**
	 * Load the TC32 Project properties from the given
	 * <code>TC32ConfigurationProperties</code> object.
	 * 
	 * @param source
	 */
	public TC32ProjectProperties(IEclipsePreferences prefs, TC32ProjectProperties source) {
		fPrefs = prefs;
		fMCUid = source.fMCUid;
		//fFCPU = source.fFCPU;

		//fUseExtRAM = source.fUseExtRAM;
		//fExtRAMSize = source.fExtRAMSize;
		//fUseExtRAMforHeap = source.fUseExtRAMforHeap;
		//fUseEEPROM = source.fUseEEPROM;

		fTCDBProperties = new TCDBProperties(prefs.node(NODE_TCDB), this,
		        source.fTCDBProperties);

		fDirty = source.fDirty;
	}

	public String getMCUId() {
		return fMCUid;
	}

	public void setMCUId(String mcuid) {
		if (!fMCUid.equals(mcuid)) {
			fMCUid = mcuid;
			fDirty = true;
		}
	}

	
	public String getBinaryTargetName() {
		return fBinaryTargetName;
	}

	public void setBinaryTargetName(String targetName) {
		fBinaryTargetName = targetName;
	}

	public TCDBProperties getTCDBProperties() {
		return fTCDBProperties;
	}

	/**
	 * Load all options from the preferences.
	 */
	protected void loadData() {
		fMCUid = fPrefs.get(KEY_MCUTYPE, DEFAULT_MCUTYPE);
	
		fTCDBProperties = new TCDBProperties(fPrefs.node(NODE_TCDB), this);

		fDirty = false;
	}

	/**
	 * Save the modified properties to the persistent storage.
	 * 
	 * @throws BackingStoreException
	 */
	public void save() throws BackingStoreException {

		try {
			if (fDirty) {
				// Save the properties of this class
				fDirty = false;
				fPrefs.put(KEY_MCUTYPE, fMCUid);


				fPrefs.flush();
			}
			// Save the associated TCDB properties
			fTCDBProperties.save();

		} catch (IllegalStateException ise) {
			// This should not happen, but just in case we ignore this unchecked
			// exception
			ise.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(fDirty ? "*" : " ");
		sb.append("[");
		sb.append("mcuid=" + fMCUid);
		sb.append("]");
		return sb.toString();
	}

}
