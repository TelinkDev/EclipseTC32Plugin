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
 * $Id: Datasheets.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.toolinfo;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import com.telink.tc32eclipse.core.IMCUProvider;
import com.telink.tc32eclipse.core.preferences.DatasheetPreferences;

/**
 * This class handles the Datasheets.
 * <p>
 * This class has two main functions:
 * <ol>
 * <li>It maps the {@link DatasheetPreferences} to the {@link IMCUProvider} Interface.</li>
 * <li>It manages the access to the actual Datasheet files.</li>
 * </ol>
 * Datasheets can be accessed with the {@link #getFile(String, IProgressMonitor)} method. This
 * method will download the file from the URL stored in the preferences, and store it in a cache for
 * later access.
 * </p>
 * 
 * @author Peter Shieh
 * @since 2.2
 * 
 */
public class Datasheets implements IMCUProvider {

	private static Datasheets	fInstance			= null;

	private IPreferenceStore	fPreferenceStore	= null;

	/**
	 * Get the default instance of the Datasheets class
	 */
	public static Datasheets getDefault() {
		if (fInstance == null)
			fInstance = new Datasheets();
		return fInstance;
	}

	// private constructor to prevent instantiation
	private Datasheets() {

		// Get the preference store for the datasheets
		fPreferenceStore = DatasheetPreferences.getPreferenceStore();
	}

	//
	// Methods of the IMCUProvider Interface
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#getMCUInfo(java.lang.String)
	 */
	public String getMCUInfo(String mcuid) {
		return fPreferenceStore.getString(mcuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#getMCUList()
	 */
	public Set<String> getMCUList() {
		Set<String> allmcus = DatasheetPreferences.getAllMCUs();
		return allmcus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#hasMCU(java.lang.String)
	 */
	public boolean hasMCU(String mcuid) {
		String value = fPreferenceStore.getString(mcuid);
		return "".equals(value) ? false : true;
	}

}
