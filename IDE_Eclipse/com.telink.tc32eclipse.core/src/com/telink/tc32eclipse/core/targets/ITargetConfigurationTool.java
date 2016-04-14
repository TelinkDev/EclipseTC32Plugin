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
 * $Id: ITargetConfigurationTool.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

import java.util.Set;

import com.telink.tc32eclipse.core.tcdb.TCDBException;

/**
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public interface ITargetConfigurationTool extends IAttributeProvider {

	public String getId();

	public String getName();

	public String getVersion() throws TCDBException;

	public Set<String> getMCUs() throws TCDBException;

	public Set<String> getProgrammers() throws TCDBException;

	public IProgrammer getProgrammer(String id) throws TCDBException;
}
