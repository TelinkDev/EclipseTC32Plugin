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
 * $Id: SizeFormatApplicabilityCalculator.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.mbs;

import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IOptionApplicability;

//import com.telink.tc32eclipse.core.toolinfo.Size;

/**
 * Calculate which size format options are applicable.
 * 
 * The Size Tool has two options for the format. One with and one without the
 * --format=TC32 option.
 * 
 * Depending on the actual size tool only one option is used, the other is
 * disabled and hidden.
 * 
 * @author Peter Shieh
 * @version 1.0
 * @since 0.1
 * 
 */
public class SizeFormatApplicabilityCalculator implements IOptionApplicability {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.managedbuilder.core.IOptionApplicability#isOptionEnabled(org.eclipse.cdt.managedbuilder.core.IBuildObject,
	 *      org.eclipse.cdt.managedbuilder.core.IHoldsOptions,
	 *      org.eclipse.cdt.managedbuilder.core.IOption)
	 */
	public boolean isOptionEnabled(IBuildObject configuration, IHoldsOptions holder, IOption option) {
		return false; // hasTC32(option) == Size.getDefault().hasTC32Option();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.managedbuilder.core.IOptionApplicability#isOptionUsedInCommandLine(org.eclipse.cdt.managedbuilder.core.IBuildObject,
	 *      org.eclipse.cdt.managedbuilder.core.IHoldsOptions,
	 *      org.eclipse.cdt.managedbuilder.core.IOption)
	 */
	public boolean isOptionUsedInCommandLine(IBuildObject configuration, IHoldsOptions holder,IOption option) {
		return false; 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.managedbuilder.core.IOptionApplicability#isOptionVisible(org.eclipse.cdt.managedbuilder.core.IBuildObject,
	 *      org.eclipse.cdt.managedbuilder.core.IHoldsOptions,
	 *      org.eclipse.cdt.managedbuilder.core.IOption)
	 */
	public boolean isOptionVisible(IBuildObject configuration, IHoldsOptions holder, IOption option) {
		return false; /// hasTC32(option) == Size.getDefault().hasTC32Option();
	}

}

