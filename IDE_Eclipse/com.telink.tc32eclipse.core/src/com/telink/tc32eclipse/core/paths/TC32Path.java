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
 * $Id: TC32Path.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.paths;

public enum TC32Path {
	// The compiler
	TC32_GCC(true, "tc32-elf-gcc","Directory containing tc32-elf-* toolchain","tc32-elf-gcc"),

	// Make
	MAKE(true, "GNU make", "Directory containing 'make' executable", "make"),

	// The TC32 header files
	//JW :do we need this ??
	//TC32_INCLUDE(true, "TC32 Header Files", "Directory containing 'tc32.h' include file", "tc32.h"), 

	// PCDB executable
	TC32_TOOLS(false, "tcdb", "Directory containing tools executable", "tcdb");


	private boolean	fRequired;
	private String	fName;
	private String	fDescription;
	private String	fTest;

	/**
	 * Default Enum constructor. Sets the internal fields according to the selected enum value.
	 */
	TC32Path(boolean required, String name, String description, String test) {
		fRequired = required;
		fName = name;
		fDescription = description;
		fTest = test;
	}

	public String getDescription() {
		return fDescription;
	}

	public String getName() {
		return fName;
	}

	public boolean isOptional() {
		return !fRequired;
	}

	public String getTest() {
		return fTest;
	}

	@Override
	public String toString() {
		return fName;
	}
}
