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
 * $Id: TC32ProjectNature.java 851 20.1.08-07 19:37:00Z innot $
 *******************************************************************************/
/**
 * 
 */
package com.telink.tc32eclipse.core.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * @author Peter Shieh
 * 
 */
public class TC32ProjectNature implements IProjectNature {

	private IProject	fProject	= null;
	IProjectDescription     fDescription    = null;
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	public void configure() throws CoreException {

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException {

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	public IProject getProject() {
	    // PS
	        if (fProject == null)
		    try {
			TC32ProjectNature.addTC32Nature(this);
		    } catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
	        
		return fProject;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject(IProject project) {
		fProject = project;

	}

	public static void addTC32Nature(TC32ProjectNature tc32ProjectNature) throws CoreException {
	    
		final String natureid = "com.telink.tc32eclipse.core.TC32nature";

		IProjectDescription description = tc32ProjectNature.getDescription();
		String[] oldnatures = description.getNatureIds();

		// Check if the project already has an TC32 nature
		for (int i = 0; i < oldnatures.length; i++) {
			if (natureid.equals(oldnatures[i]))
				return; // return if TC32 nature already set
		}
		String[] newnatures = new String[oldnatures.length + 1];
		System.arraycopy(oldnatures, 0, newnatures, 0, oldnatures.length);
		newnatures[oldnatures.length] = natureid;
		description.setNatureIds(newnatures);
		tc32ProjectNature.setDescription(description, new NullProgressMonitor());
	}

	/**
	 * @return
	 */
	private IProjectDescription getDescription() {
	
	    return fDescription;
	}

	/**
	 * @param description
	 * @param nullProgressMonitor
	 */
	private void setDescription(IProjectDescription description,
		NullProgressMonitor nullProgressMonitor) {
	
	    fDescription = description;
	    
	}
}
