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
 * $Id: SectionAvarice.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.ui.editors.targets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Peter Shieh
 * @since
 * 
 */
public class SectionPCDB extends AbstractTCSectionPart {

	/*
	 * (non-Javadoc)
	 * @see
	 * de.innot.avreclipse.ui.editors.targets.AbstractTCSectionPart#createSectionContent(org.eclipse
	 * .swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createSectionContent(Composite parent, FormToolkit toolkit) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see de.innot.avreclipse.ui.editors.targets.AbstractTCSectionPart#getPartAttributes()
	 */
	@Override
	protected String[] getPartAttributes() {
		// TODO Auto-generated method stub
		return new String[] {};
	}

	/*
	 * (non-Javadoc)
	 * @see de.innot.avreclipse.ui.editors.targets.AbstractTCSectionPart#getTitle()
	 */
	@Override
	protected String getTitle() {
		return "TCDB";
	}

	/*
	 * (non-Javadoc)
	 * @see de.innot.avreclipse.ui.editors.targets.AbstractTCSectionPart#refreshSectionContent()
	 */
	@Override
	protected void refreshSectionContent() {
		// TODO Auto-generated method stub

	}

}
