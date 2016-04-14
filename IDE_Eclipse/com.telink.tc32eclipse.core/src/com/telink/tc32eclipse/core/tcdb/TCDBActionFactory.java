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

//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
//import org.eclipse.core.runtime.Status;

//import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.PluginIDs;
import com.telink.tc32eclipse.core.tcdb.TCDBAction.Action;
import com.telink.tc32eclipse.core.tcdb.TCDBAction.FileType;
import com.telink.tc32eclipse.core.tcdb.TCDBAction.MemType;

/**
 * This class provides some static methods to get {@link TCDBAction} objects for common
 * scenarios.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TCDBActionFactory {

	/**
	 * Get a list of actions to read all readable elements of an MCU and write them to the given
	 * folder.
	 * <p>
	 * This method needs the mcu id to determine what memories are readable from the device.
	 * </p>
	 * <p>
	 * It will call TCDB to get a list of all memory types and safe them with the following
	 * filenames and formats <table>
	 * <tr>
	 * <th>Memory</th>
	 * <th>Filename</th>
	 * <th>Type</th>
	 * </tr>
	 * <tr>
	 * <td>flash</td>
	 * <td>flash.bin</td>
	 * <td>bin</td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 * @param mcuid
	 *            The mcu id value.
	 * @param backupfolderpath
	 *            Path to an existing folder
	 * @return <code>List&lt;String&gt;</code> with all actions required to backup the mcu
	 */
	public static List<TCDBAction> backupActions(String mcuid, String backupfolderpath) {

		List<TCDBAction> actions = new ArrayList<TCDBAction>();

		// TODO load the list of memories from TCDB

		IPath destpath = new Path(backupfolderpath);

		String flashfile = destpath.append("boot.bin").toOSString();

		actions.add(new TCDBAction(MemType.flash, Action.read, flashfile, FileType.binary));


		return actions;
	}

	/**
	 * Create an {@link TCDBAction} to write the flash image file defined in the given build
	 * configuration to the MCU.
	 * <p>
	 * If plugin.xml has not been modified, the filename will be
	 * <code>${BuildArtifactBaseFileName}.hex</code>. The variable is not resolved. It is up to
	 * the caller to resolve any variables in the generated arguments of the returned TCDB
	 * action.
	 * </p>
	 * <p>
	 * The generated action uses {@link TCDBAction.FileType#auto} to let TCDB determine the
	 * file type.
	 * </p>
	 * 
	 * @param buildcfg
	 *            <code>IConfiguration</code> from which to extract the flash image file name.
	 * @return <code>TCDBAction</code> to write the flash.
	 */
	public static TCDBAction writeFlashAction(IConfiguration buildcfg) {

		TCDBAction action = null;

		ITool[] tools = buildcfg.getToolsBySuperClassId(PluginIDs.PLUGIN_TOOLCHAIN_TOOL_FLASH);
		// Test if there is a Generate Flash Image tool in the toolchain of the
		// configuration.
		if (tools.length != 0) {
			// Tool does exist, extract the filename from the output option
			// We cannot get the name directly from the output element, because
			// the reference from the output element to the name-declaring
			// output option is not resolved when we call
			// outputelement.getValue().
			ITool objcopy = tools[0];
			IOption outputoption = objcopy
					.getOptionBySuperClassId("com.telink.tc32clipse.objcopy.flash.option.output");
			
			if (outputoption != null)
			{
				String filename = (String) outputoption.getValue();

				action = writeFlashAction(filename);
			}
		}
		return action;
	}

	/**
	 * Create an {@link TCDBAction} to write the given flash image file to the MCU.
	 * <p>
	 * The generated action uses {@link TCDBAction.FileType#auto} to let TCDB determine the
	 * file type.
	 * </p>
	 * <p>
	 * Any macros in the filename are not resolved. It is up to the caller to resolve any macros as
	 * required.
	 * </p>
	 * 
	 * @param filename
	 *            <code>String</code> with the flash image file name.
	 * @return <code>TCDBAction</code>
	 */
	public static TCDBAction writeFlashAction(String filename) {

		return new TCDBAction(MemType.flash, Action.write, filename, FileType.auto);
	}

}
