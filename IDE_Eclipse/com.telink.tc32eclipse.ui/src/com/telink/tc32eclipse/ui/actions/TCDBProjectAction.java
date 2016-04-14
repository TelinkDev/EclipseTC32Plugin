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
 * $Id: UploadProjectAction.java 851 21.0.38-07 19:37:00Z innot $
 *     
 *******************************************************************************/
package com.telink.tc32eclipse.ui.actions;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.progress.UIJob;

import com.telink.tc32eclipse.TC32Plugin;
import com.telink.tc32eclipse.core.natures.TC32ProjectNature;
import com.telink.tc32eclipse.core.properties.TCDBProperties;
import com.telink.tc32eclipse.core.properties.TC32ProjectProperties;
import com.telink.tc32eclipse.core.properties.ProjectPropertyManager;
//import com.telink.tc32eclipse.core.tcdb.TCDBAction;
//import com.telink.tc32eclipse.core.tcdb.BaseBytesProperties;
import com.telink.tc32eclipse.core.tcdb.ProgrammerConfig;
import com.telink.tc32eclipse.core.tcdb.TCDBException;
import com.telink.tc32eclipse.core.tcdb.TCDBSchedulingRule;
import com.telink.tc32eclipse.core.toolinfo.TCDB;
//import com.telink.tc32eclipse.core.toolinfo.fuses.FuseType;
//import com.telink.tc32eclipse.core.util.TC32MCUidConverter;
import com.telink.tc32eclipse.mbs.BuildMacro;
import com.telink.tc32eclipse.ui.dialogs.TCDBErrorDialogJob;

//import org.eclipse.core.resources.ResourcesPlugin;
////import com.telink.tc32eclipse.core.preferences.TCDBPreferences;
//import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.debug.ui.DebugUITools;

/**
 * @author Peter Shieh
 * @since 0.1
 * @since 2.3 Added optional delay between TCDB invocations
 * 
 */
public class TCDBProjectAction extends ActionDelegate implements IWorkbenchWindowActionDelegate {

	private final static String	TITLE_UPLOAD			= "Flash Upload";


	private final static String	MSG_MISSING_FILE		= "The file [{0}] for the {1} memory does not exist or is not readable\n\n"
																+ "Maybe the project needs to be build first.";

	
	private IProject			fProject;
	
	  
	private static final String NATURE_ID = "com.telink.tc32eclipse.core.TC32nature";

	/**
	 * Constructor for this Action.
	 */
	public TCDBProjectAction() {
		super();
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {

		// The user has selected a different Workbench object.
		// If it is an IProject we keep it.

		Object item;

		if (selection instanceof IStructuredSelection) {
			item = ((IStructuredSelection) selection).getFirstElement();
		} else {
			return;
		}
		if (item == null) {
			return;
		}
		IProject project = null;

		// See if the given is an IProject (directly or via IAdaptable)
		if (item instanceof IProject) {
			project = (IProject) item;
		} else if (item instanceof IResource) {
			project = ((IResource) item).getProject();
		} else if (item instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) item;
			project = (IProject) adaptable.getAdapter(IProject.class);
			if (project == null) {
				// Try ICProject -> IProject
				ICProject cproject = (ICProject) adaptable.getAdapter(ICProject.class);
				if (cproject == null) {
					// Try ICElement -> ICProject -> IProject
					ICElement celement = (ICElement) adaptable.getAdapter(ICElement.class);
					if (celement != null) {
						cproject = celement.getCProject();
					}
				}
				if (cproject != null) {
					project = cproject.getProject();
				}
			}
		}

		fProject = project;
		
		
		try {
			 //TC32ProjectNature.addTC32Nature((TC32ProjectNature)project);
			IProjectNature nature = null;
           try{
                nature = project.getNature(NATURE_ID);
            } catch (CoreException e){
            	IStatus status = new Status(Status.ERROR, TC32Plugin.PLUGIN_ID,
    					"Can't access project nature", e);
    			TC32Plugin.getDefault().log(status);
            }
			
			TC32ProjectNature.addTC32Nature((TC32ProjectNature) nature );
			 
	    } catch (CoreException e) {
		// Log the Exception
			IStatus status = new Status(Status.ERROR, TC32Plugin.PLUGIN_ID,
					"Can't access project nature", e);
			TC32Plugin.getDefault().log(status);
	    }
	}
	
	IResource extractSelection(ISelection sel) {
	      if (!(sel instanceof IStructuredSelection))
	         return null;
	      IStructuredSelection ss = (IStructuredSelection) sel;
	      Object element = ss.getFirstElement();
	      if (element instanceof IResource)
	         return (IResource) element;
	      if (!(element instanceof IAdaptable))
	         return null;
	      IAdaptable adaptable = (IAdaptable)element;
	      Object adapter = adaptable.getAdapter(IResource.class);
	      return (IResource) adapter;
	   }

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {
		
		
		if (fProject == null) {
			fProject = ProjectPropertyManager.getActiveProject();
		}

		// Get the active build configuration
		IManagedBuildInfo bi = ManagedBuildManager.getBuildInfo(fProject);
		IConfiguration activecfg = bi.getDefaultConfiguration();

		// Get the avr properties for the active configuration
		TC32ProjectProperties targetprops = ProjectPropertyManager.getPropertyManager(fProject)
				.getActiveProperties();

		// Check if the TCDB properties are valid.
		// if not the checkProperties() method will display an error message box
		if (!checkProperties(activecfg, targetprops)) {
			return;
		}

		// Everything is fine -> run TCDB
		runTCDB(activecfg, targetprops);
	}

	/**
	 * Check that the current properties are valid.
	 * <p>
	 * This method will check that:
	 * <ul>
	 * <li>there has been a Programmer selected</li>
	 * <li>TCDB supports the selected MCU</li>
	 * <li>there are some actions to perform</li>
	 * <li>all source files exist</li>
	 * <li>the fuse bytes (if uploaded) are valid for the target MCU</li>
	 * </ul>
	 * <p>
	 * 
	 * @param buildcfg
	 *            The current build configuration
	 * @param props
	 *            The current Properties
	 * @return <code>true</code> if everything is OK.
	 */
	private boolean checkProperties(IConfiguration buildcfg, TC32ProjectProperties props) {
		
		
		//TCDBPreferences tc32pref = new TCDBPreferences();
		//IPreferenceStore store = TCDBPreferences.getPreferenceStore();	
		IResource currProj = DebugUITools.getSelectedResource().getProject();
		String projName = currProj.getName();
		

		
        if (projName == null)
        	return false;
		// Check all referenced files
		// It would be cumbersome to go through all possible cases. Instead we
		// convert all action arguments back to TCDBActions and get the
		// filename from it.
		IPath invalidfile = null;
		String formemtype = null;

		TCDBProperties tpro = props.getTCDBProperties();
		
		String filename;
		
		if (tpro.getBootbin())
			filename = tpro.getFlashFile();
		else
			filename = projName + ".bin";
		
		//String filename = projName + ".bin"; //action.getFilename();
		//if (filename == null)
		//	continue;
		IPath rawfile = new Path(filename);
		IPath unresolvedfile = rawfile;
		IPath resolvedfile = rawfile;
		if (!rawfile.isAbsolute()) {
			// The filename is relative to the build folder. Get the build
			// folder and append our filename. Then resolve any macros
			unresolvedfile = buildcfg.getBuildData().getBuilderCWD().append(rawfile);
			resolvedfile = new Path(BuildMacro.resolveMacros(buildcfg, unresolvedfile
					.toString()));
		}
		File realfile = resolvedfile.toFile();
		if (!realfile.canRead()) {
			invalidfile = unresolvedfile;
		}
		if (invalidfile != null) {
			String message = MessageFormat.format(MSG_MISSING_FILE, invalidfile.toString(),
					formemtype);
			MessageDialog.openError(getShell(), TITLE_UPLOAD, message);
			return false;
		}
		
		props.setBinaryTargetName(resolvedfile.toString());

// PS
		///props.getTCDBProperties().setWriteFlash(false);
		
		// Everything is OK
		return true;
	}

	/**
	 * Start the TCDB UploadJob.
	 * 
	 * @param buildcfg
	 *            The build configuration for resolving macros.
	 * @param props
	 *            The AVR properties for the project / the current configuration
	 */
	private void runTCDB(IConfiguration buildcfg, TC32ProjectProperties props) {
		
		

		TCDBProperties TCDBprops = props.getTCDBProperties();

		// get the list of normal (non-action) arguments
		List<String> optionargs = TCDBprops.getArguments();

		// get a list of actions
		List<String> actionargs = TCDBprops.getActionArguments(buildcfg, true);
		
		// Get the ProgrammerConfig in case we need to display an error
		// message
		ProgrammerConfig programmer = TCDBprops.getProgrammer();
			
		actionargs.add(props.getBinaryTargetName()); // + " " + programmer.getArg2String());


		// Set the working directory to the CWD of the active build config, so that
		// relative paths are resolved correctly.
		IPath cwdunresolved = buildcfg.getBuildData().getBuilderCWD();
		IPath cwd = new Path(BuildMacro.resolveMacros(buildcfg, cwdunresolved.toString()));

		Job uploadjob = new UploadJob(optionargs, actionargs, cwd, programmer);

		uploadjob.setRule(new TCDBSchedulingRule(programmer));
		uploadjob.setPriority(Job.LONG);
		uploadjob.setUser(true);

		uploadjob.schedule();

	}

	/**
	 * The background Job to execute the requested TCDB commands.
	 * 
	 */
	private class UploadJob extends Job {

		private final List<String>		fOptions;
		private final List<String>		fActions;
		private final IPath				fCwd;
		private final ProgrammerConfig	fProgrammerConfig;

		public UploadJob(List<String> options, List<String> actions, IPath cwd,
				ProgrammerConfig programmer) {
			super("Flash Upload");
			fOptions = options;
			fActions = actions;
			fCwd = cwd;
			fProgrammerConfig = programmer;
		}

		@Override
		public IStatus run(IProgressMonitor monitor) {

			try {
				monitor.beginTask("Running Binary Loader", fActions.size());

				// init console. Clears the console and puts it on top.
				// TCDB is forced to use the console, so the user will always
				// see the output, regardless of the "use console" flag.
				MessageConsole console = TC32Plugin.getDefault().getConsole("Telink Binary Loader Console");
				console.clearConsole();
				console.activate();

				TCDB tools = TCDB.getDefault();

				// Append all requested actions
				// The reason this is done here is because in earlier versions
				// of the plugin each action was send separately to TCDB to better
				// track the progress.
				// However some users complained that this slows the whole upload process down.
				// So now we sent all actions in one go, as the user can monitor the progress
				// in the console anyway.
				fOptions.addAll(fActions);
				monitor.subTask("TCDB");

				// Now TCDB can be started.
				tools.runCommand(fOptions, new SubProgressMonitor(monitor, 1), true, fCwd,
						fProgrammerConfig);

			} catch (TCDBException ade) {
				// Show an Error message and exit
				Display display = PlatformUI.getWorkbench().getDisplay();
				if (display != null && !display.isDisposed()) {
					UIJob messagejob = new TCDBErrorDialogJob(display, ade, fProgrammerConfig
							.getId());
					messagejob.setPriority(Job.INTERACTIVE);
					messagejob.schedule();
					try {
						messagejob.join(); // block until the dialog is closed.
					} catch (InterruptedException e) {
						// Don't care if the dialog is interrupted from outside.
					}
				}
			} finally {
				monitor.done();
			}

			return Status.OK_STATUS;
		}
	}

	/**
	 * Get the current Shell.
	 * 
	 * @return <code>Shell</code> of the active Workbench window.
	 */
	private Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

}
