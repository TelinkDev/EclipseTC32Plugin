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
 * $Id: SectionMCU.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.ui.editors.targets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.VerifyEvent;
//import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.telink.tc32eclipse.core.targets.ITargetConfigConstants;
import com.telink.tc32eclipse.core.util.TC32MCUidConverter;

/**
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class SectionMCU extends AbstractTCSectionPart implements ITargetConfigConstants {

	private Combo						fMCUcombo;
//	private Combo						fFCPUcombo;

	final private Map<String, String>	fMCUList		= new HashMap<String, String>();
	final private List<String>			fMCUNames		= new ArrayList<String>();

	private final static String[]		PART_ATTRS		= new String[] { ATTR_MCU };
	private final static String[]		PART_DEPENDS	= new String[] { ATTR_PROGRAMMER_TOOL_ID,
			ATTR_GDBSERVER_ID							};



	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#getTitle()
	 */
	@Override
	protected String getTitle() {
		return "Target Processor";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#getDescription()
	 */
	@Override
	protected String getDescription() {
		return "The target MCU";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#getPartAttributes
	 * ()
	 */
	@Override
	public String[] getPartAttributes() {
		return PART_ATTRS;
	}

	/*
	 * (non-Javadoc)
	 * @seecom.telink.tc32eclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#
	 * getDependentAttributes()
	 */
	@Override
	protected String[] getDependentAttributes() {
		return PART_DEPENDS;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#createSectionContent
	 * (org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	protected void createSectionContent(Composite parent, FormToolkit toolkit) {
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 12;
		parent.setLayout(layout);

		//
		// The MCU Combo
		// 
		toolkit.createLabel(parent, "MCU type:");
		fMCUcombo = new Combo(parent, SWT.READ_ONLY);
		toolkit.adapt(fMCUcombo, true, true);
		fMCUcombo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		fMCUcombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String mcuid = TC32MCUidConverter.name2id(fMCUcombo.getText());
				getTargetConfiguration().setMCU(mcuid);
				refreshMessages();
				getManagedForm().dirtyStateChanged();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#updateSectionContent
	 * ()
	 */
	@Override
	protected void refreshSectionContent() {
		// Get the list of valid MCUs, sort them, convert to MCU name and fill the internal cache
		fMCUList.clear();
		fMCUNames.clear();
		Set<String> allmcuset = getTargetConfiguration().getSupportedMCUs(true);
		List<String> allmcuids = new ArrayList<String>(allmcuset);
		Collections.sort(allmcuids);

		String currentmcu = getTargetConfiguration().getMCU();

		// Add the current mcu to the list if it is not already in it.
		// This prevents the combo from becoming empty at the cost of one
		// 'invalid' mcu in the list
		if (!allmcuset.contains(currentmcu)) {
			allmcuids.add(currentmcu);
		}

		for (String mcuid : allmcuids) {
			String name = TC32MCUidConverter.id2name(mcuid);
			fMCUList.put(mcuid, name);
			fMCUNames.add(name);
		}

		// Tell the fMCUCombo about the new list but keep the previously selected MCU
		fMCUcombo.setItems(fMCUNames.toArray(new String[fMCUNames.size()]));
		fMCUcombo.setVisibleItemCount(Math.min(fMCUNames.size(), 20));

		String currentMCUName = TC32MCUidConverter.id2name(currentmcu);
		fMCUcombo.setText(currentMCUName);

		// Finally show an error if the MCU is not supported by the tools.
		refreshMessages();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.ui.editors.targets.AbstractTCSectionPart#refreshMessages()
	 */
	@Override
	protected void refreshMessages() {
		validate(ATTR_MCU, fMCUcombo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.ui.editors.targets.AbstractTCSectionPart#setFocus(java.lang.String)
	 */
	@Override
	public boolean setFocus(String attribute) {
		if (attribute.equals(ATTR_MCU)) {
			if (fMCUcombo != null && !fMCUcombo.isDisposed()) {
				fMCUcombo.setFocus();
			}
			return true;
		}
		return false;
	}

}
