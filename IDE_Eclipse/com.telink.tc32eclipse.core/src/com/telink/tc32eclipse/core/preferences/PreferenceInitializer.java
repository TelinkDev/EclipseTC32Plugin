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
 * $Id: PreferenceInitializer.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;



/**
 * Class used to initialize default preference values.
 * 
 * <p>
 * This class is called directly from the plugin.xml (in the
 * <code>org.eclipse.core.runtime.preferences</code
 * extension point. It sets default values for the Plugin preferences.
 * </p> 
 * @author Peter Shieh
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		// Store default values to default preferences
	 	TC32PathsPreferences.initializeDefaultPreferences();
	 	TCDBPreferences.initializeDefaultPreferences();
	 	
	 	
	}

}
