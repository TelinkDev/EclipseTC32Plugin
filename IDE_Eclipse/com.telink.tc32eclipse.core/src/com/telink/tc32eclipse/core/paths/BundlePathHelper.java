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
 * $Id: BundlePathHelper.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.core.paths;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Convenience class to get the path for a given resource from a Eclipse bundle.
 * 
 * @author Peter Shieh
 * @since 0.1
 */
final class BundlePathHelper {

	/**
	 * @param path
	 *            TC32Path for the path
	 * @param bundeid
	 *            Id of the Bundle from which to get the path
	 * @return IPath with the path
	 */
	public static IPath getPath(TC32Path path, String bundeid) {

		// TODO: not implemented yet
		return new Path("");
	}

}
