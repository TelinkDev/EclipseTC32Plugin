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

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.core.preferences.TCDBPreferences;
import com.telink.tc32eclipse.core.tcdb.TCDBException;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfig;

/**
* Display an TCDB Error.
* <p>
* This Dialog knows how interpret an {@link TCDBException} and will display a human readable
* message for the reason of the Exception.
* </p>
* 
* @author Peter Shieh
* @since 0.1
*/
public class TCDBErrorDialog extends ErrorDialog {

	/**
	 * Instantiate a new TCDBErrorDialog.
	 * <p>
	 * The Dialog is not shown until the <code>open()</code> method has been called.
	 * </p>
	 * 
	 * @param parentShell
	 *            <code>Shell</code> in which the dialog is opened.
	 * @param message
	 *            The message of the Dialog.
	 * @param status
	 *            The Status with the root cause.
	 */
	protected TCDBErrorDialog(Shell parentShell, String message, IStatus status) {
		super(parentShell, "TCDB Error", message, status, IStatus.OK | IStatus.INFO
				| IStatus.WARNING | IStatus.ERROR);
	}

	/**
	 * Open an Error Dialog for an TCDBException.
	 * <p>
	 * This method will take the Exception reason from the given {@link TCDBException} and
	 * display a human readable message.
	 * </p>
	 * <p>
	 * This Dialog is modal and will block until OK is clicked or the dialog is closed with ESC or
	 * the window close button.
	 * </P>
	 * 
	 * @param parent
	 *            the parent shell of the dialog, or <code>null</code> if none
	 * @param exc
	 *            The <code>TCDBException</code> that contains the root cause.
	 * @param config
	 *            The <code>ProgrammerConfig</code> in use while the Exception occured. Used for
	 *            more detailed error messages and may be <code>null</code> if not available.
	 */
	public static void openTCDBError(Shell parentShell, Throwable exc, ProgrammerConfig config) {
		String message, source;

		if (exc instanceof TCDBException) {
			TCDBException TCDBexc = (TCDBException) exc;

			// Get the Programmer id and the port from the given
			// ProgrammerConfig (if not null)
			// These are used below for more detailed error messages
			String programmer = "";
			String port = "";
			if (config != null) {
				programmer = "\"" + config.getProgrammer() + "\"";
				//port = config.getPort().equals("") ? "" : "\"" + config.getPort() + "\"";
			}

			// Also a custom TCDB configfile might be the cause of errors.
			String customconfig = "";
			IPreferenceStore TCDBstore = TCDBPreferences.getPreferenceStore();
			if (TCDBstore.getBoolean(TCDBPreferences.KEY_USECUSTOMCONFIG)) {
				customconfig = TCDBstore.getString(TCDBPreferences.KEY_CONFIGFILE);
			}

			// The nice thing about enums: using them in a switch statement!
			switch (TCDBexc.getReason()) {
				case UNKNOWN:
					message = "An error occured while accessing TCDB.\n\n"
							+ "See below for details.";
					break;

				case NO_TCDB_FOUND:
					message = "TCDB executable can not be found.\n\n"
							+ "Check in the TC32 Preferences if the path to TCDB is correct.";
					break;

				case CANT_ACCESS_TCDB:
					message = "TCDB executable can not be accessed.\n\n"
							+ "Check in the TC32 Preferences if the path to TCDB is correct\n"
							+ "(Window > Preferences... -> TC32 -> Paths)";
					break;

				case CONFIG_NOT_FOUND:
					if (customconfig.length() == 0) {
						message = "TCDB can not find its default configuration file.\n\n"
								+ "Check your TCDB setup.";
					} else {
						source = "TCDB can not find configuration file [{0}].\n\n"
								+ "Check in the TCDB Preferences if the path to the custom TCDB configuration file is correct\n"
								+ "(Window > Preferences... -> TC32 -> TCDB)";
						message = MessageFormat.format(source, customconfig);
					}
					break;

				case NO_PROGRAMMER:
					message = "No Programmer selected\n\n"
							+ "Check the TCDB properties for the project.";
					break;

				case UNKNOWN_PROGRAMMER:
					source = "TCDB does not recognize the selected programmer id {0}\n\n"
							+ "Check the current Programmer Configuration.";
					message = MessageFormat.format(source, programmer);
					break;

				case UNKNOWN_MCU:
					message = "TCDB does not recognize the selected MCU type.\n\n"
							+ "Check the TC32 Target Hardware settings if the selected MCU is supported by TCDB.";
					break;

				case TIMEOUT:
					source = "Operation timed out while trying to access the TCDB programmer {0}\n\n"
							+ "Check that the Programmer is connected and switched on.";
					message = MessageFormat.format(source, programmer);
					break;

				case PORT_BLOCKED:
					source = "The port {0} for the Programmer {1} is blocked.\n\n"
							+ "Check that no other instances of TCDB or any other programm is using the port";
					message = MessageFormat.format(source, port, programmer);
					break;

				case NO_USB:
					source = "Could not open the USB device with the port name {0}.\n\n"
							+ "Please check that\n"
							+ " - the programmer device is connected an switched on\n\n"
							+ "Also check in the configuration for programmer {1} that\n"
							+ " - the port name is correct\n"
							+ " - the \"Delay between TCDB invocations\" is set to a sufficently high value";
					message = MessageFormat.format(source, port, programmer);
					break;

				case PARSE_ERROR:
					message = "Could not understand the output from TCDB.\n\n"
							+ " - Either TCDB returned an error message that is not covered by the TC32 Eclipse Pluguin\n"
							+ " - or you have a newer TCDB version with a changed output format.\n\n"
							+ "In either case check the TCDB return message below and contact\n";
					break;

				case INVALID_CWD:
					message = "Invalid Build directory.\n"
							+ "Please check the Build directory on the \"Builder settings\" tab\n"
							+ "(Project -> Properties -> C Build)";
					break;

				case USER_CANCEL:
					message = "Operation cancelled.\n";
					break;

				case SYNC_FAIL:
					source = "Programmer {0} could not connect to the target hardware.\n\n"
							+ "Please check that the target hardware is connected correctly.";
					message = MessageFormat.format(source, programmer);
					break;

				case INIT_FAIL:
					source = "Programmer {0} could not initialize the target hardware.\n\n"
							+ "Please check that the target hardware is connected correctly.";
					message = MessageFormat.format(source, programmer);
					break;

				case NO_TARGET_POWER:
					source = "Target Hardware is not powered.\n\n"
							+ "Please check that the target hardware is connected correctly and has power.";
					message = MessageFormat.format(source, programmer);
					break;

				case INVALID_PORT:
					if (port.length() == 0) {
						// The user has not specified a port, so the OS default is used (but seemingly invalid).
						// Try to get the port used by TCDB from the TCDB output
						String abortline = TCDBexc.getMessage();
						String[] split = abortline.split("\"");
						if (split.length >= 2) {
							port = split[1];
						} else {
							port = "???";
						}
					}
					source = "TCDB has problems accessing the port \"{0}\".\n\n"
							+ "Please check that the correct port has been selected in the programmer configuration.";
					message = MessageFormat.format(source, port);
					break;
					
				case USB_RECEIVE_ERROR:
					message = "Problems receiving data from USB\n\n";
					break;
					
				default:
					message = "An unhandled Error occured while accessing TCDB.\n\n";
			}

		} else {
			// The throwable is not an instance of TCDBException
			// Why does the caller think this class is called
			// TCDBErrorDialog?
			// Nevertheless we just display the message from the Throwable
			message = exc.getLocalizedMessage();
		}

		// Set the status for the dialog
		IStatus status = new Status(IStatus.ERROR, TC32Plugin.PLUGIN_ID, exc.getLocalizedMessage(),
				exc.getCause());

		// Now open the Dialog.
		// while dialog.open() will return something, we don't care if the user
		// has pressed OK or ESC or the window close button.
		ErrorDialog dialog = new TCDBErrorDialog(parentShell, message, status);
		dialog.open();
		return;
	}
}
