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
 * $Id: TCEditorInput.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.ui.editors.targets;

import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.telink.tc32eclipse.core.targets.ITargetConfigurationWorkingCopy;
import com.telink.tc32eclipse.core.targets.TargetConfigurationManager;

/**
 * IEditorInput implementation for Target Configurations.
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TCEditorInput implements IEditorInput {

	private TargetConfigurationManager		fTCManager;

	private String							fHardwareConfigID;

	private ITargetConfigurationWorkingCopy	fHardwareConfigWC;

	public TCEditorInput(String targetConfigID) {
		fTCManager = TargetConfigurationManager.getDefault();
		fHardwareConfigID = targetConfigID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return fTCManager.exists(fHardwareConfigID);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		try {
			return fTCManager.getConfig(fHardwareConfigID).getName();
		} catch (IOException e) {
			// could not read the config from the storage.
			// Just return a dummy name
			return "Unknown";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		// TODO: Is persistance required?
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		String text;
		try {
			text = "Hardware Configuration: \"" + fTCManager.getConfig(fHardwareConfigID).getName()
					+ "\"";
		} catch (IOException e) {
			// could not read the config from the storage.
			// Just return a dummy text
			text = "Error: " + e.getLocalizedMessage();
		}
		return text;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	//@SuppressWarnings("unchecked")
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		// This might be a misuse of the adapter philosophy, but it saves some casts for the
		// users of this editor input.
		// If asked to adapt to a String, then the target configuration id is returned.
		if (String.class.equals(adapter)) {
			return fHardwareConfigID;
		}

		// Adapt to a target configuration working copy
		try {
			if (ITargetConfigurationWorkingCopy.class.equals(adapter)) {
				if (fHardwareConfigWC == null) {
					fHardwareConfigWC = fTCManager.getWorkingCopy(fHardwareConfigID);
				}
				return fHardwareConfigWC;
			}
		} catch (IOException e) {
			// could not read the config from the storage.
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// Two Target Configuration Editor Inputs are equal iff
		// they have the same Target Configuration ID.
		if (this == obj) {
			return true;
		}
		if (obj instanceof TCEditorInput) {
			TCEditorInput other = (TCEditorInput) obj;
			if (fHardwareConfigID.equals(other.fHardwareConfigID)) {
				return true;
			}
		}
		return false;
	}

}
