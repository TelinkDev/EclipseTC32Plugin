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
 * $Id: TC32MCUidConverter.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.util;

/**
 * @author Peter Shieh
 * 
 */
public class TC32MCUidConverter {

	/**
	 * Change the lower case mcuid into the official Name.
	 * 
	 * @param mcuid
	 * @return String with UI name of the MCU or <code>null</code> if the given mcuid does not
	 *         match any of the supported name families.
	 */
	public static String id2name(String mcuid) {

		// check invalid mcu names
		if (mcuid == null) {
			return "TC32";
		}
		if ("".equals(mcuid.trim())) {
			return "TC32";
		}

		// TC32 Specific
		if (mcuid.startsWith("TL")) {
			return "TC" + mcuid.substring(7).toUpperCase();
		}
		if (mcuid.startsWith("TS")) {
			// don't include the generic family names
			return "TC" + mcuid.substring(7).toUpperCase();
		}

		return "TC32"; //null;
	}

	public static String name2id(String mcuname) {
		// just convert to lowercase
		return mcuname.toLowerCase();

	}

}
