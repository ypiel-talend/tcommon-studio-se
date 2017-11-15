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
package org.talend.core.repository.model;

import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.talend.commons.runtime.model.emf.provider.CreateOptionProvider;
import org.talend.commons.runtime.model.emf.provider.ResourceHandler;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.repository.item.ItemProductValuesHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProductValuesResourceHandler extends ResourceHandler {

    @Override
    public void preSave(Object resource, OutputStream outputStream, Map<?, ?> options) {
        if (resource instanceof XMLResource) {
            final Property prop = (Property) EcoreUtil.getObjectByType(((XMLResource) resource).getContents(),
                    PropertiesPackage.eINSTANCE.getProperty());
            if (prop != null) {
                Date saveDate = new Date();
                if (options.containsKey(CreateOptionProvider.CREATE)) {
                    ItemProductValuesHelper.setValuesWhenCreate(prop, saveDate);
                }

                ItemProductValuesHelper.setValuesWhenModify(prop, saveDate);

                // always remove the date
                prop.setCreationDate(null);
                prop.setModificationDate(null);
            }
        }
    }
}
