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
 * $Id: PageTargetHardware.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.ui.propertypages;

/**
 * The Target Hardware property page.
 * <p>
 * This page uses only one single tab
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class PageTargetHardware extends AbstractTC32Page {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractPage#isSingle()
	 */
	@Override
	protected boolean isSingle() {
		return true;
	}

}
