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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.vmware.vcloud.api.rest.schema.*;
import com.vmware.vcloud.sdk.*;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * User: charles
 * Date: 10/03/12
 * Time: 12:21
 *
 * @author charles.blonde@gmail.com
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Organization.class, Vdc.class, Vcloud.class})
public class VcloudAddVMsTest {
    public static final String RESEAU_INTERNE = "Reseau interne";
    private VcloudClient vcloudClient;
    private AbstractVcloud vcloud;
    private ComposeVAppParamsType composeParams;
    private AbstractVcloud vCloudSpy;

    @Before
    public void setUp() throws Exception {
        vcloudClient = mock(VcloudClient.class);
        vcloud = new Vcloud(vcloudClient);
        VappTemplate vAppTemplate = mock(VappTemplate.class);
        ReferenceType vAppTemplateRef = mock(ReferenceType.class);
        when(vAppTemplateRef.getHref()).thenReturn("HREF");
        when(vAppTemplate.getReference()).thenReturn(vAppTemplateRef);

        NetworkConnectionSectionType networkConnectionSectionType = mock(NetworkConnectionSectionType.class);
        NetworkConnectionType networkConnectionType = mock(NetworkConnectionType.class);

        when(networkConnectionSectionType.getNetworkConnection()).thenReturn(Lists.newArrayList(networkConnectionType));
        when(vAppTemplate.getNetworkConnectionSection()).thenReturn(networkConnectionSectionType);

        vCloudSpy = PowerMockito.spy(vcloud);
        ReferenceType networkRef = mock(ReferenceType.class);
        when(networkRef.getName()).thenReturn(RESEAU_INTERNE);
        Vdc vdc = mock(Vdc.class);

        SourcedCompositionItemParamType sourcedCompositionItemParamTypeMySQL = mock(SourcedCompositionItemParamType.class);
        ReferenceType sourceMySQL = mock(ReferenceType.class);
        when(sourceMySQL.getName()).thenReturn(AbstractVcloud.VM_MYSQL);
        when(sourcedCompositionItemParamTypeMySQL.getSource()).thenReturn(sourceMySQL);

        SourcedCompositionItemParamType sourcedCompositionItemParamTypeApache = mock(SourcedCompositionItemParamType.class);
        ReferenceType sourceApache = mock(ReferenceType.class);
        when(sourceApache.getName()).thenReturn(AbstractVcloud.VM_APACHE_RP);
        when(sourcedCompositionItemParamTypeApache.getSource()).thenReturn(sourceApache);

        SourcedCompositionItemParamType sourcedCompositionItemParamTypeTC1 = mock(SourcedCompositionItemParamType.class);
        ReferenceType sourceTC1 = mock(ReferenceType.class);
        when(sourceTC1.getName()).thenReturn(AbstractVcloud.VM_TOMCAT_1);
        when(sourcedCompositionItemParamTypeTC1.getSource()).thenReturn(sourceTC1);

        SourcedCompositionItemParamType sourcedCompositionItemParamTypeTC2 = mock(SourcedCompositionItemParamType.class);
        ReferenceType sourceTC2 = mock(ReferenceType.class);
        when(sourceTC2.getName()).thenReturn(AbstractVcloud.VM_TOMCAT_2);
        when(sourcedCompositionItemParamTypeTC2.getSource()).thenReturn(sourceTC2);


        doReturn(vAppTemplate).when(vCloudSpy).findVmTemplateByName((QueryParams<QueryVAppTemplateField>) anyObject(), anyString());
        doReturn(networkRef).when(vCloudSpy).getOrgNetwork();
        doReturn(vdc).when(vCloudSpy).getVdc();

        doReturn(sourcedCompositionItemParamTypeMySQL).when(vCloudSpy).createVmFromTemplate((VappTemplate) anyObject(), eq(AbstractVcloud.VM_MYSQL));
        doReturn(sourcedCompositionItemParamTypeApache).when(vCloudSpy).createVmFromTemplate((VappTemplate) anyObject(), eq(AbstractVcloud.VM_APACHE_RP));
        doReturn(sourcedCompositionItemParamTypeTC1).when(vCloudSpy).createVmFromTemplate((VappTemplate) anyObject(), eq(AbstractVcloud.VM_TOMCAT_1));
        doReturn(sourcedCompositionItemParamTypeTC2).when(vCloudSpy).createVmFromTemplate((VappTemplate) anyObject(), eq(AbstractVcloud.VM_TOMCAT_2));

    }

    @Test
    public void testAddVmMySQL() throws Exception {
        composeParams = vCloudSpy.createComposeParams("vApp name");
        vCloudSpy.addVmMySQL(composeParams);
        Assert.assertEquals("No VM added to vApp", 1, composeParams.getSourcedItem().size());
        SourcedCompositionItemParamType sourceItem = Iterables.getOnlyElement(composeParams.getSourcedItem());
        Assert.assertEquals("Wrong VM name", AbstractVcloud.VM_MYSQL, sourceItem.getSource().getName());
    }

    @Test
    public void testAddVmApache() throws Exception {
        composeParams = vCloudSpy.createComposeParams("vApp name");
        vCloudSpy.addVmApache(composeParams);
        Assert.assertEquals("No VM added to vApp", 1, composeParams.getSourcedItem().size());
        SourcedCompositionItemParamType sourceItem = Iterables.getOnlyElement(composeParams.getSourcedItem());
        Assert.assertEquals("Wrong VM name", AbstractVcloud.VM_APACHE_RP, sourceItem.getSource().getName());
    }

    @Test
    public void testAddTwoVmsTomcat() throws Exception {
        composeParams = vCloudSpy.createComposeParams("vApp name");
        vCloudSpy.addTwoVmsTomcat(composeParams);
        Assert.assertEquals("2 VM must be added to the vApp", 2, composeParams.getSourcedItem().size());
        SourcedCompositionItemParamType firstSourceItem = composeParams.getSourcedItem().get(0);
        Assert.assertEquals("Wrong VM name", AbstractVcloud.VM_TOMCAT_1, firstSourceItem.getSource().getName());

        SourcedCompositionItemParamType secondSourceItem = composeParams.getSourcedItem().get(1);
        Assert.assertEquals("Wrong VM name", AbstractVcloud.VM_TOMCAT_2, secondSourceItem.getSource().getName());
    }
}
