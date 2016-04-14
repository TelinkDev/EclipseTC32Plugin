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
 * $Id: BaseToolInfo.java 851 20.1.08-07 19:37:00Z innot $
 *******************************************************************************/
/**
 * 
 */
package com.telink.tc32eclipse.core.toolinfo;

import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.runtime.IPath;

/**
 * @author U043192
 * 
 */
public abstract class BaseToolInfo {

	private String	fCommandName	= null;

	protected BaseToolInfo(String toolid) {
		// First: Get the command name from the toolchain
		ITool tool = ManagedBuildManager.getExtensionTool(toolid);
		if (tool != null) {
			fCommandName = tool.getToolCommand();
			if (fCommandName.startsWith("-")) {
				// remove leading "-" in command name
				// (used to suppress "make" exit on errors)
				fCommandName = fCommandName.substring(1);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.toolinfo.IToolInfo#getToolFullPath()
	 */
	public IPath getToolFullPath() {
		// Base implementation. Override as necessary.
		return null;
	}

	public String getCommandName() {
		return fCommandName;
	}
}
