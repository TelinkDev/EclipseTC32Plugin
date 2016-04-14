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
package com.telink.tc32eclipse.ui.dialogs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

import com.telink.tc32eclipse.core.tcdb.TCDBException;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfig;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfigManager;

/**
 * Displays an Error Message box for am <code>TCDBException</code>.
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TCDBErrorDialogJob extends UIJob {

	private TCDBException fException;

	private ProgrammerConfig fProgConfig;

	/**
	 * Create a new Job to display an TCDBErrorDialog.
	 * 
	 * @param jobDisplay
	 *            The <code>Display</code> to show the message on.
	 * @param exception
	 *            The Exception for which to display the error dialog.
	 * @param programmerconfigid
	 *            The id of the programmer in use when the Exception was thrown.
	 *            Used for some error messages.
	 */
	public TCDBErrorDialogJob(Display jobDisplay,
			TCDBException exception, String programmerconfigid) {
		super(jobDisplay, "TCDB Error");
		fException = exception;
		fProgConfig = ProgrammerConfigManager.getDefault().getConfig(
				programmerconfigid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {

		TCDBErrorDialog.openTCDBError(getDisplay().getActiveShell(),
				fException, fProgConfig);
		return Status.OK_STATUS;
	}
}
