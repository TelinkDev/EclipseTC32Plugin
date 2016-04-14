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
 * $Id: PCDB.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.toolinfo;

import java.io.IOException;
import org.eclipse.core.runtime.IPath;
import com.telink.tc32eclipse.core.IMCUProvider;
import com.telink.tc32eclipse.core.paths.TC32Path;
import com.telink.tc32eclipse.core.paths.TC32PathProvider;
import com.telink.tc32eclipse.core.paths.IPathProvider;
import com.telink.tc32eclipse.core.tcdb.TCDBException;



/**
 * This class handles all interactions with the TC32Tools program.
 * <p>
 * It implements the {@link IMCUProvider} Interface to get a list of all MCUs supported by the
 * selected version of TC32Tools. Additional methods are available to get a list of all supported
 * Programmers.
 * </p>
 * <p>
 * This class implements the Singleton pattern. Use the {@link #getDefault()} method to get the
 * instance of this class.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public class FindTDebug {

	/** The singleton instance of this class */
	private static FindTDebug	instance	= null;

	/** The preference store for TC32Tools */
	//private final IPreferenceStore		fPrefsStore;
	

	/** The name of the TC32Tools executable */
	private final static String				fCommandName		= "tdebug.exe";

	/** The Path provider for the TC32Tools executable */
	private final IPathProvider				fPathProvider		= new TC32PathProvider(TC32Path.TC32_TOOLS);


	/**
	 * Get the singleton instance of the TC32Tools class.
	 */
	public static FindTDebug getDefault() {
		if (instance == null)
			instance = new FindTDebug();
		return instance;
	}

	// Prevent Instantiation of the class
	private FindTDebug() {
		//fPrefsStore = TC32ToolsPreferences.getPreferenceStore();
	}

	/**
	 * Returns the name of the TC32Tools executable.
	 * <p>
	 * On Windows Systems the ".exe" extension is not included and needs to be added for access to
	 * TC32Tools other than executing the programm.
	 * </p>
	 * 
	 * @return String with "TC32Tools"
	 */
	public String getCommandName() {
		return fCommandName;
	}

	/**
	 * Returns the full path to the TC32Tools executable.
	 * <p>
	 * Note: On Windows Systems the returned path does not include the ".exe" extension.
	 * </p>
	 * 
	 * @return <code>IPath</code> to the TC32Tools executable
	 */
	public IPath getToolFullPath() {
		IPath path = fPathProvider.getPath();
		return path.append(getCommandName());
	}
	

	/**
	 * Runs wtcdb with the given arguments and using the tools directory as working directory
	 * <p>
	 * If the command fails to execute an entry is written to the log and an
	 * {@link TCDBException} with the reason is thrown.
	 * </p>
	 * 
	 * @param arguments
	 *            <code>List&lt;String&gt;</code> with the arguments
	 * @throws IOException 
	 */
		public boolean run(String arg) throws IOException {
	
			try {
	
				String command = getToolFullPath().toOSString();
	
				Runtime.getRuntime().exec(command, null, fPathProvider.getPath().toFile());
				
				return true;
			} catch (IOException e)
			{
				return false;
			}
	
		}

	}



