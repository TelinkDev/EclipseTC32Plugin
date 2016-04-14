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
 * $Id: IAttributeProvider.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

import com.telink.tc32eclipse.core.targets.ITargetConfiguration.ValidationResult;

/**
 * @author Peter Shieh
 * @since
 * 
 */
public interface IAttributeProvider {

	public String[] getAttributes();

	public String getDefaultValue(String attribute);

	public ValidationResult validate(String attribute);

}
