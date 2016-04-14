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
 * $Id: TC32HardwareConfigValidator.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;

//import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.telink.tc32eclipse.core.targets.ITargetConfiguration.Result;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration.ValidationResult;
import com.telink.tc32eclipse.core.tcdb.TCDBException;

/**
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TC32HardwareConfigValidator implements ITargetConfigConstants {

	public static List<ValidationResult> validate(ITargetConfiguration config) {

		List<ValidationResult> allresults = new ArrayList<ValidationResult>();

		ValidationResult result;

		result = checkMCU(config);
		if (!result.result.equals(Result.OK)) {
			allresults.add(result);
		}


		return allresults;
	}

	/**
	 * Check if the current MCU is supported by all tools.
	 * <p>
	 * This method will return {@link Result#OK} iff
	 * <ul>
	 * <li>The current MCU is in the list of supported MCUs from both the Programmer tool and the
	 * GDB Server (if they have been set).</li>
	 * </ul>
	 * If either tool has a list of supported mcus and the current mcu is not in them then
	 * {@link Result#ERROR} is returned.
	 * </p>
	 * <p>
	 * Calling this method may cause I/O activity (execution of the selected tools), it should not
	 * be called directly from the UI Thread.
	 * </p>
	 * 
	 * @param config
	 *            The target configuration to check.
	 * @return {@link ValidationResult} with the result code and a human readable description.
	 */
	public static ValidationResult checkMCU(ITargetConfiguration config) {
		try {

			String currentmcu = config.getMCU();

			// Check if the MCU is valid for both the Programmer Tool and the GDBServer
			// If either tool returns null as a list of supported mcus, then it is
			// assumed that the tool does not care about mcus and that every mcu is valid.
			IProgrammerTool progtool = config.getProgrammerTool();
			Set<String> progmcus = progtool.getMCUs();
			boolean progtoolOK = progmcus != null ? progmcus.contains(currentmcu) : true;

			IGDBServerTool gdbserver = config.getGDBServerTool();
			Set<String> gdbservermcus = gdbserver.getMCUs();
			boolean gdbserverOK = gdbservermcus != null ? gdbservermcus.contains(currentmcu) : true;

			if (!progtoolOK && !gdbserverOK) {
				// Neither tool supports the mcu
				String progtoolname = progtool.getName();
				String gdbservername = gdbserver.getName();
				String msg;
				if (progtoolname.equals(gdbservername)) {
					msg = "MCU is not supported by programming tool / gdbserver " + progtoolname;
				} else {
					msg = "MCU is not supported by programming tool " + progtool.getName()
							+ " and by gdbserver " + gdbserver.getName();
				}
				return new ValidationResult(Result.ERROR, msg);
			}
			if (!progtoolOK) {
				String msg = "MCU not supported by programming tool " + progtool.getName();
				return new ValidationResult(Result.ERROR, msg);
			}
			if (!progtoolOK) {
				String msg = "MCU not supported by gdbserver " + progtool.getName();
				return new ValidationResult(Result.ERROR, msg);
			}
		} catch (TCDBException ade) {
			// Don't wan't to throw the exception, but we can't ignore it either.
			// so we just report an error with the exception text as description.
			String msg = ade.getLocalizedMessage();
			return new ValidationResult(Result.ERROR, msg);
		}

		return ValidationResult.OK_RESULT;
	}

	/**
	 * Check if the current Programmer is supported by all tools.
	 * <p>
	 * This method will return {@link Result#OK} iff
	 * <ul>
	 * <li>The current Programmer is in the list of supported Programmers from both the Programmer
	 * tool and the GDB Server (if they have been set).</li>
	 * </ul>
	 * If either tool has a list of supported programmers and the current programmer is not in them
	 * then {@link Result#ERROR} is returned.
	 * </p>
	 * <p>
	 * Calling this method may cause I/O activity (execution of the selected tools), it should not
	 * be called directly from the UI Thread.
	 * </p>
	 * 
	 * @param config
	 *            The target configuration to check.
	 * @return {@link ValidationResult} with the result code and a human readable description.
	 */
	public static ValidationResult checkProgrammer(ITargetConfiguration config) {
		try {

			String currentprogger = config.getAttribute(ATTR_PROGRAMMER_ID);

			// Check if the Programmer is valid for both the Programmer Tool and the GDBServer
			// If either tool returns null as a list of supported programmers, then it is
			// assumed that the tool does not care about programmers and that every programmer is
			// valid.
			IProgrammerTool progtool = config.getProgrammerTool();
			Set<String> progProggers = progtool.getProgrammers();
			boolean progtoolOK = progProggers != null ? progProggers.contains(currentprogger)
					: true;

			IGDBServerTool gdbserver = config.getGDBServerTool();
			Set<String> gdbserverProggers = gdbserver.getProgrammers();
			boolean gdbserverOK = gdbserverProggers != null ? gdbserverProggers
					.contains(currentprogger) : true;

			if (!progtoolOK && !gdbserverOK) {
				// Neither tool supports the Programmer
				String progtoolname = progtool.getName();
				String gdbservername = gdbserver.getName();
				String msg;
				if (progtoolname.equals(gdbservername)) {
					msg = "Programmer interface is not supported by programming tool / gdbserver "
							+ progtoolname;
				} else {
					msg = "Programmer interface is not supported by programming tool "
							+ progtool.getName() + " and by gdbserver " + gdbserver.getName();
				}
				return new ValidationResult(Result.ERROR, msg);
			}
			if (!progtoolOK) {
				String msg = "Programmer interface not supported by programming tool "
						+ progtool.getName();
				return new ValidationResult(Result.ERROR, msg);
			}
			if (!gdbserverOK) {
				String msg = "Programmer interface not supported by gdbserver "
						+ progtool.getName();
				return new ValidationResult(Result.WARNING, msg);
			}
		} catch (TCDBException ade) {
			// Don't wan't to throw the exception, but we can't ignore it either.
			// so we just report an error with the exception text as description.
			String msg = ade.getLocalizedMessage();
			return new ValidationResult(Result.ERROR, msg);
		}

		return ValidationResult.OK_RESULT;

	}

}
