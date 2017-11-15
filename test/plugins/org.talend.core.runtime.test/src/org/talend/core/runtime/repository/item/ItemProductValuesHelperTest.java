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
package org.talend.core.runtime.repository.item;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.junit.Test;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("rawtypes")
public class ItemProductValuesHelperTest {

    @Test
    public void test_setValuesWhenImport_null() {
        boolean set = ItemProductValuesHelper.setValuesWhenImport(null, null);
        assertFalse(set);

        Property prop = mock(Property.class);
        set = ItemProductValuesHelper.setValuesWhenImport(prop, null);
        assertFalse(set);

        Project project = mock(Project.class);
        set = ItemProductValuesHelper.setValuesWhenImport(null, project);
        assertFalse(set);
    }

    @Test
    public void test_setValuesWhenImport_empty() {
        Property prop = mock(Property.class);
        EMap additionalProp = new BasicEMap();
        when(prop.getAdditionalProperties()).thenReturn(additionalProp);

        Project project = mock(Project.class);
        when(project.getProductVersion()).thenReturn("");

        boolean set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertFalse(set);
    }

    @Test
    public void test_setValuesWhenImport_invalid() {
        Property prop = mock(Property.class);
        EMap additionalProp = new BasicEMap();
        when(prop.getAdditionalProperties()).thenReturn(additionalProp);

        Project project = mock(Project.class);

        //
        when(project.getProductVersion()).thenReturn("Talend Open Studio for Big Data+6.5.1.20171110_1941");
        boolean set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertFalse(set);

        //
        when(project.getProductVersion()).thenReturn("Talend Open Studio for Big Data_6.5.1.20171110_1941");
        set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertFalse(set);

        when(project.getProductVersion()).thenReturn("Talend Open Studio for Big Data 6.5.1.20171110_1941");
        set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertFalse(set);
    }

    @Test
    public void test_setValuesWhenImport_tosdq() {
        // not productversion for TOS DQ
        fail("There is no product version value");
        // doTest_SetValuesForImport("","","");
    }

    @Test
    public void test_setValuesWhenImport_tos_snapshot() {
        doTest_SetValuesForImport("Talend Open Studio for Big Data-6.5.1.20171110_1941-SNAPSHOT",
                "Talend Open Studio for Big Data", "6.5.1.20171110_1941-SNAPSHOT");

    }

    @Test
    public void test_setValuesWhenImport_tos_release() {
        doTest_SetValuesForImport("Talend Open Studio-3.2.3.r35442", "Talend Open Studio", "3.2.3.r35442");
        doTest_SetValuesForImport("Talend Open Studio for Data Integration-5.3.2.r113626",
                "Talend Open Studio for Data Integration", "5.3.2.r113626");
        doTest_SetValuesForImport("Talend Open Studio for Big Data-6.5.1.20171110_1941", "Talend Open Studio for Big Data",
                "6.5.1.20171110_1941");
    }

    @Test
    public void test_setValuesWhenImport_tos_patch() {
        doTest_SetValuesForImport("Talend Data Fabric-6.3.1.20171030_0901-patch", "Talend Data Fabric",
                "6.3.1.20171030_0901-patch");
    }

    private void doTest_SetValuesForImport(String projectProductVersion, String importFullName, String importVersion) {
        Property prop = mock(Property.class);
        EMap additionalProp = new BasicEMap();
        when(prop.getAdditionalProperties()).thenReturn(additionalProp);

        Project project = mock(Project.class);
        when(project.getProductVersion()).thenReturn(projectProductVersion);

        boolean set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertTrue("Set import product values failure from " + projectProductVersion, set);

        assertFalse(additionalProp.isEmpty());

        // no hasKey, hasEntry, so try list way
        // assertThat(additionalProp,hasKey(ItemProductKeys.FULLNAME.getImportKey()));

        assertThat("Failed to set the import product full name from " + projectProductVersion,
                additionalProp.get(ItemProductKeys.FULLNAME.getImportKey()), equalTo(importFullName));
        assertThat("Failed to set the import product version from " + projectProductVersion,
                additionalProp.get(ItemProductKeys.VERSION.getImportKey()), equalTo(importVersion));
    }
}
