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

package com.telink.tc32eclipse.ui.editors.targets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.telink.tc32eclipse.core.targets.ITargetConfigurationWorkingCopy;

/**
 * The GUI code container for the Target Configuration editor.
 * <p>
 * This page is the one and only page in the {@link TargetConfigurationEditor}. It implements the
 * <code>IFormPart</code> interface to utilize the managed form API of Eclipse.
 * 
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class PageMain extends BasePage {

	public final static String						ID		= "com.telink.tc32eclipse.ui.targets.nameandmcu";

	private final static String						TITLE	= "Main";

	/**
	 * The target configuration this editor page works on. The target config is final and con not be
	 * changed after instantiation of the page. This is the 'model' for the managed form.
	 */
	final private ITargetConfigurationWorkingCopy	fTCWC;

	/**
	 * Create a new EditorPage.
	 * <p>
	 * The page has the id from the {@link #ID} identifier and the fixed title string {@link #TITLE}
	 * .
	 * </p>
	 * 
	 * @param editor
	 *            Parent FormEditor
	 */
	public PageMain(SharedHeaderFormEditor editor) {
		super(editor, ID, TITLE);

		// Get the TargetConfiguration from the editor input.
		IEditorInput ei = editor.getEditorInput();
		fTCWC = (ITargetConfigurationWorkingCopy) ei
				.getAdapter(ITargetConfigurationWorkingCopy.class);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		// fill the fixed parts of the form...
		Composite body = managedForm.getForm().getBody();
		body.setLayout(new TableWrapLayout());

		{
			SectionName namePart = new SectionName();
			namePart.setMessageManager(getMessageManager());
			managedForm.addPart(namePart);
			registerPart(namePart);
			namePart.getControl().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		}

		{
			SectionMCU mcuPart = new SectionMCU();
			mcuPart.setMessageManager(getMessageManager());
			managedForm.addPart(mcuPart);
			registerPart(mcuPart);
			mcuPart.getControl().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		}

		{
			SectionSummary summaryPart = new SectionSummary();
			summaryPart.setMessageManager(getMessageManager());
			managedForm.addPart(summaryPart);
			registerPart(summaryPart);
			summaryPart.getControl().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
			summaryPart.setEditor(getEditor());
		}

		// ... and give the 'model' to the managed form which will cause the dynamic parts of the
		// form to be rendered.
		managedForm.setInput(fTCWC);
	}
}
