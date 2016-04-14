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
 * $Id: TC32GCCScannerInfoCollector.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.mbs.scannerconfig;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.make.core.scannerconfig.IScannerInfoCollector3;
import org.eclipse.cdt.make.core.scannerconfig.ScannerInfoTypes;
import org.eclipse.cdt.make.internal.core.scannerconfig2.GCCSpecsRunSIProvider;
import org.eclipse.cdt.make.internal.core.scannerconfig2.PerProjectSICollector;
import org.eclipse.cdt.managedbuilder.scannerconfig.IManagedScannerInfoCollector;
//import org.eclipse.core.resources.IProject;

//import com.telink.tc32eclipse.core.properties.TC32ProjectProperties;
//import com.telink.tc32eclipse.core.properties.ProjectPropertyManager;

/**
 * Gather built in compiler settings.
 * <p>
 * This extends {@link PerProjectSICollector} to add the "-mmcu" option to the
 * {@link GCCSpecsRunSIProvider} compiler arguments.
 * </p>
 * <p>
 * With this the ScannerInfoProvider will get the correct #defines for the selected TC32 Target. The
 * MCU info is gathered from the project (not the build configuration)
 * </p>
 * 
 */
@SuppressWarnings("restriction")
public class TC32GCCScannerInfoCollector extends PerProjectSICollector implements
		IScannerInfoCollector3, IManagedScannerInfoCollector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.make.internal.core.scannerconfig2.PerProjectSICollector#getCollectedScannerInfo(java.lang.Object,
	 *      org.eclipse.cdt.make.core.scannerconfig.ScannerInfoTypes)
	 */
	@Override
	public List<String> getCollectedScannerInfo(Object resource, ScannerInfoTypes type) {

		if (!type.equals(ScannerInfoTypes.TARGET_SPECIFIC_OPTION)) {
			return super.getCollectedScannerInfo(resource, type);
		}

		if (getDefinedSymbols().size() == 0) {
			return null;
		}
		List<String> rv = new ArrayList<String>(1);

	
		return rv;
	}

}
