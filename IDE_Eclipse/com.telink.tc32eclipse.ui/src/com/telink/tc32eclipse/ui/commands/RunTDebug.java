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
package com.telink.tc32eclipse.ui.commands;


import java.io.IOException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import  com.telink.tc32eclipse.core.toolinfo.FindTDebug;

/**
 * @author Peter Shieh
 * @since 1.0
 * @since 2.3 Added optional delay between TCDB invocations
 * 
 */

public class RunTDebug extends AbstractHandler {

	  public Object execute(ExecutionEvent event) throws ExecutionException {
		
		
	      FindTDebug tdebug = FindTDebug.getDefault();
		
		try {
			tdebug.run("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	    //HandlerUtil.getActiveWorkbenchWindow(event).close();
	    return null;
	  }
	  

	} 




