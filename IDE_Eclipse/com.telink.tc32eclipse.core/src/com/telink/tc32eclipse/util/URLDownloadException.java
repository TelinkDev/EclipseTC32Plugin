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
 * $Id: URLDownloadException.java 851 20.1.08-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.util;

/**
 * Exception indicating a failed download.
 * <p>
 * The Exception message will have a nice readable error description.
 * </p>
 * Used by the URLDownloadManager
 * 
 * @see URLDownloadManager
 * @author Peter Shieh
 * @since 0.1
 * 
 * 
 */
public class URLDownloadException extends Exception {

	/**
     * 
     */
    private static final long serialVersionUID = -5579817130802768958L;

    public URLDownloadException(String message) {
    	super(message);
    }
    
    public URLDownloadException(String message, Throwable cause) {
    	super(message, cause);
    }
}
