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
 * $Id: TargetHardwareOptionsHandler.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.mbs;

import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler;

/**
 * Handle changes of target hardware options.
 * 
 * <p>
 * This class is registered as a <code>valueHandler</code> by the options of
 * the base toolchain in the TC32 Eclipse plugin. All changes to the option
 * implementing this handler (currently "Target MCU" )
 * will cause a call to the <code>handleValue</code> method of this class.
 * </p>
 * <p>
 * All options of all tools of the toolchain are examined and if the last part
 * of their id is equal to <code>valueHandlerExtraArgument</code>, then their
 * value is set to the value of this option.
 * </p>
 * <p>
 * The value of the <code>valueHandlerExtraArgument</code> attribute in the
 * option element is used as the name of the buildMacro / Configuration
 * environment variable to be set, while the value field of the option is used
 * as the value.
 * </p>
 * <p>
 * The {@link BuildConstants#TARGET_MCU_NAME} name is handled specially by
 * extracting the MCU Type from the id in the value field.
 * </p>
 * 
 * <p>
 * This class is extended from
 * {@link org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler}, which
 * covers the other methods of the
 * {@link org.eclipse.cdt.managedbuilder.core.IManagedOptionValueHandler}
 * interface.
 * </p>
 * 
 * @author Peter Shieh
 * @version 1.0
 * 
 * @see com.telink.tc32eclipse.mbs.TC32TargetBuildMacroSupplier
 * @see com.telink.tc32eclipse.mbs.TC32TargetEnvvarSupplier
 * 
 */
public class TargetHardwareOptionsHandler extends ManagedOptionValueHandler
		implements BuildConstants {

	/**
	 * Handle Option Change events.
	 * 
	 * <p>
	 * Any change of the this option is immediately passed onto all other
	 * options of the given Toolchain with an id that contains
	 * <code>valueHandlerExtraArgument</code>
	 * </p>
	 * 
	 * @see org.eclipse.cdt.managedbuilder.core.IManagedOptionValueHandler#handleValue(org.eclipse.cdt.managedbuilder.core.IBuildObject,
	 *      org.eclipse.cdt.managedbuilder.core.IHoldsOptions,
	 *      org.eclipse.cdt.managedbuilder.core.IOption, java.lang.String, int)
	 */
	@Override
	public boolean handleValue(IBuildObject configuration,
			IHoldsOptions holder, IOption option, String extraArgument,
			int event) {

		return false;
		
	}
}
