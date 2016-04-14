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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.osgi.service.prefs.BackingStoreException;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.core.properties.TCDBProperties;
import com.telink.tc32eclipse.core.properties.TC32ProjectProperties;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfig;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfigManager;
import com.telink.tc32eclipse.core.toolinfo.TCDB;

/**
 * Extends {@link AbstractTC32PropertyTab} to manage the list of {@link ProgrammerConfig} objects.
 * <p>
 * All Tabs requiring direct access to TCDB should extend this class to get a valid
 * ProgrammerConfig to pass to the {@link TCDB} action methods.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public abstract class AbstractTCDBPropertyTab extends AbstractTC32PropertyTab {

	/** The list of all new or edited {@link ProgrammerConfig} objects */
	private final Map<String, ProgrammerConfig>		fModifiedProgCfgs	= new HashMap<String, ProgrammerConfig>();

	/** The list of all current Programmer Config Ids */
	private Map<String, String>						fProgCfgIDs			= new HashMap<String, String>();

	protected final static ProgrammerConfigManager	fCfgManager			= ProgrammerConfigManager
																				.getDefault();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractTC32PropertyTab#performApply(de.innot.avreclipse.core.properties.TC32ProjectProperties)
	 */
	@Override
	protected void performApply(TC32ProjectProperties dst) {
		// Get the TCDB Properties and pass it to the subclass
		TCDBProperties TCDBprops = dst.getTCDBProperties();
		performApply(TCDBprops);

	}

	abstract protected void performApply(TCDBProperties dstprops);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractTC32PropertyTab#performCopy(de.innot.avreclipse.core.preferences.TC32ProjectProperties)
	 */
	@Override
	protected void performCopy(TC32ProjectProperties src) {

		// Get the TCDB Properties and pass it to the subclass
		TCDBProperties TCDBprops = src.getTCDBProperties();
		performCopy(TCDBprops);

		// Update the TCDB command preview.
		updateTCDBPreview(TCDBprops);
	}

	abstract protected void performCopy(TCDBProperties srcprops);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.innot.avreclipse.ui.propertypages.AbstractTC32PropertyTab#updateData(de.innot.avreclipse.core.preferences.TC32ProjectProperties)
	 */
	@Override
	protected void updateData(TC32ProjectProperties props) {
		// Get the TCDB Properties and pass it to the subclass
		TCDBProperties TCDBprops = props.getTCDBProperties();
		updateData(TCDBprops);

		// Update the TCDB command preview.
		updateTCDBPreview(props.getTCDBProperties());
	}

	abstract protected void updateData(TCDBProperties TCDBprops);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#performOK()
	 */
	@Override
	protected void performOK() {

		// Save all new / modified programmer configurations
		saveProgrammerConfigs();

		// The saving of the modified TCDB properties is done by the caller
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#performCancel()
	 */
	@Override
	protected void performCancel() {
		// remove all modified programmer configurations.
		// This is probably not necessary, as the field is not static and
		// after a cancel this tab is disposed.
		// But just to be sure...
		fModifiedProgCfgs.clear();
	}

	/**
	 * Get the ID of the ProgrammerConfig for the given name.
	 * 
	 * @param name
	 *            <code>String</code> with the name
	 * @return <code>String</code> with the associated ID or <code>null</code> if the name does
	 *         not exist.
	 */
	protected String getProgrammerConfigId(String name) {

		for (String id : fProgCfgIDs.keySet()) {
			String cfgname = fProgCfgIDs.get(id);
			if (cfgname.equals(name)) {
				return id;
			}
		}

		// name not found
		return null;
	}

	/**
	 * Get the name of the ProgrammerConfig for the given ID.
	 * 
	 * @param id
	 *            <code>String</code> with the ID
	 * @return <code>String</code> with the name or <code>null</code> if the ID does not exist.
	 */
	protected String getProgrammerConfigName(String id) {
		return fProgCfgIDs.get(id);
	}

	/**
	 * Get the {@link ProgrammerConfig} with the given ID.
	 * <p>
	 * This method will first check if a config with the ID is already in the new&amp;modified
	 * config list. If not, the call is passed on to the config manager.
	 * </p>
	 * 
	 * @see ProgrammerConfigManager#getConfig(String)
	 * 
	 * @param configid
	 *            <code>String</code> with an ID value
	 * @return The requested <code>ProgrammerConfig</code> or <code>null</code> if no config
	 *         with the given ID exists.
	 */
	protected ProgrammerConfig getProgrammerConfig(String configid) {
		if (fModifiedProgCfgs.containsKey(configid)) {
			// requested config is in the list of modified configs
			return fModifiedProgCfgs.get(configid);
		}
		// load the config from the preference store
		return fCfgManager.getConfig(configid);
	}

	/**
	 * Reload the list of all ProgrammerConfigs from the preference store.
	 * <p>
	 * This will clear any added / modified configs, so only call this to init the GUI or to restore
	 * the defaults.
	 * </p>
	 * <p>
	 * Interested extending classes are informed about the new list via the
	 * {@link #doProgConfigsChanged(String[], int)} hook method.
	 * </p>
	 */
	protected void loadProgrammerConfigs() {

		// Clear the list of previously modified ProgrammerConfigs
		fModifiedProgCfgs.clear();

		// Now read the list of configs from the preferences, then
		// create a List of all names,
		// sort them and finally
		// inform any extending classes about the new list
		fProgCfgIDs = fCfgManager.getAllConfigNames();
		List<String> allCfgNames = new ArrayList<String>(fProgCfgIDs.size());
		for (String cfgname : fProgCfgIDs.values()) {
			allCfgNames.add(cfgname);
		}

		Collections.sort(allCfgNames);

		// inform the interested superclasses about the new list.
		// The selection index is set at -1 to indicate that no entry in
		// particualar should be selected.
		doProgConfigsChanged(allCfgNames.toArray(new String[allCfgNames.size()]), -1);
	}

	/**
	 * Add a new / modified {@link ProgrammerConfig} to the list of modified configs.
	 * <p>
	 * The given config is <strong>not</strong> saved to the preferences until the Apply or OK
	 * buttons are clicked by the user
	 * </p>
	 * 
	 * @param newconfig
	 */
	protected void addProgrammerConfig(ProgrammerConfig config) {

		String configid = config.getId();
		String configname = config.getName();

		// Add / replace the config in the internal lists
		fModifiedProgCfgs.put(configid, config);
		fProgCfgIDs.put(configid, configname);

		// Now get the list of all config names and sort it,
		List<String> allnames = new ArrayList<String>(fProgCfgIDs.values());
		Collections.sort(allnames);

		// find the name of the config in the list
		int newindex = allnames.indexOf(configname);

		// Inform any interested extending classes about the change
		doProgConfigsChanged(allnames.toArray(new String[fProgCfgIDs.size()]), newindex);

		return;
	}

	/**
	 * List of ProgrammerConfigs has changed.
	 * <p>
	 * This is a hook for extending classes to be notified if the list of all available
	 * ProgrammerConfigs has changed.
	 * </p>
	 * 
	 * @param configs
	 *            Array of <code>String</code> with the names of all available configs.
	 * @param selectedindex
	 *            <code>int</code> with the index of the config that caused the list to be
	 *            reloaded.
	 */
	protected void doProgConfigsChanged(String[] configs, int selectedindex) {
	};

	/**
	 * Save all ProgrammerConfigs that have been added or edited on this Tab.
	 * <p>
	 * In case of Exceptions writing to the preferences, a status message is written to the system
	 * log.
	 * </p>
	 */
	protected void saveProgrammerConfigs() {
		// Go thru all modified configs and save them.
		// Afterwards the list of modified configs is cleared.
		for (ProgrammerConfig cfg : fModifiedProgCfgs.values()) {
			try {
				fCfgManager.saveConfig(cfg);
			} catch (BackingStoreException e) {
				// Inform the user that the save failed.
				IStatus status = new Status(Status.ERROR, TC32Plugin.PLUGIN_ID,
						"Can't save Programmer Configuration [" + cfg.getName()
								+ "] to the preference storage area", e);
				TC32Plugin.getDefault().log(status);
				ErrorDialog.openError(super.usercomp.getShell(), "TC32 Properties Error", null,
						status);
			}
		}
		fModifiedProgCfgs.clear();
	}

	/**
	 * Check if the given ProgrammerConfig ID is valid, i.e. exists.
	 * <p>
	 * This method checks the internal list of modified configs first, and if no config with the ID
	 * is in this list, pass the question onto the <code>ProgrammerConfigManager</code>.
	 * </p>
	 * 
	 * @see ProgrammerConfigManager#isValidId(String)
	 * 
	 * @param id
	 *            The ID value
	 * @return <code>true</code> if the id exists, <code>false</code> otherwise.
	 */
	protected boolean isValidId(String id) {
		// Check the list of modified configs
		if (fModifiedProgCfgs.containsKey(id)) {
			return true;
		}

		// else check what the config manager knows
		return fCfgManager.isValidId(id);
	}

	/**
	 * Update the TCDB command line preview.
	 * <p>
	 * This is a convenience method equivalent to
	 * 
	 * <pre>
	 * ((PageTCDB) page).updatePreview(props);
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param props
	 *            The <code>TC32ProjectProperties</code> for which to display the preview
	 */
	protected void updateTCDBPreview(TCDBProperties props) {

		if (page instanceof PageTCDB) {
			PageTCDB TCDBpage = (PageTCDB) page;

			TCDBpage.updatePreview(props);
		}
	}
}
