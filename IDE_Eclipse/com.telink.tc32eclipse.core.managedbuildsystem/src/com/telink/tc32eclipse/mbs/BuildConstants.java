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
 * $Id: BuildConstants.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.mbs;

/**
 * Default names and values for the TC32 Eclipse Plugin.
 * 
 * <p>Currently two names with their corresponding toolchain option id's are 
 * defined. One for the Target MCU type and one for the Target MCU
 * Clock Frequency.</p>
 * 
 * <p>They are used as
 * <ul>
 * 	<li>name for the <code>valueHandlerExtraArgument</code> attribute of the corresponding
 * 		option in the plugin.xml </li>
 *  <li>name of the generated <code>BuildMacro</code></li>
 *  <li>name of the generated <code>Configuration</code> environment variable</li>
 * </ul>
 * 
 * @author Peter Shieh
 * @version 1.0
 */
public interface BuildConstants {

	/** Name of the extraArgument / buildMacro / environment variable. Set to {@value} */
	public static String TARGET_MCU_NAME = "TL5320";

}
