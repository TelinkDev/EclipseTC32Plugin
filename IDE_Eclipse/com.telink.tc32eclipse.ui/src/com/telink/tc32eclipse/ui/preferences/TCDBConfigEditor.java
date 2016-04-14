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

package com.telink.tc32eclipse.ui.preferences;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.core.targets.IProgrammer;
import com.telink.tc32eclipse.core.tcdb.TCDBException;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfig;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfigManager;
import com.telink.tc32eclipse.core.toolinfo.TCDB;
import com.telink.tc32eclipse.core.toolinfo.TCDB.ConfigEntry;
import com.telink.tc32eclipse.ui.dialogs.TCDBErrorDialog;

/**
 * Dialog to edit a TCDB Programmer Configuration.
 * <p>
 * This dialog is self contained and is used to edit a TCDB Programmer configuration. Only the
 * TCDB options specific to a programmer are included, as defined in the {@link ProgrammerConfig}
 * class.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * @since 2.3 Added optional post TCDB invocation delay
 * 
 */
public class TCDBConfigEditor extends StatusDialog {

	/** The working copy of the given source Configuration */
	private final ProgrammerConfig			fConfig;

	/** Map of all Programmer IDs to their ConfigEntry. */
	private Map<String, IProgrammer>	fConfigIDMap;

	/** Map of all Programmer names to their ConfigEntry */
	private Map<String, IProgrammer>	fConfigNameMap;

	/**
	 * List of all existing configurations to avoid duplicate names (configuration names need to be
	 * unique)
	 */
	private final Set<String>				fAllConfigs;

	private Text							fPreviewText;

	/**
	 * Constructor for a new Configuration Editor.
	 * <p>
	 * The passed <code>ProgrammerConfig</code> is copied and not touched. The modified
	 * <code>ProgrammerConfig</code> can be retrieved with the {@link #getResult()} method.
	 * </p>
	 * <p>
	 * The Set of all known configurations is required to prevent duplicate names.
	 * </p>
	 * 
	 * @param parent
	 *            Parent <code>Shell</code>
	 * @param config
	 *            The <code>ProgrammerConfig</code> to edit. It is copied and not modified directly.
	 * @param allconfigs
	 *            A <code>Set&lt;String&gt;</code> of all known configuration names.
	 */
	/**
	 * @param parent
	 */
	public TCDBConfigEditor(Shell parent, ProgrammerConfig config, Set<String> allconfigs) {
		super(parent);

		setTitle("Edit TCDB Programmer Configuration " + config.getName());

		// Allow this dialog to be resizeable
		setShellStyle(getShellStyle() | SWT.RESIZE);

		// make a copy of the given Configuration that we can modify as required
		fConfig = ProgrammerConfigManager.getDefault().getConfigEditable(config);

		// Remove the current name from the list of all names
		fAllConfigs = allconfigs;
		if (fAllConfigs.contains(fConfig.getName())) {
			fAllConfigs.remove(fConfig.getName());
		}

		try {

			// Get the List of TCDB Programmer ConfigEntries.
			// They are used to build the List of Programmers and to show
			// details of
			// a selected programmer
			Collection<IProgrammer> programmers = TCDB.getDefault().getProgrammersList();
			fConfigIDMap = new HashMap<String, IProgrammer>(programmers.size());
			fConfigNameMap = new HashMap<String, IProgrammer>(programmers.size());
			for (IProgrammer type : programmers) {
				fConfigIDMap.put(type.getId(), type);
				fConfigNameMap.put(type.getDescription(), type);
			}
		} catch (TCDBException e) {
			TCDBErrorDialog.openTCDBError(getShell(), e, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		// Create the actual layout with all controls in a three column Layout.
		// The third column is currently unused, but may be used in the future
		// to add a "Browse..." Button to the Port option.
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(3, false));

		addNameControl(composite);

		addDescriptionControl(composite);

		addProgrammersComposite(composite);

		addPortControl(composite);


		addCommandlinePreview(composite);

		updateCommandPreview();

		return composite;
	}

	/**
	 * Adds the configuration name control.
	 * <p>
	 * This control edits the <code>ProgrammerConfig<code> name property.
	 * </p>
	 * <p>
	 * The entered name is checked and an error message is shown if the name is either empty or
	 * already exists for another configuration. Also slashes '/' are filtered out, as they can not
	 * be used for names.
	 * </p>
	 * 
	 * @param parent
	 */
	private void addNameControl(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Configuration name");
		final Text name = new Text(parent, SWT.BORDER);
		name.setText(fConfig.getName());
		name.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

		// Add a modify Listener to update the configuration for any name
		// changes
		name.addModifyListener(new ModifyListener() {
			/*
			 * (non-Javadoc)
			 * @see
			 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				// Upon a modify event check that the name is not empty and
				// is not already taken by another configuration.
				// Either case will generate an error message which will
				// disable the OK button.
				String newname = name.getText();
				fConfig.setName(newname);
				if (newname.length() == 0) {
					Status status = new Status(Status.ERROR, "TCDB",
							"Configuration name may not be empty", null);
					TCDBConfigEditor.this.updateStatus(status);
				} else if (fAllConfigs.contains(newname)) {
					Status status = new Status(Status.ERROR, "TCDB",
							"Configuration with the same name already exists", null);
					TCDBConfigEditor.this.updateStatus(status);
				} else {
					TCDBConfigEditor.this.updateStatus(Status.OK_STATUS);
				}
			}
		});

		// Add a Verify Listener to suppress slashes ('/')
		name.addVerifyListener(new VerifyListener() {
			/*
			 * (non-Javadoc)
			 * @see
			 * org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
			 */
			public void verifyText(VerifyEvent event) {
				String text = event.text;
				if (text.indexOf('/') != -1) {
					event.doit = false;
				}
			}
		});
	}

	/**
	 * Adds the configuration description control.
	 * <p>
	 * This control edits the <code>ProgrammerConfig<code> description property.
	 * </p>
	 * 
	 * @param parent
	 */
	private void addDescriptionControl(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Description");
		final Text description = new Text(parent, SWT.BORDER);
		description.setText(fConfig.getDescription());
		description.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		description.addModifyListener(new ModifyListener() {
			/*
			 * (non-Javadoc)
			 * @see
			 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				String newdescription = description.getText();
				fConfig.setDescription(newdescription);
			}
		});
	}

	/**
	 * Adds the Programmers selection controls.
	 * <p>
	 * This composite edits the <code>ProgrammerConfig<code> programmer property.
	 * </p>
	 * <p>
	 * It consists of a List with all available programmers and a Textbox showing the definition of
	 * the selected programmer from the TCDB configuration file These two controls are arranged
	 * in a SashForm and wrapped in a Group.
	 * </p>
	 * 
	 * @param parent
	 */
	private void addProgrammersComposite(Composite parent) {

		Group listgroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		listgroup.setText("Programmer Hardware (-c)");
		listgroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		FillLayout fl = new FillLayout();
		fl.marginHeight = 5;
		fl.marginWidth = 5;
		listgroup.setLayout(fl);

		SashForm sashform = new SashForm(listgroup, SWT.HORIZONTAL);
		sashform.setLayout(new GridLayout(2, false));

		final List list = new List(sashform, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		String[] allprogrammers = getProgrammers();
		list.setItems(allprogrammers);

		Composite devicedetails = new Composite(sashform, SWT.NONE);
		devicedetails.setLayout(new GridLayout());
		devicedetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		final Text fromtext = new Text(devicedetails, SWT.NONE);
		fromtext.setEditable(false);
		fromtext.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		final Text details = new Text(devicedetails, SWT.MULTI | SWT.BORDER);
		details.setEditable(false);
		details.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		list.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * @seeorg.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.
			 * SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				String devicename = list.getItem(list.getSelectionIndex());
				IProgrammer type = fConfigNameMap.get(devicename);
				fConfig.setProgrammer(type.getId());
				updateDetails(type, fromtext, details);
				updateCommandPreview();
			}
		});
		String programmer = fConfig.getProgrammer();
		IProgrammer type = fConfigIDMap.get(programmer);
		if (programmer.length() != 0) {
			list.select(list.indexOf(type.getDescription()));
			updateDetails(type, fromtext, details);
		}

		sashform.pack();
	}

	/**
	 * Add the configuration port control.
	 * 
	 * @param parent
	 */
	private void addPortControl(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Override default port (-P)");
		final Text port = new Text(parent, SWT.BORDER);
		//port.setText(fConfig.getPort());
		port.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
		port.addModifyListener(new ModifyListener() {
			/*
			 * (non-Javadoc)
			 * @see
			 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				//String newport = port.getText();
				//fConfig.setPort(newport);
				updateCommandPreview();
			}
		});
	}



	/**
	 * Add a Preview Control.
	 * <p>
	 * The preview control shows the current TCDB options commandline. The content is set in the
	 * {@link #updateCommandPreview()} method.
	 * </p>
	 * 
	 * @param parent
	 */
	private void addCommandlinePreview(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Command line preview");
		fPreviewText = new Text(parent, SWT.BORDER);
		fPreviewText.setEditable(false);
		fPreviewText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
	}

	/**
	 * Get the results from this dialog.
	 * <p>
	 * It will return a ProgrammerConfig with the updated items.
	 * </p>
	 * <p>
	 * This should only be called when <code>open()</code> returned <code>OK</code> (OK Button
	 * clicked). Otherwise canceled changes will be returned.
	 * </p>
	 * 
	 * @return The ProgrammerConfig with the modified values.
	 */
	public ProgrammerConfig getResult() {
		return fConfig;
	}

	/**
	 * Gets a list of all Programmer names.
	 * 
	 * @return an Array of <code>String</code> with the names of all known Programmers, sorted
	 *         alphabetically
	 */
	private String[] getProgrammers() {

		Set<String> nameset = fConfigNameMap.keySet();
		String[] allnames = nameset.toArray(new String[nameset.size()]);
		Arrays.sort(allnames, String.CASE_INSENSITIVE_ORDER);
		return allnames;
	}

	/**
	 * Update the Preview Text to show the current configuration as an TCDB options commandline.
	 */
	private void updateCommandPreview() {

		java.util.List<String> arglist = fConfig.getArguments();
		// make a String of all arguments
		StringBuffer sb = new StringBuffer("TCDB ");
		for (String argument : arglist) {
			sb.append(argument).append(" ");
		}
		sb.append(" [...part specific options...]");
		fPreviewText.setText(sb.toString());
	}

	/**
	 * Update the Programmers detail area
	 * 
	 * @param entry
	 *            The <code>ConfigEntry</code> for the selected programmer
	 * @param from
	 *            The <code>Text</code> control for the filename
	 * @param details
	 *            The multiline <code>Text</code> control for the details
	 */
	private void updateDetails(IProgrammer type, Text from, Text details) {
		ConfigEntry entry;
		try {
			entry = TCDB.getDefault().getProgrammerInfo(type.getId());
		} catch (TCDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			from.setText("Error reading TCDB.conf file");
			return;
		}
		from.setText("Programmer details from [" + entry.configfile.toOSString() + ":"
				+ entry.linenumber + "]");
		Job job = new UpdateDetailsJob(entry, details);
		job.setSystem(true);
		job.setPriority(Job.SHORT);
		job.schedule();
	}

	/**
	 * Internal Job to update the programmer detail area.
	 * <p>
	 * This is done as a Job, because calls to {@link TCDB#getConfigDetailInfo(ConfigEntry)} can
	 * take some time to load the TCDB configuration file (currently at 429KByte).
	 * </p>
	 * <p>
	 * The job is instantiated with the ConfigEntry for which to display the details and the
	 * multiline Text control into which to print the data.
	 * </p>
	 * 
	 * @see TCDB#getConfigDetailInfo(ConfigEntry)
	 * 
	 */
	private static class UpdateDetailsJob extends Job {

		private final ConfigEntry	fConfigEntry;
		private final Text			fTextControl;

		/**
		 * @param entry
		 *            The <code>ConfigEntry</code> for which to display the details
		 * @param textcontrol
		 *            The multiline <code>Text</code> control for the details
		 */
		public UpdateDetailsJob(ConfigEntry entry, Text textcontrol) {
			super("Loading programmer details");
			fConfigEntry = entry;
			fTextControl = textcontrol;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				monitor.beginTask("Retrieving programmer info", 1);
				if (fTextControl.isDisposed()) {
					return Status.CANCEL_STATUS;
				}
				// Get the preformatted info String from the TCDB class and
				// update the details Text control in the UI thread.
				final String content = TCDB.getDefault().getConfigDetailInfo(fConfigEntry);
				Display display = fTextControl.getDisplay();
				if (display != null && !display.isDisposed()) {
					display.syncExec(new Runnable() {
						public void run() {
							fTextControl.setText(content);
						}
					});
				}
				monitor.worked(1);
			} catch (IOException ioe) {
				// If TCDB is working at all, the configuration
				// file should be readable as well. So there should
				// be no IOExceptions.
				// But just in case we log the Error
				Status status = new Status(Status.ERROR, TC32Plugin.PLUGIN_ID,
						"Can't access TCDB configuration file "
								+ fConfigEntry.configfile.toOSString(), ioe);
				TC32Plugin.getDefault().log(status);
			} finally {
				monitor.done();
			}
			return Status.OK_STATUS;
		}
	}
}
