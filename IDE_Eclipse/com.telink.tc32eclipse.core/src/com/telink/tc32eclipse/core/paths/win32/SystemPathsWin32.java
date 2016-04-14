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
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.paths.win32;


import java.io.IOException;
import java.net.URL;

import org.eclipse.core.internal.runtime.Log;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.core.paths.TC32Path;
import com.telink.tc32eclipse.core.paths.SystemPathHelper;

import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

//import org.eclipse.ui.IWorkbenchPart;


/**
 * Gets the actual system paths to the TC32Win and TC32 Tools applications.
 * <p>
 * Unlike the Posix variant of this class, which actually looks through the (almost) complete
 * filesystem, this class will retrieve the paths from the Windows registry. But even this has a bit
 * of overhead, so the {@link SystemPathHelper}, which uses this class, should cache the results.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 */
public class SystemPathsWin32 {
	private static IPath		fWinTC32Path		= null;
	
	private static boolean isDebug = false;

	private SystemPathsWin32() {
		// prevent instantiation
	}
	

	/**
	 * Find the system path for the given {@link TC32Path} enum value.
	 * 
	 * @param TC32path
	 * @return a valid path or <code>null</code> if no path could be found.
	 */
	public static IPath getSystemPath(TC32Path TC32path) {

		switch (TC32path) {
			case TC32_GCC:
				return getWinTC32Path("opt/tc32/bin");
			//case TC32_INCLUDE:
			//	return getWinTC32Path("opt/tc32/include"); 
			case TC32_TOOLS:
				return getWinTC32Path("opt/tc32/tools");
			case MAKE:
				return getWinTC32Path("bin");
			default:
				// If we end up here the TC32Path Enum has new entries not yet covered.
				// Log this as an internal error and ignore otherwise
				IStatus status = new Status(
						IStatus.WARNING,
						TC32Plugin.PLUGIN_ID,
						"Internal problem! TC32Path with value ["
								+ TC32path.toString()
								+ "] is not covered. Please report to the TC32 Eclipse plugin maintainer.",
						null);
				TC32Plugin.getDefault().log(status);
				return null;
		}
	}

	private static IPath getWinTC32Path(String append) 
	{
		IPath basepath = null;

		basepath = getWinTC32BasePath();

    
		if (basepath.isEmpty()) {
			return basepath;
		}
		
		return basepath.append(append);

	}
	

	public static Location getMyService(String filter) {
	    BundleContext context = FrameworkUtil.getBundle(com.telink.tc32eclipse.TC32Plugin.class).getBundleContext();
	    ServiceTracker<?, ?> tracker = null;
	    try{ 
	        tracker = new ServiceTracker<Object, Object>(context, context.createFilter("(&(" + Constants.OBJECTCLASS + "=" + Location.class.getName()  //$NON-NLS-1$ //$NON-NLS-2$
	                + ")" + filter + ")"), null); //$NON-NLS-1$ //$NON-NLS-2$
	        tracker.open();
	        return (Location) tracker.getService();
	    } catch (InvalidSyntaxException e) {
	        return null;
	    } finally {
	        if(tracker != null)
	            tracker.close();
	    }
	}
	

	

	/**
	 * Get the path to the TC32Win base directory from the Windows registry.
	 * 
	 * @return IPath with the current path to the TC32Win base directory
	 * @throws IOException 
	 */
	private static IPath getWinTC32BasePath() { //throws IOException 
	    
		if (fWinTC32Path != null) {
			return fWinTC32Path;
		} else {
		    isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
			    getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
			    
	            // Test only
	            /*isDebug = true;

	            if (isDebug) 
	            {
		           fWinTC32Path = new Path("c:\\TelinkSDKv1.4\\");
	                   return fWinTC32Path;
	            }*/
		}
		
			    
		// ECLIPSE_HOME_FILTER
		Location loc = getMyService(Location.INSTALL_FILTER);
		URL url = loc.getURL();
		
		fWinTC32Path = new Path(url.getPath());
		System.out.println("My path = "+fWinTC32Path);
		return fWinTC32Path;
	}	

}
