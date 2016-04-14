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

package com.telink.tc32eclipse.ui.dialogs;


import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TCDBUserInputDialog extends TitleAreaDialog {
	  private Text txtOptions;
	  
	  private String strOptions;


	  public TCDBUserInputDialog(Shell parentShell) {
	    super(parentShell);
	  }

	  @Override
	  public void create() {
	    super.create();
	    setTitle("This is TCDB option dialog");
	    setMessage("This is a advanced options Dialog", IMessageProvider.INFORMATION);
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    GridLayout layout = new GridLayout(2, false);
	    container.setLayout(layout);

	    createFirstName(container);	    

	    return area;
	  }

	  private void createFirstName(Composite container) {
	    Label lbtFirstName = new Label(container, SWT.NONE);
	    lbtFirstName.setText("Options");

	    GridData dataFirstName = new GridData();
	    dataFirstName.grabExcessHorizontalSpace = true;
	    dataFirstName.horizontalAlignment = GridData.FILL;

	    txtOptions = new Text(container, SWT.BORDER);
	    txtOptions.setLayoutData(dataFirstName);
	  }
	  
	  



	  @Override
	  protected boolean isResizable() {
	    return true;
	  }

	  // save content of the Text fields because they get disposed
	  // as soon as the Dialog closes
	  private void saveInput() {
		  strOptions = txtOptions.getText();	    

	  }

	  @Override
	  protected void okPressed() {
	    saveInput();
	    super.okPressed();
	  }
	  

	  public String getFirstName() {
	    return strOptions;
	  }
	  
	  @Override
	  protected Control createButtonBar(final Composite parent)
	  {
	      final Composite buttonBar = new Composite(parent, SWT.NONE);

	      final GridLayout layout = new GridLayout();
	      layout.numColumns = 2;
	      layout.makeColumnsEqualWidth = false;
	      layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
	      buttonBar.setLayout(layout);

	      final GridData data = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
	      data.grabExcessHorizontalSpace = true;
	      data.grabExcessVerticalSpace = false;
	      buttonBar.setLayoutData(data);

	      buttonBar.setFont(parent.getFont());

	      // place a button on the left
	      final Button leftButton = new Button(buttonBar, SWT.PUSH);
	      leftButton.setText("Left!");

	      final GridData leftButtonData = new GridData(SWT.LEFT, SWT.CENTER, true, true);
	      leftButtonData.grabExcessHorizontalSpace = true;
	      leftButtonData.horizontalIndent = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
	      leftButton.setLayoutData(leftButtonData);

	      // add the dialog's button bar to the right
	      final Control buttonControl = super.createButtonBar(buttonBar);
	      buttonControl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

	      return buttonBar;
	  }

	 
}
