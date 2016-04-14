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
 *******************************************************************************/
package com.telink.tc32eclipse.ui.propertypages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
* The TCDB dummy page for static library projects.
* <p>
* TCDB support does not make sense for static library projects. But to keep the UI consistent a
* dummy page is shown informing the user about this.
* </p>
* 
* @author Peter Shieh
* @since 1.0
* 
*/
public class PageTCDBStaticLib extends AbstractTC32Page {
	
	private static Image fImage = null;

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.ui.newui.AbstractPage#createWidgets(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createWidgets(Composite c) {

		Composite compo = new Composite(c, SWT.NONE);
		compo.setLayout(new GridLayout(2, false));

		Label icon = new Label(compo, SWT.NONE);
		icon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		icon.setImage(getInfoImage(c));

		Label label = new Label(compo, SWT.BOLD);
		label.setText("TCDB is not supported for Static library projects.");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.cdt.ui.newui.AbstractPage#isSingle()
	 */
	@Override
	protected boolean isSingle() {

		// This page uses no tabs

		return true;
	}

	/**
	 * Get the Information Icon Image..
	 * 
	 * @return image the image
	 */
	private Image getInfoImage(Composite c) {
		if (fImage != null) {
			return fImage;
		}
		Display display = c.getDisplay();
		fImage = display.getSystemImage(SWT.ICON_INFORMATION);
		return fImage;
	}

}
