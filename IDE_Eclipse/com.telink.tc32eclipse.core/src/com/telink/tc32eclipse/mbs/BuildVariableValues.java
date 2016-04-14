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
 * $Id: BuildVariableValues.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.mbs;

import java.io.File;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.envvar.IBuildEnvironmentVariable;
import org.eclipse.core.resources.IProject;

import com.telink.tc32eclipse.core.paths.TC32Path;
import com.telink.tc32eclipse.core.paths.TC32PathProvider;
import com.telink.tc32eclipse.core.paths.IPathProvider;
import com.telink.tc32eclipse.core.properties.TC32ProjectProperties;
import com.telink.tc32eclipse.core.properties.ProjectPropertyManager;

/**
 * This <code>Enum</code> contains a list of all available variable names.
 * <p>
 * Each Variable knows how to extract its current value from an {@link TC32ProjectProperties} object,
 * respectively from an {@link IConfiguration}.
 * </p>
 * <p>
 * Currently these Environment Variables are handled:
 * <ul>
 * <li><code>$(TC32TARGETMCU)</code>: The target MCU id value as selected by the user</li>
  * <li><code>$(TCDBOPTIONS)</code>: The command line options for TCDB, except for any
 * action options (<em>-U</em> options)</li>
 * <li><code>$(TCDBACTIONOPTIONS)</code>: The command line options for TCDB to execute all
 * actions requested by the user. (<em>-U</em> options)</li>
 * <li><code>$(BUILDARTIFACT)</code>: name of the target build artifact (the .elf file)</li>
 * <li><code>$(PATH)</code>: The current path prepended with the paths to the tc32-elf-gcc executable
 * and the make executable. This, together with the selection of the paths on the preference page,
 * allows for multiple avr-gcc toolchains on one computer</li>
 * <li><code>$(TCDBPATH)</code>: The current path to the TCDB executable.</li>
 * </ul>
 * </p>
 * 
 * @author Peter Shieh
 * @since 0.1
 * @since 2.3 Added TCDBPATH variable (fix for Bug 2136888)
 */
public enum BuildVariableValues {

	TC32TARGETMCU() {
		@Override
		public String getValue(IConfiguration buildcfg) {
			TC32ProjectProperties props = getPropsFromConfig(buildcfg);
			if (props == null)
				return "";
			String targetmcu = props.getMCUId();
			return targetmcu;
		}
	},

	TCDBOPTIONS() {
		@Override
		public String getValue(IConfiguration buildcfg) {
			TC32ProjectProperties props = getPropsFromConfig(buildcfg);
			if (props == null)
				return "";
			List<String> TCDBoptions = props.getTCDBProperties().getArguments();
			StringBuilder sb = new StringBuilder();
			for (String option : TCDBoptions) {
				sb.append(option + " ");
			}
			return sb.toString();
		}
	},

	TCDBACTIONOPTIONS() {
		@Override
		public String getValue(IConfiguration buildcfg) {
			TC32ProjectProperties props = getPropsFromConfig(buildcfg);
			if (props == null)
				return "";
			List<String> TCDBoptions = props.getTCDBProperties().getActionArguments(buildcfg);
			StringBuilder sb = new StringBuilder();
			for (String option : TCDBoptions) {
				sb.append(option + " ");
			}
//System.out.printf("Tools action options" + sb.toString());
			return sb.toString();
		}
	},

	BUILDARTIFACT() {
		// This is only defined to export the BuildArtifact Build Macro as an
		// environment variable in case some makefile requires the path to the
		// .elf target file.
		@Override
		public String getValue(IConfiguration buildcfg) {
			String artifact = buildcfg.getArtifactName() + "." + buildcfg.getArtifactExtension();
			 
			return artifact;
		}

		@Override
		public boolean isMacro() {
			// BUILDARTIFACT is not needed as a build macro, because CDT already
			// has a macro with this name.
			return false;
		}
	},

	PATH() {
		@Override
		public String getValue(IConfiguration buildcfg) {
			// Get the paths to "avr-gcc" and "make" from the PathProvider
			// and return the paths, separated with a System specific path
			// separator.
			// The path to the TCDB executable is handled as a separate
			// variable because at least with WinTC32 avr-gcc and TCDB are
			// in the same directory and adding the path to TCDB to the
			// global path would have no effect as the TCDB executable
			// from the gccpath would be used anyway.

			StringBuilder paths = new StringBuilder();

			IPathProvider gccpathprovider = new TC32PathProvider(TC32Path.TC32_GCC);
			String gccpath = gccpathprovider.getPath().toOSString();
			if (gccpath != null && !("".equals(gccpath))) {
				paths.append(gccpath);
				paths.append(PATH_SEPARATOR);
			}

			IPathProvider makepathprovider = new TC32PathProvider(TC32Path.MAKE);
			String makepath = makepathprovider.getPath().toOSString();
			if (makepath != null && !("".equals(makepath))) {
				paths.append(makepath);
				paths.append(PATH_SEPARATOR);
			}

			return paths.toString();
		}

		@Override
		public int getOperation() {
			// Prepend our paths to the System paths
			return IBuildEnvironmentVariable.ENVVAR_PREPEND;
		}

		@Override
		public boolean isMacro() {
			// PATH not supported as a BuildMacro
			return false;
		}

	},
	
	 
	TCDBPATH() {
		@Override
		public String getValue(IConfiguration buildcfg) {
			IPathProvider TCDBpathprovider = new TC32PathProvider(TC32Path.TC32_TOOLS);
			String TCDBpath = TCDBpathprovider.getPath().toOSString();
			if (TCDBpath != null && !("".equals(TCDBpath))) {

				return TCDBpath + File.separator;
			}
			return "";
		}
	};

	/** System default Path Separator. On Windows ";", on Posix ":" */
	private final static String	PATH_SEPARATOR	= System.getProperty("path.separator");

	/**
	 * Get the current variable value for the given Configuration
	 * 
	 * @param buildcfg
	 *            <code>IConfiguration</code> for which to get the variable value.
	 * @return <code>String</code> with the current value of the variable.
	 */
	public abstract String getValue(IConfiguration buildcfg);

	/**
	 * @return <code>true</code> if this variable is supported as a build macro.
	 */
	public boolean isMacro() {
		// This method is overridden in some Enum values
		return true;
	}

	/**
	 * @return <code>true</code> if this variable is supported as an environment variable.
	 */
	public boolean isVariable() {
		// This method could be overridden in some Enum values.
		return true;
	}

	/**
	 * Get the Operation code for environment variables.
	 * <p>
	 * Most Variables will return {@link IBuildEnvironmentVariable#ENVVAR_REPLACE}. However the
	 * <code>PATH</code> environment variable will return
	 * {@link IBuildEnvironmentVariable#ENVVAR_PREPEND}.
	 * </p>
	 * 
	 * @see IBuildEnvironmentVariable#getOperation()
	 * 
	 * @return <code>int</code> with the operation code.
	 */
	public int getOperation() {
		// Default is REPLACE.
		// The PATH Variable, which requires ENVVAR_PREPEND, will override this
		// method.
		return IBuildEnvironmentVariable.ENVVAR_REPLACE;
	}

	/**
	 * Get the TC32 Project properties for the given Configuration.
	 * 
	 * @param buildcfg
	 *            <code>IConfiguration</code> for which to get the properties.
	 * @return
	 */
	private static TC32ProjectProperties getPropsFromConfig(IConfiguration buildcfg) {
		ProjectPropertyManager manager = ProjectPropertyManager
				.getPropertyManager((IProject) buildcfg.getOwner());
		TC32ProjectProperties props = manager.getConfigurationProperties(buildcfg);
		return props;
	}

}
