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
package com.telink.tc32eclipse.ui.actions;

//import org.eclipse.cdt.managedbuilder.core.IConfiguration;
//import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
//import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

//import com.telink.tc32eclipse.core.properties.ProjectPropertyManager;
//import com.telink.tc32eclipse.core.properties.TC32ProjectProperties;
import  com.telink.tc32eclipse.core.toolinfo.WTCDBTools;

/**
 * @author Peter Shieh
 * @since 0.1
 * @since 2.3 Added optional delay between TCDB invocations
 * 
 */
public class WTCDBProjectAction extends ActionDelegate implements IWorkbenchWindowActionDelegate {


	/**
	 * Constructor for this Action.
	 */
	public WTCDBProjectAction() {
		super();
	}
	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		
		WTCDBTools wtcdb = WTCDBTools.getDefault();
		
		try {
			wtcdb.run("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		
	}

}
