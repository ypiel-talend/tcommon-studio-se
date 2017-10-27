// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.hadoop;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EMap;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.hd.IDynamicDistributionManager;

/**
 * created by cmeng on Jul 20, 2015 Detailled comment
 *
 */
public class BigDataBasicUtil {

    public static Object getFramework(Item item) {
        if (item == null) {
            return null;
        }
        Property property = item.getProperty();
        if (property != null) {
            EMap additionalProperties = property.getAdditionalProperties();
            if (additionalProperties != null) {
                return additionalProperties.get(HadoopConstants.FRAMEWORK);
            }
        }
        return null;
    }

    public static void reloadAllUsersDynamicDistributions(IProgressMonitor monitor) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            IHadoopDistributionService hdService = (IHadoopDistributionService) GlobalServiceRegister.getDefault()
                    .getService(IHadoopDistributionService.class);
            if (hdService != null) {
                IDynamicDistributionManager ddManager = hdService.getDynamicDistributionManager();
                if (ddManager != null && ddManager.isLoaded()) {
                    try {
                        ddManager.reloadAllUsersDynamicDistributions(monitor);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
    }
}
