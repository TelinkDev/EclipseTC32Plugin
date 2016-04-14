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
 * $Id: HostInterface.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

/**
 * Enumeration of all host interfaces.
 * <p>
 * The host interface is part of the {@link IProgrammer} interface and is used to filter the
 * programmers in the user interface. Also the user interface uses this to show only applicable
 * options.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public enum HostInterface {

	USB("USB Port");

	private final String	fDesc;

	private HostInterface(String desc) {
		fDesc = desc;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return fDesc;
	}

}
