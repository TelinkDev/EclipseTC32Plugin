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
 * $Id: AvariceProgrammers.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets.tools;

import com.telink.tc32eclipse.core.targets.HostInterface;
import com.telink.tc32eclipse.core.targets.IProgrammer;
import com.telink.tc32eclipse.core.targets.TargetInterface;


/**
 * Enumeration of all Programmers supported by avarice.
 * <p>
 * Unlike PCDB the avarice application has no command line argument to list all supported
 * programmers, so this list is hard-coded on the assumption that avarice won't get support for new
 * devices to often.
 * </p>
 * <p>
 * This enumeration implements the {@link IProgrammer} interface, so that its members can be
 * directly used by {@link PCDBTool}.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public enum PCDBProgrammers implements IProgrammer {

	tc32_usb("TC32 in USB Debug mode") {

		@Override
		public HostInterface[] getHostInterfaces() {
			return new HostInterface[] { HostInterface.USB };
		}

		@Override
		public TargetInterface getTargetInterface() {
			return TargetInterface.USB;
		}

	

	

	};

	private String	fDescription;

	private PCDBProgrammers(String description) {
		fDescription = description;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IProgrammer#getId()
	 */
	public String getId() {
		return this.name();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IProgrammer#getDescription()
	 */
	public String getDescription() {
		return fDescription;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IProgrammer#getAdditionalInfo()
	 */
	public String getAdditionalInfo() {
		// Avarice does not have any additional infos.
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IProgrammer#getHostInterfaces()
	 */
	public abstract HostInterface[] getHostInterfaces();

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IProgrammer#getTargetInterface()
	 */
	public abstract TargetInterface getTargetInterface();

	
	

}
