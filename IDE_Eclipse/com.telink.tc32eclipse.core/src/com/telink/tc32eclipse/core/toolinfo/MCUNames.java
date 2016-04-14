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
 * $Id: MCUNames.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.toolinfo;

import java.util.HashSet;
import java.util.Set;

import com.telink.tc32eclipse.core.IMCUProvider;
//import com.telink.tc32eclipse.core.util.TC32MCUidConverter;

/**
 * This class handles the conversion of known MCU ids to MCU Names.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class MCUNames implements IMCUProvider {

	private static MCUNames fInstance = null;

	/**
	 * Get the default instance of the Signatures class
	 */
	public static MCUNames getDefault() {
		if (fInstance == null)
			fInstance = new MCUNames();
		return fInstance;
	}

	// private constructor to prevent instantiation
	private MCUNames() {
	}

	/**
	 * Get the Name for the given MCU id.
	 * 
	 * @param mcuid
	 *            String with a MCU id
	 * @return String with the MCU Name.
	 */
	public String getName(String mcuid) {
		return "TC32";
	}

	/**
	 * Get the MCU id for the given Name.
	 * 
	 * @param mcuname
	 *            String with an MCU name
	 * @return String with the corresponding MCU id or <code>null</code> if
	 *         the given id is invalid.
	 */
	public String getID(String mcuname) {
		return "TC32";
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
		return getName(mcuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#getMCUList()
	 */
	public Set<String> getMCUList() {
		return new HashSet<String>(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#hasMCU(java.lang.String)
	 */
	public boolean hasMCU(String mcuid) {
		if (getName(mcuid)!= null) {
			return true;
		}
		return false;
	}

}
