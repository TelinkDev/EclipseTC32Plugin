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
 * $Id: AbstractTool.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.ui.console.MessageConsole;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.core.targets.ITargetConfigConstants;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration;
import com.telink.tc32eclipse.core.targets.ToolManager;
import com.telink.tc32eclipse.core.tcdb.TCDBException;
import com.telink.tc32eclipse.core.tcdb.TCDBException.Reason;
import com.telink.tc32eclipse.core.toolinfo.ExternalCommandLauncher;
import com.telink.tc32eclipse.core.toolinfo.ICommandOutputListener;
/**
* @author Peter Shieh
* @since
* 
*/
public abstract class AbstractTool {

	private final ITargetConfiguration	fHC;

	protected AbstractTool(ITargetConfiguration hc) {
		fHC = hc;
	}

	protected abstract String getName();

	protected abstract String getId();

	/**
	 * Returns the value of the command attribute.
	 * <p>
	 * This is used to get the name of the executable for the tool. The command can be either just
	 * the command name (e.g. 'TCDB') or a absolute path (e.g. '/usr/bin/TCDB')
	 * </p>
	 * 
	 * @return String with the command
	 */
	protected abstract String getCommand();

	protected abstract ICommandOutputListener getOutputListener();

	protected ITargetConfiguration getHardwareConfig() {
		return fHC;
	}

	/**
	 * Runs the tool with the given arguments.
	 * <p>
	 * The Output of stdout and stderr are merged and returned in a <code>List&lt;String&gt;</code>.
	 * </p>
	 * <p>
	 * If the command fails to execute an entry is written to the log and an
	 * {@link TCDBException} with the reason is thrown.
	 * </p>
	 * 
	 * @param arguments
	 *            Zero or more arguments for TCDB
	 * @return A list of all output lines, or <code>null</code> if the command could not be
	 *         launched.
	 * @throws TCDBException
	 *             when TCDB cannot be started or when TCDB returned an
	 */
	public List<String> runCommand(String... arguments) throws TCDBException {

		List<String> arglist = new ArrayList<String>(1);
		for (String arg : arguments) {
			arglist.add(arg);
		}

		return runCommand(arglist, new NullProgressMonitor(), false, null);
	}

	/**
	 * Runs tool with the given arguments.
	 * <p>
	 * The Output of stdout and stderr are merged and returned in a <code>List&lt;String&gt;</code>.
	 * If the "use Console" flag is set in the Preferences, the complete output is shown on a
	 * Console as well.
	 * </p>
	 * <p>
	 * If the command fails to execute an entry is written to the log and an
	 * {@link TCDBException} with the reason is thrown.
	 * </p>
	 * 
	 * @param arguments
	 *            <code>List&lt;String&gt;</code> with the arguments
	 * @param monitor
	 *            <code>IProgressMonitor</code> to cancel the running process.
	 * @param forceconsole
	 *            If <code>true</code> all output is copied to the console, regardless of the "use
	 *            console" flag.
	 * @param cwd
	 *            <code>IPath</code> with a current working directory or <code>null</code> to use
	 *            the default working directory (usually the one defined with the system property
	 *            <code>user.dir</code). May not be empty.
	 * @return A list of all output lines, or <code>null</code> if the command could not be
	 *         launched.
	 * @throws TCDBException
	 *             when the tool cannot be started or when it returns with an error.
	 */
	public List<String> runCommand(List<String> arglist, IProgressMonitor monitor,
			boolean forceconsole, IPath cwd) throws TCDBException {

		try {
			monitor.beginTask("Running " + getName(), 100);

			// Check if the CWD is valid
			if (cwd != null && cwd.isEmpty()) {
				throw new TCDBException(Reason.INVALID_CWD,
						"CWD does not point to a valid directory.");
			}

			// TODO: resolve variables in the path
			String command = getCommand();

			// Set up the External Command
			ExternalCommandLauncher launcher = new ExternalCommandLauncher(command, arglist, cwd);
			launcher.redirectErrorStream(true);

			// Set the Console (if requested by the user for the target configuratio)
			MessageConsole console = null;
			String consoleattr = getId() + ".useconsole";
			boolean useconsole = fHC.getBooleanAttribute(consoleattr);
			if (useconsole || forceconsole) {
				console = TC32Plugin.getDefault().getConsole("External Tools");
				launcher.setConsole(console);
			}

			ICommandOutputListener outputlistener = getOutputListener();
			outputlistener.init(monitor);
			launcher.setCommandOutputListener(outputlistener);

			// USB devices:
			// This will delay the actual call if the previous call finished less than the
			// user provided time in milliseconds
			//TCDBInvocationDelay(console, new SubProgressMonitor(monitor, 10));

			// Run TCDB
			try {
				int result = launcher.launch(new SubProgressMonitor(monitor, 80));

				// Test if launch was aborted
				Reason abortReason = outputlistener.getAbortReason();
				if (abortReason != null) {
					throw new TCDBException(abortReason, outputlistener.getAbortLine());
				}

				if (result == -1) {
					throw new TCDBException(Reason.USER_CANCEL, "");
				}
			} catch (IOException e) {
				// Something didn't work while running the external command
				throw new TCDBException(Reason.NO_TCDB_FOUND,
						"Cannot run TCDB executable. Please check the TC32 path preferences.", e);
			}

			// Everything was fine: get the ooutput from TCDB and return it
			// to the caller
			List<String> stdout = launcher.getStdOut();

			monitor.worked(10);

			return stdout;
		} finally {
			monitor.done();
			String progport = fHC.getAttribute(ITargetConfigConstants.ATTR_PROGRAMMER_PORT);
			ToolManager.getDefault().setLastAccess(progport, System.currentTimeMillis());
		}
	}


}
