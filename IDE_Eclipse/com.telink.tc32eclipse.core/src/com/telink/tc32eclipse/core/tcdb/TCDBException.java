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
package com.telink.tc32eclipse.core.tcdb;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Wrapper for all Exceptions that may be thrown when accessing TCDB.
 * <p>
 * This Exceptions contains a reason, set when creating the Exception and readable with
 * {@link #getReason()}. This is used by the {@link TCDBErrorDialog} to display a human readable
 * detailed description of the error.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TCDBException extends Exception {

	private static final long	serialVersionUID	= 1L;

	public enum Reason {
		UNKNOWN, NO_TCDB_FOUND, CANT_ACCESS_TCDB, CONFIG_NOT_FOUND, UNKNOWN_MCU, UNKNOWN_PROGRAMMER, NO_PROGRAMMER, PORT_BLOCKED, NO_USB, TIMEOUT, PARSE_ERROR, INVALID_CWD, USER_CANCEL, SYNC_FAIL, INIT_FAIL, NO_TARGET_POWER, INVALID_PORT, USB_RECEIVE_ERROR;
	}

	/** The Reason for the exception */
	private Reason	fReason;

	/**
	 * Instantiate a new TCDBException with the given reason.
	 * 
	 * @param reason
	 *            Enum <code>Reason</code> for the reason of this Exception.
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            <code>getMessage()</code> method).
	 */
	public TCDBException(Reason reason, String message) {
		this(reason, message, null);
	}

	/**
	 * Instantiate a new TCDBException with the given reason and the root Exception.
	 * 
	 * @param reason
	 *            Enum <code>Reason</code> for the reason of this Exception.
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            <code>getMessage()</code> method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the <code>getCause()</code>
	 *            method). (A <code>null</code> value is permitted, and indicates that the cause is
	 *            nonexistent or unknown.)
	 */
	public TCDBException(Reason reason, String message, Throwable cause) {
		super(message, cause);
		fReason = reason;
	}

	/**
	 * Instantiate a new TCDBException with the given root Exception.
	 * <p>
	 * If the given Exception matches some predefined Exceptions, a reason will be set. Otherwise
	 * <code>Reason.UNKNOWN</code> will be used.
	 * </p>
	 * 
	 * @param exc
	 *            Root <code>Exception</code>
	 */
	public TCDBException(Exception exc) {
		super(exc);
		if (exc instanceof FileNotFoundException) {
			fReason = Reason.NO_TCDB_FOUND;
		} else if (exc instanceof IOException) {
			fReason = Reason.CANT_ACCESS_TCDB;
		} else {
			fReason = Reason.UNKNOWN;
		}
	}

	/**
	 * Get the reason for this Exception.
	 * 
	 * @return Enum <code>Reason</code>
	 */
	public Reason getReason() {
		return fReason;
	}
}
