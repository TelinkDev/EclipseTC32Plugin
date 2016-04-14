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
 * $Id: TabTargetHardware.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.ui.propertypages;

import java.io.IOException;
//import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//import org.eclipse.cdt.build.core.scannerconfig.CfgInfoContext;
//import org.eclipse.cdt.build.internal.core.scannerconfig.CfgDiscoveredPathManager;
//import org.eclipse.cdt.make.core.MakeCorePlugin;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
//import org.eclipse.core.runtime.SubProgressMonitor;
//import org.eclipse.core.runtime.jobs.Job;
//import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.SWTException;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.VerifyEvent;
//import org.eclipse.swt.events.VerifyListener;
//import org.eclipse.swt.graphics.FontMetrics;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.ui.ISharedImages;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.progress.UIJob;

import com.telink.tc32eclipse.TC32Plugin;
//import com.telink.tc32eclipse.core.properties.TCDBProperties;
import com.telink.tc32eclipse.core.properties.TC32ProjectProperties;
//import com.telink.tc32eclipse.core.tcdb.TCDBException;
//import com.telink.tc32eclipse.core.tcdb.TCDBSchedulingRule;
import com.telink.tc32eclipse.core.toolinfo.TCDB;
import com.telink.tc32eclipse.core.toolinfo.GCC;
import com.telink.tc32eclipse.core.util.TC32MCUidConverter;
//import com.telink.tc32eclipse.ui.dialogs.TCDBErrorDialogJob;


/**
 * This tab handles setting of all target hardware related properties.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TabTargetHardware extends AbstractTC32PropertyTab {

//	private static final String		TEXT_LOADBUTTON			= "Load from MCU";
//	private static final String		TEXT_LOADBUTTON_BUSY	= "Loading...";

	
	/** The Properties that this page works with */
	private TC32ProjectProperties	fTargetProps;

	private Combo					fMCUcombo;
//	private Button					fLoadButton;
	private Composite				fMCUWarningComposite;

	private Set<String>				fMCUids;
	private String[]				fMCUNames;

//	private String					fOldMCUid;
//	private static final Image		IMG_WARN				= PlatformUI.getWorkbench()
//																	.getSharedImages()
//																	.getImage(ISharedImages.IMG_OBJS_WARN_TSK);

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.cdt.ui.newui.AbstractCPropertyTab#createControls(org.eclipse.swt.widgets.Composite
	 * )
	 */
	@Override
	public void createControls(Composite parent) {
		super.createControls(parent);
		usercomp.setLayout(new GridLayout(4, false));

		// Get the list of supported MCU id's from the compiler
		// The list is then converted into an array of MCU names
		//
		// If we ever implement per project paths this needs to be moved to the
		// updataData() method to reload the list of supported mcus every time
		// the paths change. The list is added to the combo in addMCUSection().
		if (fMCUids == null) {
			try {
				fMCUids = GCC.getDefault().getMCUList();
			} catch (IOException e) {
				// Could not start avr-gcc. Pop an Error Dialog and continue with an empty list
				IStatus status = new Status(
						IStatus.ERROR,
						TC32Plugin.PLUGIN_ID,
						"Could not execute tc32-elf-gcc. Please check the TC32 installed paths in the preferences.",
						e);
				ErrorDialog.openError(usercomp.getShell(), "tc32-elf-gcc Execution fault", null, status);
				fMCUids = new HashSet<String>();
			}
			String[] allmcuids = fMCUids.toArray(new String[fMCUids.size()]);
			fMCUNames = new String[fMCUids.size()];
			for (int i = 0; i < allmcuids.length; i++) {
				fMCUNames[i] = TC32MCUidConverter.id2name(allmcuids[i]);
			}
			Arrays.sort(fMCUNames);
		}
		addSeparator(usercomp);

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#performApply(de.innot.avreclipse
	 * .core.preferences.AVRConfigurationProperties,
	 * de.innot.avreclipse.core.preferences.AVRConfigurationProperties)
	 */
	@Override
	protected void performApply(TC32ProjectProperties dst) {

		if (fTargetProps == null) {
			// Do nothing if the Target properties do not exist.
			return;
		}


		// Check if a rebuild is required
		//checkRebuildRequired();

		// Now we need to invalidate all discovered Symbols, because they still contain infos about
		// the previous MCU.
		// TODO: check if a rebuild is actually required
		/* 
		if (false) { 
			// This does not work
			IProject project = (IProject) getCfg().getManagedProject().getOwner();
			MakeCorePlugin.getDefault().getDiscoveryManager().removeDiscoveredInfo(project);
			CfgDiscoveredPathManager.getInstance().removeDiscoveredInfo(
					(IProject) getCfg().getManagedProject().getOwner(),new CfgInfoContext(getCfg()));
		}
		*/
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#performDefaults(de.innot.avreclipse
	 * .core.preferences.AVRProjectProperties)
	 */
	@Override
	protected void performCopy(TC32ProjectProperties defaults) {
		fTargetProps.setMCUId(defaults.getMCUId());
		updateData(fTargetProps);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#performOK()
	 */
	@Override
	protected void performOK() {
		// We override this to set the rebuild state as required
		//checkRebuildRequired();
		super.performOK();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#updateData(de.innot.avreclipse
	 * .core.preferences.AVRConfigurationProperties)
	 */
	@Override
	protected void updateData(TC32ProjectProperties cfg) {

		fTargetProps = cfg;

		String mcuid = cfg.getMCUId();
		fMCUcombo.select(fMCUcombo.indexOf(TC32MCUidConverter.id2name(mcuid)));
		checkTCDB(mcuid);



	}

	/**
	 * Check if the given MCU is supported by TCDB and set visibility of the MCU Warning Message
	 * accordingly.
	 * 
	 * @param mcuid
	 *            The MCU id value to test
	 */
	private void checkTCDB(String mcuid) {
		if (TCDB.getDefault().hasMCU(mcuid)) {
			fMCUWarningComposite.setVisible(false);
		} else {
			fMCUWarningComposite.setVisible(true);
		}
	}


	/**
	 * Load the actual MCU from the currently selected Programmer and set the MCU combo accordingly.
	 * <p>
	 * This method will start a new Job to load the values and return immediately.
	 * </p>
	 */
	/*
	private void loadComboFromDevice() {

		// Disable the Load Button. It is re-enabled by the load job when it finishes.
		fLoadButton.setEnabled(false);
		fLoadButton.setText(TEXT_LOADBUTTON_BUSY);

		// The Job that does the actual loading.
		Job readJob = new Job("Reading MCU Signature") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					monitor.beginTask("Starting TCDB", 100);

					final String mcuid = TCDB.getDefault().getAttachedMCU(
							fTargetProps.getTCDBProperties().getProgrammer(),
							new SubProgressMonitor(monitor, 95));

					fTargetProps.setMCUId(mcuid);

					// and update the user interface
					if (!fLoadButton.isDisposed()) {
						fLoadButton.getDisplay().syncExec(new Runnable() {
							public void run() {
								updateData(fTargetProps);

								// Check if supported by TCDB and set the errorpane as
								// required
								checkTCDB(mcuid);


								// Set the rebuild flag for the configuration
								getCfg().setRebuildState(true);

							}
						});
					}
					monitor.worked(5);
				} catch (TCDBException ade) {
					// Show an Error message and exit
					if (!fLoadButton.isDisposed()) {
						UIJob messagejob = new TCDBErrorDialogJob(fLoadButton.getDisplay(), ade,
								fTargetProps.getTCDBProperties().getProgrammerId());
						messagejob.setPriority(Job.INTERACTIVE);
						messagejob.schedule();
						try {
							messagejob.join(); // block until the dialog is closed.
						} catch (InterruptedException e) {
							// Don't care if the dialog is interrupted from outside.
						}
					}
				} catch (SWTException swte) {
					// The display has been disposed, so the user is not
					// interested in the results from this job
					return Status.CANCEL_STATUS;
				} finally {
					monitor.done();
					// Enable the Load from MCU Button
					if (!fLoadButton.isDisposed()) {
						fLoadButton.getDisplay().syncExec(new Runnable() {
							public void run() {
								// Re-Enable the Button
								fLoadButton.setEnabled(true);
								fLoadButton.setText(TEXT_LOADBUTTON);
							}
						});
					}
				}

				return Status.OK_STATUS;
			}
		};

		// now set the Job properties and start it
		readJob.setRule(new TCDBSchedulingRule(fTargetProps.getTCDBProperties()
				.getProgrammer()));
		readJob.setPriority(Job.SHORT);
		readJob.setUser(true);
		readJob.schedule();
	}
	
	*/
}
