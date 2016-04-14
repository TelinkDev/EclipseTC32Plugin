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
 * $Id: PathsPreferencePage.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.telink.tc32eclipse.core.preferences.TC32PathsPreferences;

/**
 * Paths Preference page of the TC32 Eclipse plugin.
 * <p>
 * This page manages two preferences:
 * <ul>
 * <li>The "no scan at startup" flag to inhibit the background scan for changed system paths.</li>
 * <li>The path settings for all paths required by the plugin.</li>
 * </ul>
 * </p>
 * <p>
 * Most of the real work of path management is done in the {@link TC32PathsFieldEditor} included on
 * this page.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */

public class PathsPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private IPreferenceStore	fPreferenceStore	= null;

	public PathsPreferencePage() {
		super(GRID);

		// Get the instance scope path preference store
		fPreferenceStore = TC32PathsPreferences.getPreferenceStore();
		setPreferenceStore(fPreferenceStore);
		setDescription("Path Settings for the TC32 Eclipse Plugin");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	public void createFieldEditors() {

		// This page has two fields:
		// The first one to inhibit the startup search for changed system paths
		// The second to edit all paths.

		Composite parent = getFieldEditorParent();

		Label filler = new Label(parent, SWT.NONE);
		filler.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

		// Startup search inhibit

		BooleanFieldEditor autoScanBoolean = new BooleanFieldEditor(
				TC32PathsPreferences.KEY_NOSTARTUPSCAN,
				"Disable search for system paths at startup", BooleanFieldEditor.DEFAULT, parent);
		addField(autoScanBoolean);

		Composite note = createNoteComposite(JFaceResources.getDialogFont(), parent, "Note:",
				"If disabled, a manual rescan may be required when a new tc32-elf-tcctoolchain has been installed.\n");
		note.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

		filler = new Label(parent, SWT.NONE);
		filler.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

		// Path editor field control.

		TC32PathsFieldEditor pathEditor = new TC32PathsFieldEditor(parent);
		addField(pathEditor);

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
