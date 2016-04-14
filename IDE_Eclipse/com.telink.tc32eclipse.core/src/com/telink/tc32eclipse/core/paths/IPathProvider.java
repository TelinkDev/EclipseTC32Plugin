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
 * $Id: IPathProvider.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.paths;

import org.eclipse.core.runtime.IPath;

/**
 * Interface to get the current path from the preference store.
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public interface IPathProvider {

	/**
	 * Gets the currently active path.
	 * 
	 * @return <code>IPath</code> to the active source directory
	 */
	public IPath getPath();

	
}
