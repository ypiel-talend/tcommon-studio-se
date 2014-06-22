// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.rcp;

import org.talend.rcp.intro.ActionBarBuildHelper;

/**
 * qwei class global comment. Detailled comment
 */
public class RcpServices implements IRcpService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.rcp.IRcpService#activeSwitchProjectAction()
     */
    public void activeSwitchProjectAction() {
        // ApplicationActionBarAdvisor.activeSwitchAction();
        ActionBarBuildHelper.activeSwitchAction();
    }

}
