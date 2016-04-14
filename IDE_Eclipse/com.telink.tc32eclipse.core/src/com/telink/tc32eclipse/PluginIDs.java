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
 ******************************************************************************/

package com.telink.tc32eclipse;

/**
 * Definitions of id values used in the plugin.xml
 * 
 * Some id's from the plugin.xml are used quite frequently to programmatically access some parts of the toolchain.
 * They are defined here in one central place to aid refactoring of the plugin.xml
 * 
 * @author Peter Shieh
 * @since 2.0
 *
 */
public interface PluginIDs {

	/** 
	 * ID of the base toolchain, all other toolchains are derived from this.
	 * Value: {@value}
	 */
	public final static String PLUGIN_BASE_TOOLCHAIN = "com.telink.tc32eclipse.toolchain.TC32Win.base";
	
	/** ID of the mcu type option of the base toolchain. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_OPTION_MCU = "com.telink.tc32eclipse.toolchain.options.target.mcutype";
	
	
	/** ID of the "generate Flash" toolchain option. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_OPTION_GENERATEFLASH = "com.telink.tc32eclipse.toolchain.options.toolchain.objcopy.flash";
	
	/** ID of the compiler tool. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_TOOL_COMPILER = "com.telink.tc32eclipse.tool.compiler.TC32Win";

	/** ID of the linker tool. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_TOOL_LINKER = "com.telink.tc32eclipse.tool.linker.TC32Win";

	/** ID of the flash objcopy tools. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_TOOL_FLASH = "com.telink.tc32eclipse.tool.objcopy.flash.TC32Win";

		/** ID of the size tool. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_TOOL_SIZE = "com.telink.tc32eclipse.tool.size.TC32Win";

	/** ID of the size tool format option with TC32. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_TOOL_SIZE_FORMATWITHTC32 = "com.telink.tc32eclipse.size.option.formatwithTC32";
	
	/** ID of the size tool format option without TC32. Value: {@value} */
	public final static String PLUGIN_TOOLCHAIN_TOOL_SIZE_FORMAT = "com.telink.tc32eclipse.size.option.format";
	
	/** ID of the TC32 Nature. Value: {@value} */
	public final static String NATURE_ID = "com.telink.tc32eclipse.core.TC32nature";
}
