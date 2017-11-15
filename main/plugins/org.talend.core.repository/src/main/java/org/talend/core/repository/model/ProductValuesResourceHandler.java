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
import org.talend.commons.runtime.model.emf.provider.OptionProvider;
import org.talend.commons.runtime.model.emf.provider.ResourceHandler;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.core.runtime.repository.item.ItemProductValuesHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProductValuesResourceHandler extends ResourceHandler {

    public static final OptionProvider migrationOption = new OptionProvider() {

        @Override
        public String getName() {
            return "option_migration";
        }

        @Override
        public Object getValue() {
            return Boolean.TRUE;
        }

    };

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

                // if migration, nothing to do, becuase it have been processed in migration task
                if (!options.containsKey(migrationOption.getName())) {

                    if (prop.getAdditionalProperties().containsKey(ItemProductKeys.FULLNAME.getModifiedKey())
                            || prop.getAdditionalProperties().containsKey(ItemProductKeys.FULLNAME.getCreatedKey())) {
                        // if existed, just do update
                        ItemProductValuesHelper.setValuesWhenModify(prop, saveDate);
                    } else {// no any keys, do migration too.
                            // currently, especially when copy/paste items, won't do migration task in first logon
                        ItemProductValuesHelper.setValuesWhenMigrate(prop);
                    }

                    // always remove the date
                    prop.setCreationDate(null);
                    prop.setModificationDate(null);
                }
            }
        }
    }
}
