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
 * $Id: IProgrammer.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets;
/**
* Description of programmer interface hardware.
* <p>
* This interface represents a single programmer interface.
* </p>
* <p>
* A list of supported <code>IProgrammer</code> objects is returned by every implementation of the
* <code>ITargetConfigurationTool</code> interface.
* </p>
* 
* 
* @author Peter Shieh
* @since 0.1
* 
*/
public interface IProgrammer {

	/**
	 * Returns the id of the programmer.
	 * <p>
	 * The id is determined by the implementation. The id is unique, every id will represent only a
	 * single programmer. However a single programmer can have more than one id (as is done with
	 * TCDB).
	 * </p>
	 * <p>
	 * The baseline for id values are the programmer id's from TCDB.
	 * <code>ITargetConfigurationTools</code> supporting the same programmers as TCDB should use
	 * the same id values. <code>ITargetConfigurationTools</code> that support programmers not
	 * supported by TCDB must use separate id values.
	 * </p>
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * Returns a descriptive name of the programmer.
	 * <p>
	 * The description is used in the user interface.
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Returns additional info about the programmer.
	 * <p>
	 * This info is used by the user interface to display additional info.
	 * </p>
	 * 
	 * @return
	 */
	public String getAdditionalInfo();

	/**
	 * Returns all host interfaces the programmer supports.
	 * <p>
	 * Most programmer hardware supports only a single host interface, e.g. the serial port, but
	 * some hardware, like the TC32 ICE can be connected to either the serial port or via usb.
	 * So this function may returns an array of all {@link HostInterface}s supported by the
	 * programmer.
	 * </p>
	 * 
	 * @return <code>HostInterface</code>s supported by the programmer.
	 */
	public HostInterface[] getHostInterfaces();

	/**
	 * Returns the target interface supported by the programmer.
	 * 
	 * @return {@link TargetInterface}
	 */
	public TargetInterface getTargetInterface();



}
