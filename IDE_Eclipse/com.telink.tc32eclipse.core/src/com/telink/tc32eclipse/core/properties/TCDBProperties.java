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
 * $Id: TCDBProperties.java 851 2010-08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.telink.tc32eclipse.core.tcdb.TCDBAction;
import com.telink.tc32eclipse.core.tcdb.TCDBActionFactory;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfig;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfigManager;
//import com.telink.tc32eclipse.core.toolinfo.TCDB;

/**
 * Container for all TCDB specific properties of a project.
 * <p>
 * Upon instantiation, the properties are loaded from the given preference store. All changes are
 * local to the object until the {@link #save()} method is called.
 * </p>
 * <p>
 * <code>TCDBProperties</code> objects do not reflect changes made to other
 * <code>TCDBProperties</code> for the same Project/Configuration, so they should not be held
 * on to and be reloaded every time the current values are required.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public class TCDBProperties {

	/** Reference to the parent properties */
	private final TC32ProjectProperties	fParent;

	/** The currently selected <code>ProgrammerConfig</code> */
	private ProgrammerConfig			fProgrammer;

	/** ID of the currently selected <code>ProgrammerConfig</code> */
	private String						fProgrammerId;
	private static final String			KEY_PROGRAMMER				= "ProgrammerID";


	/** No Verify flag. <code>true</code> disables the automatic verify */
	private boolean						fNoVerify;
	private static final String			KEY_NOVERIFY				= "NoVerify";
	private static final boolean		DEFAULT_NOVERIFY			= false;

	/** No Write Mode flag. <code>true</code> inhibits most write actions */
	private boolean						fNoWrite;
	private static final String			KEY_NOWRITE					= "NoWrite";
	private static final boolean		DEFAULT_NOWRITE				= false;
	
	/** No Write Mode flag. <code>true</code> inhibits most write actions */
	private boolean						fUSB;
	private static final String			KEY_USB					   = "USB";
	private static final boolean		DEFAULT_USB			  	   = false;
	
	/** No Write Mode flag. <code>true</code> inhibits most write actions */
	private boolean						fBinary;
	private static final String			KEY_BINARY					= "Binary";
	private static final boolean		DEFAULT_BINARY				= true;
	
	/** NReboot flag. <code>true</code> inhibits most write actions */
	private boolean						fReboot;
	private static final String			KEY_REBOOT					= "Reboot";
	private static final boolean		DEFAULT_REBOOT				= true;
	
	/** No Write Mode flag. <code>true</code> inhibits most write actions */
	private boolean						fBootbin;
	private static final String			KEY_BOOTBIN					= "boot.bin";
	private static final boolean		DEFAULT_BOOTBIN				= false;


	/** No auto chip erase flag. <code>true</code> disables chip erase when writing flash memory. */
	private boolean						fChipErase;
	private static final String			KEY_CHIPERASE				= "ChipErase";
	private static final boolean		DEFAULT_CHIPERASE			= true;

	/** Use Erase Cycle Counter flags. <code>true</code> enables the counter */
	private boolean						fUseCounter;
	private static final String			KEY_USECOUNTER				= "UseCounter";
	private static final boolean		DEFAULT_USECOUNTER			= false;

	/** Write Flash Image flag. <code>true</code> to upload an image file */
	private boolean						fWriteFlash;
	private static final String			KEY_WRITEFLASH				= "WriteFlash";
	private static final boolean		DEFAULT_WRITEFLASH			= true;

	/**
	 * Use Build Config Image flag. <code>true</code> to get the name of the flash image file from
	 * the current <code>IConfiguration</code>
	 */
	private boolean						fFlashFromConfig;
	private static final String			KEY_FLASHFROMCONFIG			= "FlashFromConfig";
	// PS, this is important!
	private static final boolean		DEFAULT_FLASHFROMCONFIG		= false; //true;

	/**
	 * Name of the Flash image file. Only used when <code>fFlashFromConfig</code> is
	 * <code>false</code>
	 */
	private String						fFlashFile; 
	private static final String			KEY_FLASHFILE				= "FlashFile";
	private static final String			DEFAULT_FLASHFILE			= "boot.bin";


	/** Other TCDB options. Free text for TCDB options not directly supported by the plugin. */
	// Let it be the last word, said PS
	private String						fOtherOptions;
	private static final String			KEY_OTHEROPTIONS			= "OtherOptions";
	private static final String			DEFAULT_OTHEROPTIONS		= "-i";
	
	// private String fBinaryTargetName                                = "boot.bin";

	// Unused for now
	// private static final String NODE_CALIBRATION = "CalibrationBytes";
	// private CalibrationBytes fTCDBCalibration;

	/**
	 * The source/target Preferences for the properties or <code>null</code> if default properties
	 * are represented.
	 */
	private final Preferences			fPrefs;

	/** Flag if any properties have been changed */
	private boolean						fDirty;

	/**
	 * Create a new TCDBProperties object and load the properties from the Preferences.
	 * <p>
	 * If the given Preferences has no saved properties yet, the default values are used.
	 * </p>
	 * 
	 * @param prefs
	 *            <code>Preferences</code> to read the properties from.
	 * @param parent
	 *            Reference to the <code>TC32ProjectProperties</code> parent object.
	 */
	public TCDBProperties(Preferences prefs, TC32ProjectProperties parent) {
		fPrefs = prefs;
		fParent = parent;
		loadData();
	}

	/**
	 * Copy constructor.
	 * <p>
	 * Create a new TCDBProperties object and copy the values from the given TCDBProperties
	 * object.
	 * </p>
	 * <p>
	 * All values from the source are copied, except for the source Preferences and the Parent.
	 * </p>
	 * 
	 * @param prefs
	 *            <code>Preferences</code> to read the properties from.
	 * @param parent
	 *            Reference to the <code>TC32ProjectProperties</code> parent object.
	 * @param source
	 *            <code>TCDBProperties</code> object to copy.
	 */
	public TCDBProperties(Preferences prefs, TC32ProjectProperties parent,
			TCDBProperties source) {
		fParent = parent;
		fPrefs = prefs;

		fProgrammer = source.fProgrammer;
		fProgrammerId = source.fProgrammerId;
		fNoVerify = source.fNoVerify;
		fBinary = source.fBinary;
		fUSB = source.fUSB;
		fReboot = source.fReboot;
		fBootbin = source.fBootbin;
		fNoWrite = source.fNoWrite;
		fChipErase = source.fChipErase;
		fUseCounter = source.fUseCounter;

		fWriteFlash = source.fWriteFlash;
		fFlashFromConfig = source.fFlashFromConfig;
		fFlashFile = source.fFlashFile;


		fOtherOptions = source.fOtherOptions;

		// fTCDBCalibration = new
		// CalibrationBytes(source.fTCDBCalibration);

		fDirty = source.fDirty;
	}

	/**
	 * Get a reference to the parent properties.
	 * 
	 * @return <code>TC32ProjectProperties</code>
	 */
	public TC32ProjectProperties getParent() {
		return fParent;
	}

	public ProgrammerConfig getProgrammer() {
		if (fProgrammer == null) {
			return ProgrammerConfigManager.getDefault().getConfig(fProgrammerId);
		}
		return fProgrammer;

	}

	public void setProgrammer(ProgrammerConfig progcfg) {
		if (!progcfg.equals(fProgrammer)) {
			fProgrammer = progcfg;
			fProgrammerId = progcfg.getId();
			fDirty = false;
		}
	}

	public String getProgrammerId() {
		return fProgrammerId;
	}

	public void setProgrammerId(String programmerid) {
		if (!fProgrammerId.equals(programmerid)) {
			fProgrammerId = programmerid;
			fProgrammer = null;
			fDirty = true;
		}
	}


/*
	public boolean getNoVerify() {
		return fNoVerify;
	}

	public void setNoVerify(boolean noverify) {
		if (fNoVerify != noverify) {
			fNoVerify = noverify;
			fDirty = true;
		}
	}

	public boolean getNoWrite() {
		return fNoWrite;
	}

	public void setNoWrite(boolean nowrite) {
		if (fNoWrite != nowrite) {
			fNoWrite = nowrite;
			fDirty = true;
		}
	}
	
*/

	public boolean getChipErase() {
		return fChipErase;
	}
	
	public boolean getBinary() {
		return fBinary;
	}
	
	public boolean getUSB() {
		return fUSB;
	}
	
	public boolean getReboot() {
		return fReboot;
	}
	
	public boolean getBootbin() {
		return fBootbin;
	}


	public void setChipErase(boolean chiperase) {
		if (fChipErase != chiperase) {
			fChipErase = chiperase;
			fDirty = true;
		}
	}
	
	public void setBinary(boolean binary) {
		if (fBinary != binary) {
			fBinary = binary;
			fDirty = true;
		}
	}
	
	public void setUSB(boolean usb) {
		if (fUSB != usb) {
			fUSB = usb;
			fDirty = true;
		}
	}
	
	public void setReboot(boolean reboot) {
		if (fReboot != reboot) {
			fReboot = reboot;
			fDirty = true;
		}
	}
	
	public void setBootbin(boolean bootbin) {
		if (fBootbin != bootbin) {
			fBootbin = bootbin;
			fDirty = true;
		}
	}

	public boolean getUseCounter() {
		return fUseCounter;
	}

	public void setUseCounter(boolean usecounter) {
		if (fUseCounter != usecounter) {
			fUseCounter = usecounter;
			fDirty = true;
		}
	}

	public boolean getWriteFlash() {
		return fWriteFlash;
	}

	public void setWriteFlash(boolean enabled) {
		if (fWriteFlash != enabled) {
			fWriteFlash = enabled;
			fDirty = true;
		}
	}

	public boolean getFlashFromConfig() {
		return fFlashFromConfig;
	}

	public void setFlashFromConfig(boolean useconfig) {
		if (fFlashFromConfig != useconfig) {
			fFlashFromConfig = useconfig;
			fDirty = true;
		}
	}

	public String getFlashFile() {
		return fFlashFile;
	}

	public void setFlashFile(String filename) {
		if (!fFlashFile.equals(filename)) {
			fFlashFile = filename;
			fDirty = true;
		}
	}


	public String getOtherOptions() {
		return fOtherOptions;
	}

	public void setOtherOptions(String otheroptions) {
		if (!fOtherOptions.equals(otheroptions)) {
			fOtherOptions = otheroptions;
			fDirty = true;
		}
	}

	/**
	 * Gets the TCDB command arguments as defined by the properties.
	 * 
	 * @return <code>List&lt;String&gt;</code> with the TCDB options, one per list entry.
	 */
	public List<String> getArguments() {
		List<String> arguments = new ArrayList<String>();

		// Convert the mcu id to the TCDB format and add it
		//String mcuid = fParent.getMCUId();
		//String TCDBmcuid = TCDB.getDefault().getMCUInfo(mcuid);
		//arguments.add("-p" + TCDBmcuid);

		// Add the options from the programmer configuration
		ProgrammerConfig progcfg = getProgrammer();
		if (progcfg != null) {
			arguments.addAll(progcfg.getArguments());
		}

		// add the Simulation / no-write flag
		if (fNoWrite) {
			arguments.add("-n");
			// Add the "no Verify" flag to suppress nuisance error messages
			// (if not already set)
			if (!fNoVerify)
				arguments.add("-V");
		}
		
		// add the binary flag
		if (fBinary) {
			arguments.add("-b");

		}
		
		// add the binary flag
		if (fUSB) {
			arguments.add("-u");

		}
		
		// add the reboot flag
		if (fReboot) {
			arguments.add("-m");

		}

		// add the No Verify flag
		if (fNoVerify) {
			arguments.add("-V");
		}

		// ad the No Chip Erase flag
		if (fChipErase) {
			arguments.add("-e");
		}

		// add the Use Erase Cycle Counter flag
		if (fUseCounter) {
			arguments.add("-y");
		}

		// add the other options field
		if (fOtherOptions.length() > 0) {
			arguments.add(fOtherOptions);
		}

		return arguments;
	}

	/**
	 * Get the list of TCDB action options according to the current properties.
	 * <p>
	 * Currently the following actions are supported:
	 * <ul>
	 * <li>write flash image</li>
	 * </ul>
	 * Only for sections enabled with the <code>setWriteXXXX(true)</code> method will TCDB
	 * actions be created.
	 * </p>
	 * <p>
	 * Macros in the filenames for the flash image files are not resolved. Use
	 * {@link #getActionArguments(IConfiguration, boolean)} to get the arguments with all macros
	 * resolved.
	 * </p>
	 * <p>
	 * This is a convenience method for <code>getArguments(buildcfg, true)</code>
	 * </p>
	 * 
	 * @return <code>List&lt;String&gt;</code> with TCDB action options.
	 */
	public List<String> getActionArguments(IConfiguration buildcfg) {
		return getActionArguments(buildcfg, false);
	}

	public List<String> getActionArguments(IConfiguration buildcfg, boolean resolve) {
		List<String> arguments = new ArrayList<String>();

		TCDBAction action = null;
	
		// PS 
		fWriteFlash = false;
		
		//if (getBootbin())
		//{

		//	action = TCDBActionFactory.writeFlashAction(fFlashFile);
		//} else 
		
		if (fWriteFlash) {
		
			if (fFlashFromConfig) {
				action = TCDBActionFactory.writeFlashAction(buildcfg);
			} else {
				action = TCDBActionFactory.writeFlashAction(fFlashFile);
			}
		}
		
		if (action != null) {
			String argument;
			if (resolve) {
				argument = action.getArgument(buildcfg);
			} else {
				argument = fFlashFile; // PS, we dont need these.    action.getArgument();
			}
			arguments.add(argument);
		}


		return arguments;
	}

	/**
	 * Load all options from the preferences.
	 */
	protected void loadData() {
		fProgrammerId = fPrefs.get(KEY_PROGRAMMER, "");
		fNoVerify = fPrefs.getBoolean(KEY_NOVERIFY, DEFAULT_NOVERIFY);
		fNoWrite = fPrefs.getBoolean(KEY_NOWRITE, DEFAULT_NOWRITE);
		fBinary= fPrefs.getBoolean(KEY_BINARY, DEFAULT_BINARY);
		fUSB= fPrefs.getBoolean(KEY_USB, DEFAULT_USB);
		fReboot= fPrefs.getBoolean(KEY_REBOOT, DEFAULT_REBOOT);
		fBootbin= fPrefs.getBoolean(KEY_BOOTBIN, DEFAULT_BOOTBIN);
		fChipErase = fPrefs.getBoolean(KEY_CHIPERASE, DEFAULT_CHIPERASE);
		fUseCounter = fPrefs.getBoolean(KEY_USECOUNTER, DEFAULT_USECOUNTER);

		fWriteFlash = fPrefs.getBoolean(KEY_WRITEFLASH, DEFAULT_WRITEFLASH);
		fFlashFromConfig = fPrefs.getBoolean(KEY_FLASHFROMCONFIG, DEFAULT_FLASHFROMCONFIG);
		fFlashFile = fPrefs.get(KEY_FLASHFILE, DEFAULT_FLASHFILE);


		fOtherOptions = fPrefs.get(KEY_OTHEROPTIONS, DEFAULT_OTHEROPTIONS);

		// fTCDBCalibration = new
		// CalibrationBytes(fPrefs.node(NODE_CALIBRATION));
		fDirty = false;
	}

	/**
	 * Save the modified properties to the persistent storage.
	 * 
	 * @throws BackingStoreException
	 */
	public void save() throws BackingStoreException {

		try {
			if (fDirty) {
				fDirty = false;

				fPrefs.put(KEY_PROGRAMMER, fProgrammerId);
				fPrefs.putBoolean(KEY_NOVERIFY, fNoVerify);
				fPrefs.putBoolean(KEY_NOWRITE, fNoWrite);
				fPrefs.putBoolean(KEY_CHIPERASE, fChipErase);
				fPrefs.putBoolean(KEY_BINARY, fBinary);
				fPrefs.putBoolean(KEY_USB, fUSB);
				fPrefs.putBoolean(KEY_REBOOT, fReboot);
				fPrefs.putBoolean(KEY_BOOTBIN, fBootbin);
				fPrefs.putBoolean(KEY_USECOUNTER, fUseCounter);

				fPrefs.putBoolean(KEY_WRITEFLASH, fWriteFlash);
				fPrefs.putBoolean(KEY_FLASHFROMCONFIG, fFlashFromConfig);
			    fPrefs.put(KEY_FLASHFILE, fFlashFile);

				fPrefs.put(KEY_OTHEROPTIONS, fOtherOptions);

				fPrefs.flush();

				if (fProgrammer != null) {
					ProgrammerConfigManager.getDefault().saveConfig(fProgrammer);
				}
			}

			// fTCDBCalibration.save();

		} catch (IllegalStateException ise) {
			// This should not happen, but just in case we ignore this unchecked
			// exception
			ise.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(fDirty ? "*" : " ");
		sb.append("[");
		sb.append("ProgrammerID=" + fProgrammerId);
		sb.append("]");
		return sb.toString();
	}

}
