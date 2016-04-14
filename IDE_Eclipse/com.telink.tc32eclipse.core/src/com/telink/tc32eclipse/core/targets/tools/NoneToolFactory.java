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
 * $Id: NoneToolFactory.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/

package com.telink.tc32eclipse.core.targets.tools;

import java.util.Set;

import com.telink.tc32eclipse.core.targets.IGDBServerTool;
import com.telink.tc32eclipse.core.targets.IProgrammer;
import com.telink.tc32eclipse.core.targets.IProgrammerTool;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration;
import com.telink.tc32eclipse.core.targets.ITargetConfigurationTool;
import com.telink.tc32eclipse.core.targets.IToolFactory;
import com.telink.tc32eclipse.core.targets.ITargetConfiguration.ValidationResult;
import com.telink.tc32eclipse.core.tcdb.TCDBException;

/**
 * Factory for the 'None' tool.
 * <p>
 * The 'None' tool stands for <em>no tool selected</em> and is a dummy tool that is used by the user
 * interface to handle the case where no tool is required without using <code>null</code> objects.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class NoneToolFactory implements IToolFactory {

	public final static String		ID			= "NONE";
	public final static String		NAME		= "None";

	private final static String[]	EMPTY_LIST	= new String[] {};

	private final NoneTool			fToolInstance;

	public NoneToolFactory() {
		fToolInstance = new NoneTool();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.telink.tc32eclipse.core.targets.IToolFactory#createTool(com.telink.tc32eclipse.core.targets
	 * .ITargetConfiguration)
	 */
	public ITargetConfigurationTool createTool(ITargetConfiguration tc) {
		return fToolInstance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IToolFactory#getId()
	 */
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IToolFactory#getName()
	 */
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telink.tc32eclipse.core.targets.IToolFactory#isType(java.lang.String)
	 */
	public boolean isType(String tooltype) {
		// The nonetool can represent all tool types
		return true;
	}

	/**
	 * This is a special virtual tool that represents no selected tool.
	 */
	private class NoneTool implements IGDBServerTool, IProgrammerTool {
		/*
		 * (non-Javadoc)
		 * @see com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getId()
		 */
		public String getId() {
			return ID;
		}

		/*
		 * (non-Javadoc)
		 * @see com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getName()
		 */
		public String getName() {
			return NAME;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getVersion(com.telink.tc32eclipse.
		 * core.targets.ITargetConfiguration)
		 */
		public String getVersion() throws TCDBException {
			return getName();
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getMCUs(com.telink.tc32eclipse
		 * .core .targets.ITargetConfiguration)
		 */
		public Set<String> getMCUs() throws TCDBException {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getProgrammer(com.telink.tc32eclipse
		 * .core.targets.ITargetConfiguration, java.lang.String)
		 */
		public IProgrammer getProgrammer(String id) throws TCDBException {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#getProgrammers(com.telink.tc32eclipse
		 * .core.targets.ITargetConfiguration)
		 */
		public Set<String> getProgrammers() throws TCDBException {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see com.telink.tc32eclipse.core.targets.IAttributeProvider#getAttributes()
		 */
		public String[] getAttributes() {
			return EMPTY_LIST;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.telink.tc32eclipse.core.targets.IAttributeProvider#getDefaultValue(java.lang.String)
		 */
		public String getDefaultValue(String attribute) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.telink.tc32eclipse.core.targets.ITargetConfigurationTool#validate(com.telink.tc32eclipse
		 * .core .targets.ITargetConfiguration, java.lang.String)
		 */
		public ValidationResult validate(String attr) {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see com.telink.tc32eclipse.core.targets.IGDBServerTool#isSimulator()
		 */
		public boolean isSimulator() {
			return false;
		}
	}
}
