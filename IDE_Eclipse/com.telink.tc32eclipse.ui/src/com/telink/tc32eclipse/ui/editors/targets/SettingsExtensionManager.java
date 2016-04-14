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
 * $Id: SettingsExtensionManager.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.ui.editors.targets;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

import com.telink.tc32eclipse.ui.TC32UIPlugin;

/**
 * Manages the extension setting parts for the target configuration editor.
 * <p>
 * This class manages the
 * </p>
 * <p>
 * This class implements the singleton pattern. There is only one instance of this class, accessible
 * with {@link #getDefault()}.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class SettingsExtensionManager {

	private static SettingsExtensionManager	fInstance;

	private final static String				NAMESPACE		= TC32UIPlugin.PLUGIN_ID;
	public final static String				EXTENSIONPOINT	= NAMESPACE + ".targetToolSettings";

	public static SettingsExtensionManager getDefault() {
		if (fInstance == null) {
			fInstance = new SettingsExtensionManager();
		}

		return fInstance;
	}

	// prevent instantiation
	private SettingsExtensionManager() {
		// empty constructor
	}

	public ITCEditorPart getSettingsPartForTool(String id) {

		return loadExtension(id);
	}

	/**
	 * Load all extensions.
	 * 
	 * @see #added(IExtension[])
	 * 
	 */
	private ITCEditorPart loadExtension(String toolid) {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(EXTENSIONPOINT);
		for (IConfigurationElement element : elements) {

			// Get the id of the tool this settings part is applicable for.
			String id = element.getAttribute("toolId");
			if (!id.equals(toolid)) {
				// Not the required id -- continue searching
				continue;
			}

			// Get an instance of the implementing class
			Object obj;
			try {
				obj = element.createExecutableExtension("class");
			} catch (CoreException e) {
				// TODO log an error
				continue;
			}

			if (obj instanceof ITCEditorPart) {
				ITCEditorPart part = (ITCEditorPart) obj;
				return part;
			}
		}

		// No part found for the given tool id
		// TODO: return a default part which contains a meaningful error message
		return null;

	}

}
