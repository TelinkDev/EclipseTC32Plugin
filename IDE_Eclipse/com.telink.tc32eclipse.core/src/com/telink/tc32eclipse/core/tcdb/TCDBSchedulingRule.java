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
package com.telink.tc32eclipse.core.tcdb;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * A simple SchedulingRule to prevent TCDB from being started multiple times (which would
 * probably result in a PORT_BLOCKED Exception).
 * <p>
 * Instances of this Rule can should be added to all Jobs that run TCDB and will cause actual
 * access to a programmer.
 * </p>
 * <p>
 * The rule will try to determine conflicts by comparing the ProgrammerConfig of this Rule with that
 * of an conflicting rule. If there is a chance that both configs use the same port, this rule will
 * report a conflict.
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * 
 */
public class TCDBSchedulingRule implements ISchedulingRule {

	/** The Config to determine which port TCDB is currently running on */
	private final ProgrammerConfig	fProgrammerConfig;

	/**
	 * Creates a new SchedulingRule for the given ProgrammerConfig.
	 * 
	 * @param config
	 *            <code>ProgrammerConfig</code>
	 */
	public TCDBSchedulingRule(ProgrammerConfig config) {
		fProgrammerConfig = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#contains(org.eclipse.core.runtime.jobs.ISchedulingRule)
	 */
	public boolean contains(ISchedulingRule rule) {
		if (rule == this) {
			return true;
		}
		// Don't need any nesting
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.ISchedulingRule#isConflicting(org.eclipse.core.runtime.jobs.ISchedulingRule)
	 */
	public boolean isConflicting(ISchedulingRule rule) {
		
		if (!(rule instanceof TCDBSchedulingRule))
			// Don't care about other Rules
			return false;

		// But conflict with ourself
		if (rule == this)
			return true;

		TCDBSchedulingRule testrule = (TCDBSchedulingRule) rule;
		ProgrammerConfig testcfg = testrule.fProgrammerConfig;

		// if either config is null we have no conflict (because the call to TCDB will fail
		// anyway)
		if (fProgrammerConfig == null || testcfg == null) {
			return false;
		}

		return true;


	}
}
