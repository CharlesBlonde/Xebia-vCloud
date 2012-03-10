/*
 * Copyright (c) 2012. Xebia and the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.xebia.vcloud;

import com.vmware.vcloud.api.rest.schema.GuestCustomizationSectionType;
import com.vmware.vcloud.sdk.Task;
import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.VcloudClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * User: charles
 * Date: 10/03/12
 * Time: 14:39
 *
 * @author charles.blonde@gmail.com
 */
public class VcloudConfigureVmTest {

    private VcloudClient vcloudClient;
    private AbstractVcloud vcloud;
    private VM vm;
    private GuestCustomizationSectionType customizationSectionType;
    private Task taskUpdateSection;
    private static final String IP_MYSQL = "XXX.XXX.XXX.XXX";
    private static final String IP_TOMCAT_1 = "XXX.XXX.XXX.XX1";
    private static final String IP_TOMCAT_2 = "XXX.XXX.XXX.XX2";

    @Before
    public void setUp() throws Exception {
        vcloudClient = mock(VcloudClient.class);
        vcloud = new Vcloud(vcloudClient);
        vm = mock(VM.class);
        customizationSectionType = new GuestCustomizationSectionType();
        when(vm.getGuestCustomizationSection()).thenReturn(customizationSectionType);

        taskUpdateSection = mock(Task.class);
        when(vm.updateSection(customizationSectionType)).thenReturn(taskUpdateSection);

    }

    @Test
    public void testConfigureVmMySQL() throws Exception {
        vcloud.configureVmMySQL(vm);
        GuestCustomizationSectionType customizationSection = vm.getGuestCustomizationSection();
        Assert.assertNotNull("Bootstrap script not set", customizationSection.getCustomizationScript());
        Assert.assertEquals("Wrong bootstrap script", AbstractVcloud.loadScriptByVmName(AbstractVcloud.VM_MYSQL),
                customizationSection.getCustomizationScript());
        Assert.assertEquals("AdminPasswordAuto must be false", Boolean.FALSE, customizationSection.isAdminPasswordAuto());
        Assert.assertNotNull("Root password is not set", customizationSection.getAdminPassword());
        Assert.assertEquals("Wrong root password", AbstractVcloud.ADMIN_PASSWORD, customizationSection.getAdminPassword());

        verify(vm, times(1)).updateSection(customizationSectionType);
        verify(taskUpdateSection, times(1)).waitForTask(anyInt());
    }

    @Test
    public void testConfigureVmTomcat() throws Exception {
        vcloud.configureVmTomcat(vm, IP_MYSQL);
        GuestCustomizationSectionType customizationSection = vm.getGuestCustomizationSection();
        Assert.assertNotNull("Bootstrap script not set", customizationSection.getCustomizationScript());
        Assert.assertEquals("Wrong bootstrap script or MySQL IP address not set",
                String.format(AbstractVcloud.loadScriptByVmName(AbstractVcloud.VM_TOMCAT_1), IP_MYSQL),
                customizationSection.getCustomizationScript());
        Assert.assertEquals("AdminPasswordAuto must be false", Boolean.FALSE, customizationSection.isAdminPasswordAuto());
        Assert.assertNotNull("Root password is not set", customizationSection.getAdminPassword());
        Assert.assertEquals("Wrong root password", AbstractVcloud.ADMIN_PASSWORD, customizationSection.getAdminPassword());

        verify(vm, times(1)).updateSection(customizationSectionType);
        verify(taskUpdateSection, times(1)).waitForTask(anyInt());
    }

    @Test
    public void testConfigureVmApache() throws Exception {
        vcloud.configureVmApache(vm, IP_TOMCAT_1, IP_TOMCAT_2);
        GuestCustomizationSectionType customizationSection = vm.getGuestCustomizationSection();
        Assert.assertNotNull("Bootstrap script not set", customizationSection.getCustomizationScript());
        Assert.assertEquals("Wrong bootstrap script or Tomcat IP addresses not set",
                String.format(AbstractVcloud.loadScriptByVmName(AbstractVcloud.VM_APACHE_RP), IP_TOMCAT_1, IP_TOMCAT_2),
                customizationSection.getCustomizationScript());
        Assert.assertEquals("AdminPasswordAuto must be false", Boolean.FALSE, customizationSection.isAdminPasswordAuto());
        Assert.assertNotNull("Root password is not set", customizationSection.getAdminPassword());
        Assert.assertEquals("Wrong root password", AbstractVcloud.ADMIN_PASSWORD, customizationSection.getAdminPassword());

        verify(vm, times(1)).updateSection(customizationSectionType);
        verify(taskUpdateSection, times(1)).waitForTask(anyInt());
    }
}
