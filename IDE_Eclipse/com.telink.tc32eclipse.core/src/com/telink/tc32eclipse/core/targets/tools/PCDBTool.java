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
 * $Id: AvariceTool.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.telink.tc32eclipse.core.targets.IGDBServerTool;
import com.telink.tc32eclipse.core.targets.IProgrammer;
import com.telink.tc32eclipse.core.targets.IProgrammerTool;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration.Result;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration.ValidationResult;
import com.telink.tc32eclipse.core.tcdb.TCDBException;
import com.telink.tc32eclipse.core.toolinfo.ICommandOutputListener;

/**
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class PCDBTool extends AbstractTool implements IProgrammerTool, IGDBServerTool {

	public final static String			ID					= "TC32eclipse.pcdb";

	public final static String			NAME				= "PCDB";

	public final static String			ATTR_CMD_NAME		= ID + ".command";
	private final static String			DEF_CMD_NAME		= "pcdb";

	public final static String			ATTR_USE_CONSOLE	= ID + ".useconsole";
	public final static String			DEF_USE_CONSOLE		= Boolean.toString(true);
	// Change to false for release

	private final static String[]		ALL_ATTRS			= new String[] { ATTR_CMD_NAME,
			ATTR_USE_CONSOLE								};

	private ICommandOutputListener		fOutputListener		= new PCDBOutputListener();

	private Set<String>					fProgrammerIds;

	/** Cache of all Name/Version strings, mapped to their respective command name. */
	private Map<String, String>			fNameVersionMap		= new HashMap<String, String>();

	/** Cache of all MCU Sets, mapped to their respective command name */
	private Map<String, Set<String>>	fMCUMap				= new HashMap<String, Set<String>>();

	public PCDBTool(ITargetConfiguration hc) {
		super(hc);
		// tell the hardware configuration about the avarice attributes and their default values.
		// String[][] avariceattrs = new String[][] { { ATTR_CMD_NAME, DEF_CMD_NAME },
		// { ATTR_USE_CONSOLE, DEF_USE_CONSOLE } };
		// getHardwareConfig().addAttributes(this, avariceattrs);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getId()
	 */
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getName()
	 */
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IAttributeProvider#getAttributes()
	 */
	public String[] getAttributes() {
		return ALL_ATTRS;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IAttributeProvider#getDefaultValue(java.lang.String)
	 */
	public String getDefaultValue(String attribute) {
		String defaultvalue = null;
		if (ATTR_CMD_NAME.equals(attribute)) {
			defaultvalue = DEF_CMD_NAME;
		} else if (ATTR_USE_CONSOLE.equals(attribute)) {
			defaultvalue = DEF_USE_CONSOLE;
		}

		return defaultvalue;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.tools.AbstractTool#getCommand(com.telink.tc32eclipse.core.targets
	 * .ITargetConfiguration)
	 */
	public String getCommand() {
		String command = getHardwareConfig().getAttribute(ATTR_CMD_NAME);
		if (command == null || command.length() == 0) {
			command = DEF_CMD_NAME;
		}
		return command;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getVersion(com.telink.tc32eclipse.
	 * core.targets.ITargetConfiguration)
	 */
	public String getVersion() throws TCDBException {

		String cmd = getCommand();

		// Check if we already have the version in the cache
		if (fNameVersionMap.containsKey(cmd)) {
			return fNameVersionMap.get(cmd);
		}

		// Execute avarice without any options
		// The name / version are in the first full line of the output in the format
		// "AVaRICE version 2.8, Nov  7 2008 22:02:05"
		String name = null;
		List<String> stdout = runCommand("");

		if (stdout != null) {
			// look for a line matching "*Version TheVersionNumber *"
			Pattern mcuPat = Pattern.compile(".*version\\s+([\\w\\.]+).*");
			Matcher m;
			for (String line : stdout) {
				m = mcuPat.matcher(line);
				if (!m.matches()) {
					continue;
				}
				name = getName() + " " + m.group(1);
				break;
			}
		}
		if (name == null) {
			// could not read the version from the output, probably the regex has a
			// mistake. Return a reasonable default.
			return getName() + " ?.?";
		}

		fNameVersionMap.put(cmd, name);
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.tools.AbstractTool#getOutputListener()
	 */
	@Override
	protected ICommandOutputListener getOutputListener() {
		return fOutputListener;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getMCUs()
	 */
	public Set<String> getMCUs() throws TCDBException {

		String cmd = getCommand();

		if (fMCUMap.containsKey(cmd)) {
			return fMCUMap.get(cmd);
		}

		Set<String> allmcus = new HashSet<String>();
		List<String> stdout;

		stdout = runCommand("--known-devices");

		if (stdout != null) {
			// look for a line matching alphanumeric characters (the mcu id) followed by whitespaces
			// and "0x" (the beginning of the device id field)
			Pattern mcuPat = Pattern.compile("(\\w+)\\s+0x.+");
			Matcher m;
			for (String line : stdout) {
				m = mcuPat.matcher(line);
				if (!m.matches()) {
					continue;
				}
				allmcus.add(m.group(1));
			}
		}
		fMCUMap.put(cmd, allmcus);
		return allmcus;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getProgrammers(com.telink.tc32eclipse
	 * .core.targets.ITargetConfiguration)
	 */
	public Set<String> getProgrammers() throws TCDBException {
		if (fProgrammerIds == null) {
			fProgrammerIds = new HashSet<String>();
			for (PCDBProgrammers progger : PCDBProgrammers.values()) {
				fProgrammerIds.add(progger.getId());
			}
		}
		return fProgrammerIds;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getProgrammer(com.telink.tc32eclipse
	 * .core.targets.ITargetConfiguration, java.lang.String)
	 */
	public IProgrammer getProgrammer(String id) throws TCDBException {

		// Quick check if the programmer id is actually supported by avarice
		if (!getProgrammers().contains(id)) {
			return null;
		}

		IProgrammer progger = PCDBProgrammers.valueOf(id);

		return progger;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#validate(com.telink.tc32eclipse.core
	 * .targets.ITargetConfiguration, java.lang.String)
	 */
	public ValidationResult validate(String attr) {

		Result result = Result.OK;
		String description = "";

		if (ATTR_CMD_NAME.equals(attr)) {
			// Check that the command is valid by executing avarice with parameter "--known-devices" and check
			// that the output is long enough.
			try {
				List<String> stdout;

				stdout = runCommand("--known-devices");

				if (stdout.size() < 10) {
					result = Result.ERROR;
					description = "Not a TC32 executable";
				}
			} catch (TCDBException ade) {
				// avarice not found
				result = Result.ERROR;
				description = "Invalid Path";
			}
		} else if (ATTR_USE_CONSOLE.equals(attr)) {
			// USE_CONSOLE is always valid
		} else {
			result = Result.UNKNOWN_ATTRIBUTE;
		}

		return new ValidationResult(result, description);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IGDBServerTool#isSimulator()
	 */
	public boolean isSimulator() {
		return false;
	}

}
