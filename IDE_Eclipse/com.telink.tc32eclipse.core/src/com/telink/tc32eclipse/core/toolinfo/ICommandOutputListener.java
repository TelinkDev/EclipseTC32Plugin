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
 * $Id: ICommandOutputListener.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.toolinfo;

import org.eclipse.core.runtime.IProgressMonitor;

import com.telink.tc32eclipse.core.tcdb.TCDBException.Reason;

/**
 * Listen to the output of a {@link ExternalCommandLauncher} line by line.
 * <p>
 * Implementors can listen to the output of a external program line by line to - for example -
 * update the user interface accordingly.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public interface ICommandOutputListener {

	public enum StreamSource {
		STDOUT, STDERR;
	}

	/**
	 * Sets the progress monitor for the listener. The listener can use the monitor to abort the
	 * current launch when it detects errors.
	 * 
	 * @param monitor
	 */
	public void init(IProgressMonitor monitor);

	/**
	 * @param line
	 *            The current line from the output of the external program.
	 * @param source
	 *            A <code>StreamSource</code> to indicate whether the line came from
	 *            {@link StreamSource#STDOUT} or from {@link StreamSource#STDERR}.
	 */
	public void handleLine(String line, StreamSource source);

	/**
	 * Gets the last abort reason.
	 * 
	 * @return The last abort reason or <code>null</code> if no errors since init.
	 */
	public Reason getAbortReason();

	/**
	 * Returns the line from the output that caused the abort.
	 * 
	 * @return
	 */
	public String getAbortLine();

}
