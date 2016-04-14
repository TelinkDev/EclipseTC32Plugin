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
 * $Id: GCC.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.toolinfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IPath;

import com.telink.tc32eclipse.PluginIDs;
import com.telink.tc32eclipse.core.IMCUProvider;
import com.telink.tc32eclipse.core.paths.TC32Path;
import com.telink.tc32eclipse.core.paths.TC32PathProvider;
import com.telink.tc32eclipse.core.paths.IPathProvider;
import com.telink.tc32eclipse.core.util.TC32MCUidConverter;

/**
 * This class provides some information about the used gcc compiler in the toolchain.
 * <p>
 * It can return a list of all supported target mcus.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public class GCC extends BaseToolInfo implements IMCUProvider {

	private static final String	TOOL_ID			= PluginIDs.PLUGIN_TOOLCHAIN_TOOL_COMPILER;

	private static GCC			instance		= null;

	private Map<String, String>	fMCUmap			= null;

	private IPath				fCurrentPath	= null;

	private final IPathProvider	fPathProvider	= new TC32PathProvider(TC32Path.TC32_GCC);

	/**
	 * Get an instance of this Tool.
	 */
	public static GCC getDefault() {
		if (instance == null)
			instance = new GCC();
		return instance;
	}

	private GCC() {
		// Let the superclass get the command name
		super(TOOL_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.toolinfo.IToolInfo#getToolPath()
	 */
	@Override
	public IPath getToolFullPath() {
		IPath path = fPathProvider.getPath();
		return path.append(getCommandName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#getMCUInfo(java.lang.String)
	 */
	public String getMCUInfo(String mcuid) {
		try {
			Map<String, String> internalmap = loadMCUList();
			return internalmap.get(mcuid);
		} catch (IOException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#getMCUList()
	 */
	public Set<String> getMCUList() throws IOException {
		Map<String, String> internalmap = loadMCUList();
		Set<String> idlist = internalmap.keySet();
		return new HashSet<String>(idlist);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.IMCUProvider#hasMCU(java.lang.String)
	 */
	public boolean hasMCU(String mcuid) {
		try {
			Map<String, String> internalmap = loadMCUList();
			return internalmap.containsKey(mcuid);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * @return Map &lt;mcu id, UI name&gt; of all supported MCUs
	 */
	private Map<String, String> loadMCUList() throws IOException {

		if (!getToolFullPath().equals(fCurrentPath)) {
			// toolpath has changed, reload the list
			fMCUmap = null;
			fCurrentPath = getToolFullPath();
		}

		if (fMCUmap != null) {
			// return stored map
			return fMCUmap;
		}

		fMCUmap = new HashMap<String, String>();

		// Execute tc32-elf-tccwith the "--target-help" option and parse the
		// output
		List<String> stdout = runCommand("--target-help");
		if (stdout == null) {
			// Return empty map on failures
			return fMCUmap;
		}

		boolean start = false;

		// The parsing is done by reading the output line by line until a line
		// with "Known MCU names:" is found. Then the parsing starts and all
		// following lines are split into the mcu ids until a line is reached
		// that does not start with a space (the mcu id lines start always with
		// a space).
		//
		// Maybe this could be done with a Pattern matcher, but I don't know how
		// to do multiline pattern matching and this is probably faster anyway
		for (String line : stdout) {
			if ("Known MCU names:".equals(line)) {
				start = true;
			} else if (start && !line.startsWith(" ")) {
				// finished
				start = false;
			} else if (start) {
				String[] names = line.split(" ");
				for (String mcuid : names) {
					String mcuname = TC32MCUidConverter.id2name(mcuid);
					if (mcuname == null) {
						// some mcuid are generic and should not be
						// included
						continue;
					}
					fMCUmap.put(mcuid, mcuname);
				}
			} else {
				// a line outside of the "Known MCU names:" section
			}
		}

		return fMCUmap;
	}

	/**
	 * Get the command name and the current version of GCC.
	 * <p>
	 * The name comes from the buildDefinition. The version is gathered by executing with the "-v"
	 * option and parsing the output.
	 * </p>
	 * 
	 * @return <code>String</code> with the command name and version
	 * @throws IOException
	 *             if the tc32-elf-tcccommand could not be executed.
	 */
	public String getNameAndVersion() throws IOException {

		// Execute tc32-elf-tccwith the "-v" option and parse the
		// output
		List<String> stdout = runCommand("-v");
		if (stdout == null) {
			// Return default name on failures
			return getCommandName() + " n/a";
		}

		// look for a line matching "gcc version TheVersionNumber"
		Pattern mcuPat = Pattern.compile("gcc version\\s*(.*)");
		Matcher m;
		for (String line : stdout) {
			m = mcuPat.matcher(line);
			if (!m.matches()) {
				continue;
			}
			return getCommandName() + " " + m.group(1);
		}

		// could not read the version from the output, probably the regex has a
		// mistake. Return a reasonable default.
		return getCommandName() + "?.?";
	}

	/**
	 * Runs the GCC with the given arguments.
	 * <p>
	 * The Output of stdout and stderr are merged and returned in a <code>List&lt;String&gt;</code>.
	 * </p>
	 * <p>
	 * If the command fails to execute an entry is written to the log and <code>null</code> is
	 * returned
	 * 
	 * @param arguments
	 *            Zero or more arguments for gcc
	 * @throws IOException
	 *             if the command could not be executed
	 * @return A list of all output lines, or <code>null</code> if the command could not be
	 *         launched.
	 */
	private List<String> runCommand(String... arguments) throws IOException {

		// PS String command = getToolFullPath().toOSString();
		String command = getToolFullPath().toPortableString();
		List<String> arglist = new ArrayList<String>(1);
		for (String arg : arguments) {
			arglist.add(arg);
		}

		ExternalCommandLauncher gcc = new ExternalCommandLauncher(command, arglist);
		gcc.redirectErrorStream(true);
		gcc.launch();

		List<String> stdout = gcc.getStdOut();

		return stdout;
	}
}
