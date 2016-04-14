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
 * $Id: IToolFactory.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

/**
 * A tool factory can produce hardware configuration tools for a hardware configuration.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public interface IToolFactory {

	/**
	 * Get the id of the tools created by this factory.
	 * 
	 * @return tool id
	 */
	public String getId();

	/**
	 * Get the name of the tool created by this factory.
	 * <p>
	 * This is the same as <code>createTool().getName()</code>, but without needing a target
	 * configuration.
	 * </p>
	 * 
	 * @return Name of the tool.
	 */
	public String getName();

	/**
	 * Checks if the factory can produce tools of the given type.
	 * 
	 * @param tooltype
	 * @return
	 */
	public boolean isType(String tooltype);

	/**
	 * Create a new tool for the given hardware configuration.
	 * 
	 * @param tc
	 *            Reference to the hardware configuration that the tool belongs to.
	 * @return
	 */
	public ITargetConfigurationTool createTool(ITargetConfiguration tc);

}
