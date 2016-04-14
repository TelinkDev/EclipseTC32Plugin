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
 * $Id: SectionHostInterface.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.ui.editors.targets;

import java.text.MessageFormat;

//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.SelectionListener;
//import org.eclipse.swt.events.VerifyEvent;
//import org.eclipse.swt.events.VerifyListener;
//import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.layout.RowLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Group;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
//import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.telink.tc32eclipse.core.targets.HostInterface;
import com.telink.tc32eclipse.core.targets.ITargetConfigConstants;
import com.telink.tc32eclipse.core.targets.ITargetConfigurationWorkingCopy;

/**
 * FormPart to edit all settings for the current host interface.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class SectionHostInterface extends AbstractTCSectionPart implements ITargetConfigConstants {

	/** Used as the default string for Port / Baudrate settings. */
//	private final static String		DEFAULT			= "default";


	/** The list of target configuration attributes that this part manages. */
	private final static String[]	PART_ATTRS		= new String[] { //
													//
			ATTR_PROGRAMMER_PORT};

	/** The list of target configuration attributes that cause this part to refresh. */
	private final static String[]	PART_DEPENDS	= new String[] { ATTR_HOSTINTERFACE };

	/** the client area of the Section created by the superclass. */
	private Composite				fSectionClient;

	/*
	 * (non-Javadoc)
	 * @see de.innot.avreclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#getTitle()
	 */
	@Override
	protected String getTitle() {
		// This is just a placeholder dummy.
		// The real name will be set in the refreshSectionContent() method.
		return "Host Interface";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.innot.avreclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#getDescription()
	 */
	@Override
	protected String getDescription() {
		return null; // TODO: add a description
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.innot.avreclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#getPartAttributes
	 * ()
	 */
	@Override
	public String[] getPartAttributes() {
		return PART_ATTRS;
	}

	/*
	 * (non-Javadoc)
	 * @seede.innot.avreclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#
	 * getDependentAttributes()
	 */
	@Override
	protected String[] getDependentAttributes() {
		return PART_DEPENDS;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * de.innot.avreclipse.ui.editors.targets.AbstractTargetConfigurationEditorPart#getSectionStyle
	 * ()
	 */
	@Override
	protected int getSectionStyle() {
		return Section.TWISTIE | Section.SHORT_TITLE_BAR | Section.EXPANDED | Section.CLIENT_INDENT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.AbstractFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	public void createSectionContent(Composite parent, FormToolkit toolkit) {

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 12;
		parent.setLayout(layout);

		fSectionClient = parent;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.AbstractFormPart#refresh()
	 */
	@Override
	public void refreshSectionContent() {

		final ITargetConfigurationWorkingCopy tcwc = getTargetConfiguration();

		// Get the required information from the target configuration
		String newHItxt = tcwc.getAttribute(ATTR_HOSTINTERFACE);
		HostInterface newHI = HostInterface.valueOf(newHItxt);

		//
		// Clear the old section content
		//

		// remove all previous controls from the section
		Control[] children = fSectionClient.getChildren();
		for (Control child : children) {
			child.dispose();
		}
		fSectionClient.layout(true, true);

		// Finally reflow the form. Otherwise layout artifacts may remain behind.
		getManagedForm().reflow(true);

		//
		// redraw the complete section.
		//

		String title = MessageFormat.format("{0} Settings", newHI.toString());
		getControl().setText(title);

		// rebuild the content
		FormToolkit toolkit = getManagedForm().getToolkit();
		Composite portCompo = toolkit.createComposite(fSectionClient);
		portCompo
				.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 2));
		portCompo.setLayout(new GridLayout(2, false));


		//Control section;



	}


}
