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

import java.util.HashSet;
import java.util.Set;

//import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.VerifyEvent;
//import org.eclipse.swt.events.VerifyListener;
//import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.telink.tc32eclipse.core.properties.TCDBProperties;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfig;
import com.telink.tc32eclipse.ui.preferences.TCDBConfigEditor;

/**
 * The main / general TCDB options tab.
 * <p>
 * On this tab, the following properties are edited:
 * <ul>
 * <li>TCDB Programmer Configuration, incl. buttons to edit the current
 * config or add a new config</li>
 * <li>The JTAG BitClock</li>
 * <li>The BitBanger bit change delay</li>
 * </ul>
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TabTCDBProgrammer extends AbstractTCDBPropertyTab {

	// The GUI texts
	// Programmer config selection group
	private final static String GROUP_PROGCONFIG = "Programmer configuration";
	private final static String TEXT_EDITBUTTON = "Edit...";
	private final static String TEXT_NEWBUTTON = "New...";
	private final static String LABEL_CONFIG_WARNING = "The Programmer configuration previously associated with this project/configuration\n"
	        + "does not exist anymore. Please select a different one.";
	private final static String LABEL_NOCONFIG = "Please select a Programmer Configuration to enable TCDB functions";

	// JTAG Bitclock group
	//private final static String GROUP_BITCLOCK = "JTAG ICE BitClock";
	//private final static String LABEL_BITCLOCK = "Specify the bit clock period in microseconds for the JTAG interface or the ISP clock (JTAG ICE only).\n"
	//        + "Set this to > 1.0 for target MCUs running with less than 4MHz on a JTAG ICE.\n"
	//        + "Leave the field empty to use the preset bit clock period of the selected Programmer.";
	//private final static String TEXT_BITCLOCK = "JTAG ICE bitclock";
	//private final static String LABEL_BITCLOCK_UNIT = "µs";

	// BitBang delay group
	
	// The GUI widgets
	private Combo fProgrammerCombo;
	private Label fConfigWarningIcon;
	private Label fConfigWarningMessage;


	/** The Properties that this page works with */
	private TCDBProperties fTargetProps;

	/** Warning image used for invalid Programmer Config values */
	private static final Image IMG_WARN = PlatformUI.getWorkbench().getSharedImages().getImage(
	        ISharedImages.IMG_OBJS_WARN_TSK);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControls(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		addProgrammerConfigSection(parent);

		//addBitClockSection(parent);

		//addBitBangDelaySection(parent);

	}

	/**
	 * Add the Programmer Configuration selection <code>Combo</code> and the
	 * "Edit", "New" Buttons.
	 * 
	 * @param parent
	 *            <code>Composite</code>
	 */
	private void addProgrammerConfigSection(Composite parent) {

		Group configgroup = setupGroup(parent, GROUP_PROGCONFIG, 3, SWT.NONE);
		configgroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		fProgrammerCombo = new Combo(configgroup, SWT.READ_ONLY);
		fProgrammerCombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		fProgrammerCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedname = fProgrammerCombo
				        .getItem(fProgrammerCombo.getSelectionIndex());
				String selectedid = getProgrammerConfigId(selectedname);
				fTargetProps.setProgrammerId(selectedid);
				showProgrammerWarning("", false);
				updateTCDBPreview(fTargetProps);
			}
		});
		// Init the combo with the list of available programmer configurations
		loadProgrammerConfigs();

		// Edit... Button
		Button editButton = setupButton(configgroup, TEXT_EDITBUTTON, 1, SWT.NONE);
		editButton.setBackground(parent.getBackground());
		editButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editButtonAction(false);
			}
		});

		// New... Button
		Button newButton = setupButton(configgroup, TEXT_NEWBUTTON, 1, SWT.NONE);
		newButton.setBackground(parent.getBackground());
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editButtonAction(true);
			}
		});

		// The Warning icon / message composite
		Composite warningComposite = new Composite(configgroup, SWT.NONE);
		warningComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 3, 1));
		GridLayout gl = new GridLayout(2, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.verticalSpacing = 0;
		gl.horizontalSpacing = 0;
		warningComposite.setLayout(gl);

		fConfigWarningIcon = new Label(warningComposite, SWT.LEFT);
		fConfigWarningIcon.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		fConfigWarningIcon.setImage(IMG_WARN);

		fConfigWarningMessage = new Label(warningComposite, SWT.LEFT);
		fConfigWarningMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fConfigWarningMessage.setText("two-line\ndummy");

		// By default make the warning invisible
		// updateData() will make it visible when required
		fConfigWarningIcon.setVisible(false);
		fConfigWarningMessage.setVisible(false);

	}


	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#performApply(de.innot.avreclipse.core.preferences.AVRProjectProperties)
	 */
	@Override
	protected void performApply(TCDBProperties dstprops) {

		// Save all new / modified programmer configurations
		saveProgrammerConfigs();

		// Copy the currently selected values of this tab to the given, fresh
		// Properties.
		// The caller of this method will handle the actual saving
		dstprops.setProgrammerId(fTargetProps.getProgrammerId());
		//dstprops.setBitclock(fTargetProps.getBitclock());
		//dstprops.setBitBangDelay(fTargetProps.getBitBangDelay());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#performDefaults()
	 */
	@Override
	protected void performDefaults() {

		// Reset the list of Programmer Configurations
		loadProgrammerConfigs();

		// The other defaults related stuff is done in the performCopy() method,
		// which is called later by the superclass.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractAVRPropertyTab#performDefaults(de.innot.avreclipse.core.preferences.AVRProjectProperties)
	 */
	@Override
	protected void performCopy(TCDBProperties srcprops) {

		// Reload the items on this page
		fTargetProps.setProgrammerId(srcprops.getProgrammerId());
		//fTargetProps.setBitclock(srcprops.getBitclock());
		//fTargetProps.setBitBangDelay(srcprops.getBitBangDelay());
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

		// Set the selection of the Programmercombo
		// If the programmerid of the target properties does not exist,
		// show a warning and select the first item (without copying it into the
		// Target Properties)
		String programmerid = fTargetProps.getProgrammerId();
		if (programmerid.length() == 0) {
			// No Programmer has been set yet
			// Deselect the combo and show a Message
			fProgrammerCombo.deselect(fProgrammerCombo.getSelectionIndex());
			showProgrammerWarning(LABEL_NOCONFIG, false);
		} else {
			// Programmer id exists. Now test if it is still valid
			if (!isValidId(programmerid)) {
				// id is not valid. Deselect Combo and show a Warning
				fProgrammerCombo.deselect(fProgrammerCombo.getSelectionIndex());
				showProgrammerWarning(LABEL_CONFIG_WARNING, true);
			} else {
				// everything is good. Select the id in the combo
				String programmername = getProgrammerConfigName(programmerid);
				int index = fProgrammerCombo.indexOf(programmername);
				fProgrammerCombo.select(index);
				showProgrammerWarning("", false);
			}
		}

		//fBitClockText.setText(fTargetProps.getBitclock());
		//fBitBangDelayText.setText(fTargetProps.getBitBangDelay());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractTCDBPropertyTab#doProgConfigsChanged(java.lang.String[],
	 *      int)
	 */
	@Override
	protected void doProgConfigsChanged(String[] configs, int newindex) {

		fProgrammerCombo.setItems(configs);

		// make the combo show all available items (no scrollbar)
		fProgrammerCombo.setVisibleItemCount(configs.length);

		if (newindex != -1) {
			fProgrammerCombo.select(newindex);
		} else {
			fProgrammerCombo.deselect(fProgrammerCombo.getSelectionIndex());
		}
	};

	/**
	 * Adds a new configuration or edits the currently selected Programmer
	 * Configuration.
	 * <p>
	 * Called when either the new or the edit button has been clicked.
	 * </p>
	 * 
	 * @see TCDBConfigEditor
	 */
	private void editButtonAction(boolean createnew) {
		ProgrammerConfig oldconfig = null;

		// Create a list of all currently available configurations
		// This is used by the editor to avoid name clashes
		// (a configuration name needs to be unique)
		String[] allcfgs = fProgrammerCombo.getItems();
		Set<String> allconfignames = new HashSet<String>(allcfgs.length);
		for (String cfg : allcfgs) {
			allconfignames.add(cfg);
		}

		if (createnew) { // new config
			// Create a new configuration with a default name
			// (with a trailing running number if required),
			// a sample Description text and stk500v2 as programmer
			// (because I happen to have one)
			// All other options remain at the default (empty)
			String basename = "New Configuration";
			String defaultname = basename;
			int i = 1;
			while (allconfignames.contains(defaultname)) {
				defaultname = basename + " (" + i++ + ")";
			}
			oldconfig = fCfgManager.createNewConfig();
			oldconfig.setName(defaultname);
		} else { // edit existing config
			// Get the ProgrammerConfig from the Combo
			String configname = allcfgs[fProgrammerCombo.getSelectionIndex()];
			String configid = getProgrammerConfigId(configname);
			oldconfig = getProgrammerConfig(configid);
		}

		// Open the Config Editor.
		// If the OK Button was selected, the modified Config is fetched from
		// the Dialog and the the superclass is informed about the addition /
		// modification.
		TCDBConfigEditor dialog = new TCDBConfigEditor(fProgrammerCombo.getShell(),
		        oldconfig, allconfignames);
		if (dialog.open() == Window.OK) {
			// OK Button selected:
			ProgrammerConfig newconfig = dialog.getResult();
			fTargetProps.setProgrammer(newconfig);

			addProgrammerConfig(newconfig);
			updateData(fTargetProps.getParent());
		}
	}

	/**
	 * Show the supplied Warning in the Programmer config group.
	 * 
	 * @param text
	 *            Message to display.
	 * @param warning
	 *            <code>true</code> to make the warning visible,
	 *            <code>false</code> to hide it.
	 */
	private void showProgrammerWarning(String text, boolean warning) {
		fConfigWarningIcon.setVisible(warning);
		fConfigWarningMessage.setText(text);
		fConfigWarningMessage.pack();
		fConfigWarningMessage.setVisible(true);
	}

}
