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
 * $Id: ProjectUpdateConverter.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.mbs.converter;

import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IConvertManagedBuildObject;

/**
 * @author Thomas
 * 
 */
public class ProjectUpdateConverter implements IConvertManagedBuildObject {

	/**
	 * Update a given Project to the latest TC32 Eclipse Plugin settings
	 * 
	 * @author Peter Shieh
	 * 
	 */
	public ProjectUpdateConverter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.managedbuilder.core.IConvertManagedBuildObject#convert(org.eclipse.cdt.managedbuilder.core.IBuildObject,
	 *      java.lang.String, java.lang.String, boolean)
	 */
	public IBuildObject convert(IBuildObject buildObj, String fromId,
			String toId, boolean isConfirmed) {

		// This is currently only called from the CDT ConvertTargetDialog and
		// only for an existing TC32 Eclipse Plugin project.
		
		if (toId.endsWith("0.1.0")) {
			buildObj = Convert21.convert(buildObj, fromId);
		}
		return buildObj;
	}

}
