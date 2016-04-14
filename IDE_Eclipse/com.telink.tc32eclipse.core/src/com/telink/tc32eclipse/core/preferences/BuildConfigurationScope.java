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
 * $Id: BuildConfigurationScope.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.preferences;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class BuildConfigurationScope implements IScopeContext {

	private IConfiguration fConfig = null;
	private IProject fProject = null;

	public BuildConfigurationScope(IConfiguration configuration) {
		super();
		if (configuration == null)
			throw new IllegalArgumentException();
		fConfig = configuration;
		fProject = (IProject) configuration.getManagedProject().getOwner();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getLocation()
	 */
	public IPath getLocation() {
		IProject project = (IProject) fConfig.getManagedProject().getOwner();
		IPath location = project.getLocation();
		return location == null ? null : location.append(".settings");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getName()
	 */
	public String getName() {
		// Camouflage as a "project" Setting
		return ProjectScope.SCOPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getNode(java.lang.String)
	 */
	public IEclipsePreferences getNode(String qualifier) {
		if (qualifier == null)
			throw new IllegalArgumentException();
		return (IEclipsePreferences) Platform.getPreferencesService().getRootNode().node(getName())
		        .node(fProject.getName()).node(qualifier).node(fConfig.getId());
	}

	public boolean configExists(String qualifier, IConfiguration buildcfg) {
		Preferences projectnode = Platform.getPreferencesService().getRootNode().node(getName())
		        .node(fProject.getName()).node(qualifier);
		try {
			return projectnode.nodeExists(buildcfg.getId());
		} catch (BackingStoreException e) {
			// In case of an exception assume that the node did not exist
			return false;
		}
	}

}
