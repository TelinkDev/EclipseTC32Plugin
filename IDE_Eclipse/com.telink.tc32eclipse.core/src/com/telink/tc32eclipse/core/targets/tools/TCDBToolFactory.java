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
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets.tools;

import com.telink.tc32eclipse.core.targets.ITargetConfiguration;
import com.telink.tc32eclipse.core.targets.ITargetConfigurationTool;
import com.telink.tc32eclipse.core.targets.IToolFactory;
import com.telink.tc32eclipse.core.targets.ToolManager;

/**
 * @author Peter Shieh
 * @since
 * 
 */
public class TCDBToolFactory implements IToolFactory {

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IToolFactory#getId()
	 */
	public String getId() {
		return TCDBTool.ID;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IToolFactory#getName()
	 */
	public String getName() {
		return TCDBTool.NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IToolFactory#isType(java.lang.String)
	 */
	public boolean isType(String tooltype) {


		if (ToolManager.TC32PROGRAMMERTOOL.equals(tooltype)) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.IToolFactory#createTool(com.telink.tc32eclipse.core.targets
	 * .ITargetConfiguration)
	 */
	public ITargetConfigurationTool createTool(ITargetConfiguration hc) {
		return new TCDBTool(hc);
	}

}
