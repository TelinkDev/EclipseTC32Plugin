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
 * $Id: ITargetConfigChangeListener.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

import java.util.EventListener;

/**
 * Listener for Target Configuration changes.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public interface ITargetConfigChangeListener extends EventListener {

	/**
	 * Notification that a Target Configuration attribute has changed.
	 * <p>
	 * This method gets called when any attribute of the observed target configuration is modified.
	 * </p>
	 * 
	 * @param config
	 *            The <code>TargetConfiguration</code> which has changed
	 * @param name
	 *            the name of the changed attribute
	 * @param oldValue
	 *            the old value, or <code>null</code> if not known or not relevant
	 * @param newValue
	 *            the new value, or <code>null</code> if not known or not relevant
	 */
	public void attributeChange(ITargetConfiguration config, String attribute, String oldvalue,
			String newvalue);

}
