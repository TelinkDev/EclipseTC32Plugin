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
 * $Id: AbstractTC32PropertyTab.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.ui.propertypages;

import org.eclipse.cdt.core.settings.model.ICResourceDescription;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.ui.properties.AbstractCBuildPropertyTab;
import org.eclipse.cdt.ui.newui.ICPropertyTab;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.core.properties.TC32ProjectProperties;
import com.telink.tc32eclipse.core.properties.ProjectPropertyManager;

/**
* Abstract parent class for all TC32 Property tabs.
* <p>
* This class is an interface between <code>ICPropertyTab</code>, which works on
* ICResourceDescriptions, and the {@link TC32ProjectProperties} where all TC32 specific settings are
* stored, either per project or - at user discretion - per build configuration.
* </p>
* <p>
* {@link #performApply(TC32ProjectProperties)} and {@link #updateData(TC32ProjectProperties)} are
* almost identical to the methods in <code>ICPropertyTab</code>, while
* <code>performDefaults()</code> is replaced by {@link #performCopy(TC32ProjectProperties)},
* which enables this class to send different default properties to the implementor.
* </p>
* 
* @author Peter Shieh
* @since 0.1
* 
*/
public abstract class AbstractTC32PropertyTab extends AbstractCBuildPropertyTab {

	/**
	 * special Tab message to indicate that the given Properties should be copied. This is very
	 * similar to {@link ICPropertyTab#DEFAULTS} message.
	 */
	public final static int	COPY	= 200;

	/**
	 * Action for an Apply event.
	 * <p>
	 * The implementation must copy the values relevant to the current page to the given destination
	 * properties.
	 * </p>
	 * The given properties are fresh, unmodified props from the properties storage. They will be
	 * saved once this method returns.
	 * </p>
	 * 
	 * @param dstprops
	 *            Destination properties.
	 */
	protected abstract void performApply(TC32ProjectProperties dstprops);

	/**
	 * Action for a Copy event.
	 * <p>
	 * The implementation must copy the values relevant to the current page from the given source
	 * properties.
	 * </p>
	 * <p>
	 * This method is called with either the default properties or with the project properties,
	 * depending on whether the "Defaults" or the "Copy from Project" Button has been clicked by the
	 * user.
	 * </p>
	 * <p>
	 * It is up to the implementor to call {@link #updateData(TC32ProjectProperties)} to update the
	 * representation after the copy has taken place.
	 * </p>
	 * 
	 * @param srcprops
	 *            Source properties.
	 */
	protected abstract void performCopy(TC32ProjectProperties srcprops);

	/**
	 * Update the tab to the values of the given properties.
	 * <p>
	 * This method is called whenever a different build configuration is selected by the user or the
	 * "per Config Settings" flag has changed. The props parameter has the properties for the
	 * configuration / project.
	 * </p>
	 * <p>
	 * Implementing classes should update their controls to the values of the properties and can
	 * must make all future modifications directly to the given properties.
	 * </p>
	 * 
	 * @param props
	 *            <code>TC32ProjectProperties</code> the tab must work with.
	 */
	protected abstract void updateData(TC32ProjectProperties props);

	/**
	 * Action for a defaults event.
	 * <p>
	 * This is called in addition to {@link #performCopy(TC32ProjectProperties)}, so that subclasses
	 * can override to add any special handling for the defaults case, which does not apply to the
	 * copy event. E.g. the main page overrides this to reset the list of available programmers.
	 * </p>
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		// Subclasse
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#handleTabEvent(int, java.lang.Object)
	 */
	@Override
	public void handleTabEvent(int kind, Object data) {
		// Override handleTabEvent to handle the COPY and DEFAULTS messages.
		// 
		// The DEFAULTS message is intercepted here, because the handling of
		// defaults is different in the TC32 tabs compared to the standard
		// CPropertyTabs. The default properties are not writable (at least not
		// saveable), so we cannot pass the default properties directly to the
		// updateData() method.
		// Instead the default properties are passed to the new performCopy(),
		// which is implemented in subclasses and in which they copy their
		// relevant properties from the given default/project properties.
		// 
		// The same method is used to reset a tab to the project settings. If
		// the "Copy Project Settings" Button is pressed, the parent
		// AbstractTC32Page will get the Project properties and generate a COPY
		// message with the project properties attached.
		//
		// Both handlers call updateData(getResDesc()) first, because
		// updateData() is used to pass a valid TC32ProjectProperties to the
		// subclass and might not have been called when the handler is executed.
		switch (kind) {
			case COPY:
				updateData(getResDesc());
				TC32ProjectProperties projectprops = (TC32ProjectProperties) data;
				performCopy(projectprops);
				break;
			case ICPropertyTab.DEFAULTS:
				updateData(getResDesc());
				TC32ProjectProperties defaultprops = ProjectPropertyManager.getDefaultProperties();
				performDefaults();
				performCopy(defaultprops);
				break;
			default:
				// All other messages (APPLY, DISPOSE etc.) are handled by the
				// superclass.
				super.handleTabEvent(kind, data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#performApply(org.eclipse.cdt.core.settings.model.ICResourceDescription,
	 *      org.eclipse.cdt.core.settings.model.ICResourceDescription)
	 */
	@Override
	protected void performApply(ICResourceDescription src, ICResourceDescription dst) {

		// Apply should only save the values of this Tab.
		// To do this, we get a fresh Property Element, which is filled with the
		// values from the property storage.
		// Then this new Element is passed on to the subclass, which
		// modifies only its own values.
		// Finally the Element is saved again to the property storage.

		TC32ProjectProperties freshprops;

		freshprops = TC32PropertyPageManager.getConfigPropertiesNoCache(src);

		performApply(freshprops);

		try {
			freshprops.save();
		} catch (BackingStoreException e) {
			IStatus status = new Status(IStatus.ERROR, TC32Plugin.PLUGIN_ID,
					"Could not write to the preferences.", e);

			ErrorDialog.openError(super.usercomp.getShell(), "TC32 Properties Error", null, status);
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#updateData(org.eclipse.cdt.core.settings.model.ICResourceDescription)
	 */
	@Override
	protected void updateData(ICResourceDescription resdesc) {

		// Translate ICResourceDescription to TC32ProjectProperties and pass them
		// to the subclass.
		TC32ProjectProperties props = TC32PropertyPageManager.getConfigProperties(resdesc);
		updateData(props);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#updateButtons()
	 */
	@Override
	protected void updateButtons() {
		// Why was this method made Abstract by the superclass?
		// As only few pages actually need this, it could have been declared as
		// an empty method.
		// Like we do here, to spare extending classes of implementing this
		// useless method.
	};

	/**
	 * Sets the rebuild flag for the current configuration or the complete project.
	 * <p>
	 * Passing <code>false</code> is not recommended, as it might prevent necessary rebuilds
	 * caused by changes outside of the TC32 property world.
	 * </p>
	 * 
	 * @param rebuild
	 *            <code>true</code> if a complete rebuild is required.
	 */
	protected void setRebuildState(boolean rebuild) {

		// Check if we have per project or per config setting
		AbstractTC32Page tc32page = (AbstractTC32Page) page;
		if (tc32page.isPerConfig()) {
			// Set the rebuild flag for the current configuration
			getCfg().setRebuildState(rebuild);
		} else {
			// Set the rebuild flag for the complete project
			ManagedBuildManager.getBuildInfo(getCfg().getOwner()).setRebuildState(rebuild);
		}
	}

	/**
	 * Convenience method to add a separator bar to the composite.
	 * <p>
	 * The parent composite must have a <code>GridLayout</code>. The separator bar will span all
	 * columns of the parent grid layout.
	 * </p>
	 * 
	 * @param parent
	 *            <code>Composite</code>
	 */
	protected void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		Layout parentlayout = parent.getLayout();
		if (parentlayout instanceof GridLayout) {
			int columns = ((GridLayout) parentlayout).numColumns;
			GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false, columns, 1);
			separator.setLayoutData(gridData);
		}
	}

	/**
	 * Returns the value of the "per config" flag for this project.
	 * 
	 * @return <code>true</code> if each build configuration has its own properties.
	 */
	protected boolean isPerConfig() {
		if (page instanceof AbstractTC32Page) {
			AbstractTC32Page tc32page = (AbstractTC32Page) page;
			return tc32page.isPerConfig();
		}
		return true;
	}

	/**
	 * Create and return a "Workplace" browse Button.
	 * <p>
	 * Clicking the Button will open a Workplace file selector Dialog and the result is copied to
	 * the supplied <code>Text</code> Control.
	 * </p>
	 * 
	 * @param parent
	 *            Parent <code>Composite</code>, which needs to have <code>GridLayout</code>
	 * @param text
	 *            Target <code>Text</code> Control
	 * @return <code>Button</code> Control with the created Button.
	 */
	protected Button setupWorkplaceButton(Composite parent, final Text text) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(WORKSPACEBUTTON_NAME);
		GridData gd = new GridData(SWT.CENTER, SWT.NONE, false, false);
		// make all Buttons the same size
		gd.minimumWidth = BUTTON_WIDTH;
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String location = getWorkspaceFileDialog(text.getShell(), EMPTY_STR);
				if (location != null) {
					text.setText(location);
				}
			}
		});
		return button;
	}

	/**
	 * Create and return a "Filesystem" browse Button.
	 * <p>
	 * Clicking the Button will open a file selector Dialog and the result is copied to the supplied
	 * <code>Text</code> Control.
	 * </p>
	 * 
	 * @param parent
	 *            Parent <code>Composite</code>, which needs to have <code>GridLayout</code>
	 * @param text
	 *            Target <code>Text</code> Control
	 * @param exts
	 *            <code>String[]</code> with all valid file extensions. Files with other
	 *            extensions will be filtered.
	 * @return <code>Button</code> Control with the created Button.
	 */
	protected Button setupFilesystemButton(Composite parent, final Text text, String[] exts) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(FILESYSTEMBUTTON_NAME);
		GridData gd = new GridData(SWT.CENTER, SWT.NONE, false, false);
		// make all Buttons the same size
		gd.minimumWidth = BUTTON_WIDTH;
		button.setLayoutData(gd);
		button.setData(exts);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String[] exts = (String[]) event.widget.getData();
				String location = getFileSystemFileDialog(text.getShell(), EMPTY_STR, exts);
				if (location != null) {
					text.setText(location);
				}
			}
		});
		return button;
	}

	/**
	 * Create and return a "Variable" browse Button.
	 * <p>
	 * Clicking the Button will open a variable selector Dialog and the result is inserted into the
	 * supplied <code>Text</code> Control at the current cursor position.
	 * </p>
	 * 
	 * @param parent
	 *            Parent <code>Composite</code>, which needs to have <code>GridLayout</code>
	 * @param text
	 *            Target <code>Text</code> Control
	 * @return <code>Button</code> Control with the created Button.
	 */
	protected Button setupVariableButton(Composite parent, final Text text) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(VARIABLESBUTTON_NAME);
		GridData gd = new GridData(SWT.CENTER, SWT.NONE, false, false);
		// make all Buttons the same size
		gd.minimumWidth = BUTTON_WIDTH;
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String var = getVariableDialog(text.getShell(), getResDesc().getConfiguration());
				if (var != null) {
					text.insert(var);
				}
			}
		});
		return button;
	}

	/**
	 * Open a FileSystem Dialog and return the selected file as a <code>String</code>.
	 * 
	 * @param shell
	 *            Shell in which to open the Dialog
	 * @param text
	 *            Root file name
	 * @param exts
	 *            <code>String[]</code> with all valid file extensions. Files with other
	 *            extensions will be filtered.
	 * @return <code>String</code> with the selected filename or <cod>null</code> if the user has
	 *         cancelled or an error occured.
	 */
	public static String getFileSystemFileDialog(Shell shell, String text, String[] exts) {

		// Why has the AbstractCPropertyTab.getFileSystemDialog() a hardcoded
		// list of extensions?
		// This is basically the same method, but with a list of valid
		// extensions as parameter.
		FileDialog dialog = new FileDialog(shell);
		if (text != null && text.trim().length() != 0)
			dialog.setFilterPath(text);
		dialog.setFilterExtensions(exts);
		dialog.setText(FILESYSTEM_FILE_DIALOG_TITLE);
		return dialog.open();
	}

	/**
	 * Enable / Disable the given Composite.
	 * 
	 * @param compo
	 *            A <code>Composite</code> with some controls.
	 * @param value
	 *            <code>true</code> to enable, <code>false</code> to disable the given group.
	 */
	protected void setEnabled(Composite compo, boolean value) {
		Control[] children = compo.getChildren();
		for (Control child : children) {
			child.setEnabled(value);
		}
	}

}
