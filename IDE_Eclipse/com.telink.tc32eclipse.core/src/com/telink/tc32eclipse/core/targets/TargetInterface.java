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
 * $Id: TargetInterface.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

/**
 * Enumeration of all known interfaces between a programmer and the target hardware.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public enum TargetInterface {

	/** Standard In System Programming interface */
	USB("User Interface"),

	/** JTAG interface */
	JTAG("JTAG") {
		@Override
		public boolean isOCDCapable() {
			return true;
		}
	};


	/**
	 * Checks if the interface supports On Chip Debugging
	 * 
	 * @return <code>true</code> if capable of OCD.
	 */
	public boolean isOCDCapable() {
		// All interfaces except JTAG and DEBUGWIRE can not
		// be used for On Chip Debugging
		return false;
	}

	private final String	fDescription;

	private TargetInterface(String description) {
		fDescription = description;
	}

	/**
	 * Get a human readable description of the interface type
	 * 
	 * @return
	 */
	public String getDescription() {
		return fDescription;
	}

}
