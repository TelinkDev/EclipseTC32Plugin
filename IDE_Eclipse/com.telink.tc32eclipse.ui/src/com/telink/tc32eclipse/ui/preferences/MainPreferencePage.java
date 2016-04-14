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
 * $Id: MainPreferencePage.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Main Preference page of the TC32 Eclipse plugin.
 * 
 * <p>
 * For the time being this page is empty and will only contain descriptions of the subpages. This is
 * against the recommendation of the Eclipse Style Guide, but I currently do not have any plugin
 * global settings which should be on the main page.
 * </p>
 * 
 * @author Peter Shieh
 * @since 2.3
 */

public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public MainPreferencePage() {
		super();

		setDescription("TC32 Eclipse Plugin Preferences");
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite content = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		content.setLayout(layout);
		content.setFont(parent.getFont());

		Label filler = new Label(content, SWT.NONE);
		filler.setText("");

		Label label = createDescriptionLabel(content);
		label
				.setText("Please select one of the sub-pages to change the settings for the TC32 plugin.");

		filler = new Label(content, SWT.NONE);
		filler.setText("");

		createNoteComposite(JFaceResources.getDialogFont(), content, "TCDB:",
				"Manage the configuration of TCDB Binary Loader.\n");

		createNoteComposite(JFaceResources.getDialogFont(), content, "Paths:",
				"Manage the paths to the external tools and files used by the plugin.\n");

		return content;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// nothing to init
	}

}
