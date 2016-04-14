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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
//import org.eclipse.swt.widgets.Label;

import com.telink.tc32eclipse.core.properties.TCDBProperties;

/**
* The TCDB Advanced options Tab page.
* <p>
* On this tab, the following properties are edited:
* <ul>
* <li>The automatic verify check</li>
* <li>The Signature check</li>
* <li>Enable the no-Write / Simulation mode</li>
* <li>Inhibit the auto flash erase</li>
* </ul>
* </p>
* 
* @author Peter Shieh
* @since 0.1
* 
*/
public class TabTCDBAdvanced extends AbstractTCDBPropertyTab {

	// The GUI texts

	// No verify group
	//private final static String	GROUP_NOVERIFY		= "Verify Check (-V)";
	//private final static String	LABEL_NOVERIFY		= "Disabling the automatic verify check will improve upload time at the risk of unnoticed upload errors.";
	//private final static String	TEXT_NOVERIFY		= "Disable automatic verify check";

	// No Signature check group
	//private final static String	GROUP_NOSIGCHECK	= "Device Signature Check (-F)";
	//private final static String	LABEL_NOSIGCHECK	= "Enable this if the target MCU has a broken (erased or overwritten) device signature\n"
	//														+ "but is otherwise operating normally.";
	//private final static String	TEXT_NOSIGCHECK		= "Disable device signature check";

	// No write / simulation group
	//private final static String	GROUP_NOWRITE		= "Simulation Mode (-n)";
	//private final static String	LABEL_NOWRITE		= "Note: Even with this option set, TCDB might still perform a chip erase.";
	//private final static String	TEXT_NOWRITE		= "Simulation mode (no data is actually written to the device)";

	// no chip erase cylce group
	private final static String	GROUP_TCDBOPTIONS	= "Telink Loader TCDB Options";
	private final static String	LABEL_TCDBOPTIONS	= "By default, the EVK board is required.";
	private final static String	TEXT_CHIPERASE	    = "Erase before loading firmware";
	private final static String	TEXT_REBOOT	        = "Reboot chip after loading";
	private final static String	TEXT_BOOTBIN	    = "Loading boot.bin instead";
	private final static String	TEXT_USB            = "Use USB interface instead of EVK";

	// The GUI widgets
	//private Button				fNoVerifyButton;

	//private Button				fNoSigCheckButton;

	//private Button				fNoWriteCheck;

	private Button				fChipEraseCheck;
	private Button				fRebootCheck;
	private Button				fUSBCheck;
	private Button				fBootbinCheck;


	/** The Properties that this page works with */
	private TCDBProperties	fTargetProps;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		//addNoVerifySection(parent);

		//addNoSignatureSection(parent);

		//addNoWriteSection(parent);

		addTCDBOptionsSection(parent);

	}

	/**
	 * Add the No Verify check button.
	 * 
	 * @param parent
	 *            <code>Composite</code>

	private void addNoVerifySection(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group.setLayout(new GridLayout(1, false));
		group.setText(GROUP_NOVERIFY);

		Label label = new Label(group, SWT.WRAP);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		label.setText(LABEL_NOVERIFY);
		fNoVerifyButton = setupCheck(group, TEXT_NOVERIFY, 1, SWT.FILL);
	}
	 */
	/**
	 * Add the No Signature Check check button.
	 * 
	 * @param parent
	 *            <code>Composite</code>

	private void addNoSignatureSection(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group.setLayout(new GridLayout(1, false));
		group.setText(GROUP_NOSIGCHECK);

		Label label = new Label(group, SWT.WRAP);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		label.setText(LABEL_NOSIGCHECK);
		//fNoSigCheckButton = setupCheck(group, TEXT_NOSIGCHECK, 1, SWT.FILL);
	}
	 */
	/**
	 * Add the No Write / Simulate check button.
	 * 
	 * @param parent
	 *            <code>Composite</code>

	private void addNoWriteSection(Composite parent) {

		Group group = setupGroup(parent, GROUP_NOWRITE, 1, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		setupLabel(group, LABEL_NOWRITE, 1, SWT.NONE);
		fNoWriteCheck = setupCheck(group, TEXT_NOWRITE, 1, SWT.CHECK);
	}
	 */
	/**
	 * Add the TCDB options button.
	 * 
	 * @param parent
	 *            <code>Composite</code>
	 */
	private void addTCDBOptionsSection(Composite parent) {

		Group group = setupGroup(parent, GROUP_TCDBOPTIONS, 1, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		setupLabel(group, LABEL_TCDBOPTIONS, 1, SWT.NONE);
		fChipEraseCheck = setupCheck(group, TEXT_CHIPERASE, 1, SWT.CHECK);
		fRebootCheck = setupCheck(group, TEXT_REBOOT, 1, SWT.CHECK);
		fUSBCheck = setupCheck(group, TEXT_USB, 0, SWT.CHECK);
		fBootbinCheck = setupCheck(group, TEXT_BOOTBIN, 0, SWT.CHECK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#checkPressed(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	protected void checkPressed(SelectionEvent e) {
		// This is called for all checkbuttons / tributtons which have been set
		// up with the setupXXX() calls

		Control source = (Control) e.widget;
/*
		if (source.equals(fNoVerifyButton)) {
			// No Verify checkbox selected
			boolean noverify = fNoVerifyButton.getSelection();
			fTargetProps.setNoVerify(noverify);

		} else if (source.equals(fNoWriteCheck)) {
			// No Write = Simulation Checkbox has been selected
			// Write the new value to the target properties
			boolean newvalue = fNoWriteCheck.getSelection();
			fTargetProps.setNoWrite(newvalue);

		} else */
		if (source.equals(fChipEraseCheck)) {
			// "Chip Erase" checkbox selected
			boolean newvalue = fChipEraseCheck.getSelection();
			fTargetProps.setChipErase(newvalue);

		} else if (source.equals(fRebootCheck)) {
			// "Reboot" checkbox selected
			boolean newvalue = fRebootCheck.getSelection();
			fTargetProps.setReboot(newvalue);

		} else if (source.equals(fUSBCheck)) {
			// "USB Interface" checkbox selected
			boolean newvalue = fUSBCheck.getSelection();
			fTargetProps.setUSB(newvalue);

		} else if (source.equals(fBootbinCheck)) {
			// "Using Boot.bin" checkbox selected
			boolean newvalue = fBootbinCheck.getSelection();
			fTargetProps.setBootbin(newvalue);

		}

		updateTCDBPreview(fTargetProps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#performApply(de.innot.avreclipse.core.preferences.AVRProjectProperties)
	 */
	@Override
	protected void performApply(TCDBProperties dstprops) {

		if (fTargetProps == null) {
			// updataData() has not been called and this tab has no (modified)
			// settings yet.
			return;
		}

		// Copy the currently selected values of this tab to the given, fresh
		// Properties.
		// The caller of this method will handle the actual saving
		//dstprops.setNoVerify(fTargetProps.getNoVerify());
		//dstprops.setNoSigCheck(fTargetProps.getNoSigCheck());
		//dstprops.setNoWrite(fTargetProps.getNoWrite());
		dstprops.setChipErase(fTargetProps.getChipErase());
		dstprops.setBinary(fTargetProps.getBinary());
		dstprops.setReboot(fTargetProps.getReboot());
		dstprops.setBootbin(fTargetProps.getBootbin());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#performDefaults(de.innot.avreclipse.core.preferences.AVRProjectProperties)
	 */
	@Override
	protected void performCopy(TCDBProperties srcprops) {

		// Reload the items on this page
//		fTargetProps.setNoVerify(srcprops.getNoVerify());
//		fTargetProps.setNoWrite(srcprops.getNoWrite());
		fTargetProps.setChipErase(srcprops.getChipErase());
		fTargetProps.setBinary(srcprops.getBinary());
		fTargetProps.setReboot(srcprops.getReboot());
		fTargetProps.setBootbin(srcprops.getBootbin());
		updateData(fTargetProps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#updateData(de.innot.avreclipse.core.preferences.AVRProjectProperties)
	 */
	@Override
	protected void updateData(TCDBProperties props) {

		fTargetProps = props;

		// Update the GUI widgets on this Tab.
//		fNoVerifyButton.setSelection(fTargetProps.getNoVerify());
		//fNoSigCheckButton.setSelection(fTargetProps.getNoSigCheck());
//		fNoWriteCheck.setSelection(fTargetProps.getNoWrite());
		fChipEraseCheck.setSelection(fTargetProps.getChipErase());
		fRebootCheck.setSelection(fTargetProps.getReboot());
		fUSBCheck.setSelection(fTargetProps.getUSB());
		fBootbinCheck.setSelection(fTargetProps.getBootbin());
	}

}
