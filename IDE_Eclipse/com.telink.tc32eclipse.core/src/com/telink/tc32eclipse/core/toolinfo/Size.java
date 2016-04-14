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
 * $Id: Size.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.toolinfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.PluginIDs;
import com.telink.tc32eclipse.core.paths.TC32Path;
import com.telink.tc32eclipse.core.paths.TC32PathProvider;
import com.telink.tc32eclipse.core.paths.IPathProvider;

/**
 * This class provides some information about the used size tool in the toolchain.
 * 
 * It can return a list of all supported format options.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class Size extends BaseToolInfo {

	private static final String	TOOL_ID			= PluginIDs.PLUGIN_TOOLCHAIN_TOOL_SIZE;

	private Map<String, String>	fOptionsMap		= null;

	private static Size			instance		= null;

	private final IPathProvider	fPathProvider	= new TC32PathProvider(TC32Path.TC32_GCC);

	/**
	 * Get an instance of this Tool.
	 */
	public static Size getDefault() {
		if (instance == null)
			instance = new Size();
		return instance;
	}

	private Size() {
		// Let the superclass get the command name
		super(TOOL_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telink.tc32eclipse.core.toolinfo.IToolInfo#getToolFullPath()
	 */
	@Override
	public IPath getToolFullPath() {
		IPath path = fPathProvider.getPath();
		return path.append(getCommandName());
	}

	/**
	 * @return true if this size tool supports the -format=TC32 option.
	 */
	//public boolean hasTC32Option() {

	//	return false; //getSizeOptions().containsValue("TC32");
	//}

	/**
	 * @return Map &lt;UI-name, option-name&gt; with all supported size options.
	 */
	public Map<String, String> getSizeOptions() {

		if (fOptionsMap != null) {
			return fOptionsMap;
		}

		fOptionsMap = new HashMap<String, String>();

		// Execute tc32-elf-tcc with the "--target-help" option and parse the
		// output
		String command = getToolFullPath().toOSString();
		List<String> argument = new ArrayList<String>(1);
		argument.add("-h");
		ExternalCommandLauncher size = new ExternalCommandLauncher(command, argument);

		// At least in TC32Win TC32-size -h will print to the error stream!
		size.redirectErrorStream(true);
		try {
			size.launch();
		} catch (IOException e) {
			// Something didn't work while running the external command
			IStatus status = new Status(Status.ERROR, TC32Plugin.PLUGIN_ID, "Could not start "
					+ command, e);
			TC32Plugin.getDefault().log(status);
			return fOptionsMap;
		}

		List<String> stdout = size.getStdOut();

		for (String line : stdout) {
			if (line.contains("--format=")) {
				// this is the line we are looking for
				// extract the format options
				int start = line.indexOf('{');
				int end = line.lastIndexOf('}');
				String options = line.substring(start + 1, end);
				// next line does not work and i am no regex expert
				// to know how to split at a "|"
				// String[] allopts = options.split("|");
				int splitter = 0;
				while ((splitter = options.indexOf('|')) != -1) {
					String opt = options.substring(0, splitter);
					fOptionsMap.put(convertOption(opt), opt);
					options = options.substring(splitter + 1);
				}
				fOptionsMap.put(convertOption(options), options);
				break;
			}
		}

		return fOptionsMap;
	}

	/**
	 * Get a better name for known format options.
	 * 
	 * @param option
	 * @return String with the UI name of the Option
	 */
	private static String convertOption(String option) 
	{
		if ("berkeley".equals(option)) {
			return "Berkeley Format";
		}
		if ("sysv".equals(option)) {
			return "SysV Format";
		}

		// unknown option
		// log a message telling the user to report this new option for inclusion into the list
		// above (as if anyone would actually read the log)
		IStatus status = new Status(
				IStatus.INFO,
				TC32Plugin.PLUGIN_ID,
				"Size encountered an unknown option for TC32-size ["
						+ option
						+ "]. Please report this to the TC32 Eclipse plugin maintainer to include this option in future versions of the plugin.",
				null);
		TC32Plugin.getDefault().log(status);

		return option;
	}
}
