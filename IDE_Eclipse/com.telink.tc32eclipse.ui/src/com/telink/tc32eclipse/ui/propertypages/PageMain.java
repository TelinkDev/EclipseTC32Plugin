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
 * $Id: PageMain.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.ui.propertypages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.prefs.BackingStoreException;

import com.telink.tc32eclipse.TC32Plugin;

/**
 * This is the Main TC32 Property Page.
 * <p>
 * Currently only one item handled by this page: the "per config" flag.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public class PageMain extends AbstractTC32Page {

	private static final String	TEXT_PERCONFIG	= "Enable individual settings for Build Configurations";

	private Button				fPerConfigButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.ui.propertypages.AbstractTC32Page#contentForCDT(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void contentForCDT(Composite composite) {

		// We don't call the superclass, because this page does not use the
		// configuration selection group.

		fPerConfigButton = new Button(composite, SWT.CHECK);
		fPerConfigButton.setText(TEXT_PERCONFIG);
		fPerConfigButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean newvalue = fPerConfigButton.getSelection();
				PageMain.super.setPerConfig(newvalue);
			}
		});

		fPerConfigButton.setSelection(super.isPerConfig());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractPage#performApply()
	 */
	@Override
	public void performApply() {

		// Save the current state of the "per Config flag", and only the flag.
		try {
			fPropertiesManager.save();
		} catch (BackingStoreException e) {
			IStatus status = new Status(IStatus.ERROR, TC32Plugin.PLUGIN_ID,
					"Could not write \"per config\" flag to the preferences.", e);

			ErrorDialog.openError(this.getShell(), "TC32 Main Properties Error", null, status);
			e.printStackTrace();
		}

		// Let the superclass do any additional things.
		super.performApply();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.ui.propertypages.AbstractTC32Page#contributeButtons(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void contributeButtons(Composite parent) {
		// Over-Override this method, because this page does not need the "Copy
		// from Project" Button
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractPage#isSingle()
	 */
	@Override
	protected boolean isSingle() {
		// This page does not use any tabs
		return true;
	}

}
