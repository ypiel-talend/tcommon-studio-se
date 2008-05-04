// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.rcp.intro;

import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.rcp.i18n.Messages;
import org.talend.repository.ui.login.LoginDialog;

/**
 * This class controls all aspects of the application's execution.
 */
public class Application implements IApplication {

    public Object start(IApplicationContext context) throws Exception {
        Display display = PlatformUI.createDisplay();
        try {
            Shell shell = new Shell(display, SWT.ON_TOP);

            // If we cannot get the workspace lock, pop up an error dialog and then exit the application.
            if (!acquireWorkspaceLock(shell)) {
                return IApplication.EXIT_OK;
            }

            CorePlugin.getDefault().getRepositoryService().setRCPMode();

            IMigrationToolService service = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                    IMigrationToolService.class);
            service.executeWorspaceTasks();

            try {
                if (!logUserOnProject(display.getActiveShell())) {
                    Platform.endSplash();
                    return EXIT_OK;
                }
            } finally {
                if (shell != null) {
                    shell.dispose();
                }
            }

            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            } else {
                return IApplication.EXIT_OK;
            }
        } finally {
            display.dispose();
            // release workspace lock
            releaseWorkspaceLock();
        }

    }

    /**
     * Return <code>true</code> if the lock could be acquired.
     * 
     * @param shell
     * @throws IOException
     */
    private boolean acquireWorkspaceLock(Shell shell) {
        Location instanceLoc = Platform.getInstanceLocation();
        // It may be the first time for user to use tos, and we haven't create workspace yet.
        if (instanceLoc == null || instanceLoc.getURL() == null) {
            return true;
        }

        try {
            // try to lock the workspace
            if (instanceLoc.lock()) {
                return true;
            }
        } catch (Throwable t) {
            // do nothing
        }
        MessageDialog.openError(shell, Messages.getString("Application_workspaceInUseTitle"), Messages //$NON-NLS-1$
                .getString("Application.workspaceInUse")); //$NON-NLS-1$
        return false;
    }

    /**
     * Release the workspace lock before we exit the application.
     */
    private void releaseWorkspaceLock() {
        Location instanceLoc = Platform.getInstanceLocation();
        if (instanceLoc != null) {
            instanceLoc.release();
        }
    }

    private boolean logUserOnProject(Shell shell) {
        boolean logged = false;
        LoginDialog loginDialog = new LoginDialog(shell);
        logged = loginDialog.open() == LoginDialog.OK;
        return logged;
    }

    public void stop() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) {
            return;
        }
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                if (!display.isDisposed()) {
                    workbench.close();
                }
            }
        });
    }
}
