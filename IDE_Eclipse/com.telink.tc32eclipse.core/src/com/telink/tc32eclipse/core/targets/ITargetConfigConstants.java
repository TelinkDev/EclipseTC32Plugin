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
 * $Id: ITargetConfigConstants.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

import com.telink.tc32eclipse.core.targets.tools.TCDBTool;
import com.telink.tc32eclipse.core.targets.tools.NoneToolFactory;

/**
 * The common attributes of a target configuration and their default values.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public interface ITargetConfigConstants {

	// General (Name and description)
	public final static String	ATTR_NAME				= "name";
	public final static String	DEF_NAME				= "New target";

	public final static String	ATTR_DESCRIPTION		= "description";
	public final static String	DEF_DESCRIPTION			= "";

	// Target Hardware
	public final static String	ATTR_MCU				= "mcu";
	public final static String	DEF_MCU					= "tc32";

	// Programmer device
	public final static String	ATTR_PROGRAMMER_ID		= "programmer";
	public final static String	DEF_PROGRAMMER_ID		= "tcdb";

	// Host interface
	public final static String	ATTR_HOSTINTERFACE		= "hostinterface";
	public final static String	DEF_HOSTINTERFACE		= "USB";

	public final static String	ATTR_PROGRAMMER_PORT	= "port";
	public final static String	DEF_PROGRAMMER_PORT		= "3333";


	// Uploader tool
	public final static String	ATTR_PROGRAMMER_TOOL_ID	= "programmertool";
	public final static String	DEF_PROGRAMMER_TOOL_ID	= TCDBTool.ID;

	// GDBServer tool
	public final static String	ATTR_GDBSERVER_ID		= "gdbservertool";
	public final static String	DEF_GDBSERVER_ID		= NoneToolFactory.ID;

}
