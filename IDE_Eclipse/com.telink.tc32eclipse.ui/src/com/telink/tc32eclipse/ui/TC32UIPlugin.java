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
 * $Id: TC32UIPlugin.java 851 20.1.08-07 19:37:00Z innot $
 *******************************************************************************/
package com.telink.tc32eclipse.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.PageBook;

/**
 * The activator class controls the plug-in life cycle
 */
public class TC32UIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String	PLUGIN_ID	= "com.telink.tc32eclipse.ui";

	// The shared instance
	private static TC32UIPlugin	plugin;

	private PageBook pagebook;
	private TableViewer tableviewer;
	private TextViewer textviewer;
	
	
	/**
	 * Shows the given selection in this view.
	 */
	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
		
		//setContentDescription(sourcepart.getTitle() + " (" + selection.getClass().getName() + ")");
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			showItems(ss.toArray());
		}
		if (selection instanceof ITextSelection) {
			ITextSelection ts  = (ITextSelection) selection;
			showText(ts.getText());
		}
		if (selection instanceof IMarkSelection) {
			IMarkSelection ms = (IMarkSelection) selection;
			try {
			    showText(ms.getDocument().get(ms.getOffset(), ms.getLength()));
			} catch (BadLocationException ble) { }
		}
	}
	
	
	private void showItems(Object[] items) {
		tableviewer.setInput(items);
		pagebook.showPage(tableviewer.getControl());
	}
	
	private void showText(String text) {
		textviewer.setDocument(new Document(text));
		pagebook.showPage(textviewer.getControl());
	}
/*	
	public void createPartControl(Composite parent) {
		// the PageBook allows simple switching between two viewers
		pagebook = new PageBook(parent, SWT.NONE);
		
		tableviewer = new TableViewer(pagebook, SWT.NONE);
		tableviewer.setLabelProvider(new WorkbenchLabelProvider());
		tableviewer.setContentProvider(new ArrayContentProvider());
		
		// we're cooperative and also provide our selection
		// at least for the tableviewer
		//getSite().setSelectionProvider(tableviewer);
		
		textviewer = new TextViewer(pagebook, SWT.H_SCROLL | SWT.V_SCROLL);
		textviewer.setEditable(false);
		
		//getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
	}
*/
	public void setFocus() {
		pagebook.setFocus();
	}
/*
	public void dispose() {
		// important: We need do unregister our listener when the view is disposed
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}
*/
	

	/**
	 * The constructor
	 */
	public TC32UIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static TC32UIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
