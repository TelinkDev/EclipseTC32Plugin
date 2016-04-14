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
 * $Id: PageGDBServerTool.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.ui.editors.targets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.telink.tc32eclipse.core.targets.ITargetConfigChangeListener;
import com.telink.tc32eclipse.core.targets.ITargetConfigConstants;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration;
import com.telink.tc32eclipse.core.targets.ITargetConfigurationWorkingCopy;
import com.telink.tc32eclipse.core.targets.ToolManager;

/**
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class PageGDBServerTool extends BasePage implements ITargetConfigChangeListener {

	public final static String						ID		= "com.telink.tc32eclipse.ui.targets.gdbservertool";

	private final static String						TITLE	= "GDB Server";

	private ITCEditorPart							fSectionPart;

	private IManagedForm							fManagedForm;

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
	public PageGDBServerTool(SharedHeaderFormEditor editor) {
		super(editor, ID, TITLE);

		// Get the TargetConfiguration from the editor input.
		IEditorInput ei = editor.getEditorInput();
		fTCWC = (ITargetConfigurationWorkingCopy) ei
				.getAdapter(ITargetConfigurationWorkingCopy.class);

		fTCWC.addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#dispose()
	 */
	@Override
	public void dispose() {
		if (fTCWC != null) {
			fTCWC.removePropertyChangeListener(this);
		}
		// TODO Auto-generated method stub
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {

		fManagedForm = managedForm;

		Composite body = managedForm.getForm().getBody();
		body.setLayout(new TableWrapLayout());

		SectionGDBServerTool gdbserverToolPart = new SectionGDBServerTool();
		gdbserverToolPart.setMessageManager(getMessageManager());
		managedForm.addPart(gdbserverToolPart);
		registerPart(gdbserverToolPart);
		gdbserverToolPart.getControl().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		createExtensibleContent();

		// ... and give the 'model' to the managed form which will cause the dynamic parts of the
		// form to be rendered.
		managedForm.setInput(fTCWC);

	}

	private void createExtensibleContent() {

		if (fSectionPart != null) {
			// Remove the previous settings part
			fManagedForm.removePart(fSectionPart);
			unregisterPart(fSectionPart);
			fSectionPart.dispose();
			fSectionPart = null;
			fManagedForm.reflow(true);
		}

		String toolid = fTCWC.getAttribute(ITargetConfigConstants.ATTR_GDBSERVER_ID);
		ITCEditorPart part = SettingsExtensionManager.getDefault().getSettingsPartForTool(toolid);

		if (part == null) {
			// No extension with a GUI for the tool available
			// Create a dummy part that contains just a label to inform the user that there are no
			// settings for this tool.
			part = createDummyPart(toolid);
		}

		part.setMessageManager(getMessageManager());
		fManagedForm.addPart(part);
		registerPart(part);
		part.getControl().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		fSectionPart = part;
		fManagedForm.reflow(true);

		// Setting the part name does not change anything. Probably because the
		// MulitPageEditorPart
		// does not listen to Part Property change events.
		// I leave this code in case the current behaviour gets changed in a future Eclipse
		// version.
		String toolname = ToolManager.getDefault().getToolName(toolid);
		String partname = "GDBServer: " + toolname;
		setPartName(partname);

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.ITargetConfigChangeListener#attributeChange(com.telink.tc32eclipse
	 * .core.targets.ITargetConfiguration, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void attributeChange(ITargetConfiguration config, String attribute, String oldvalue,
			String newvalue) {

		if (ITargetConfigConstants.ATTR_GDBSERVER_ID.equals(attribute)) {
			// The gdbserver has been changed
			createExtensibleContent();
		}

	}

	private ITCEditorPart createDummyPart(String tool) {

		return new AbstractTCSectionPart() {

			@Override
			protected void createSectionContent(Composite parent, FormToolkit toolkit) {
			}

			@Override
			protected String[] getPartAttributes() {
				return new String[] {};
			}

			@Override
			protected String getTitle() {
				return "Settings";
			}

			@Override
			protected String getDescription() {
				return "No Settings available";
			}

			@Override
			protected int getSectionStyle() {
				return Section.SHORT_TITLE_BAR | Section.EXPANDED | Section.CLIENT_INDENT;
			}

			@Override
			protected void refreshSectionContent() {
				// Do nothing
			}

		};

	}
}
