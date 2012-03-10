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
import com.vmware.vcloud.sdk.QueryParams;
import com.vmware.vcloud.sdk.VappTemplate;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: charles
 * Date: 10/03/12
 * Time: 13:44
 *
 * @author charles.blonde@gmail.com
 */
public class VcloudCreateVmFromTemplateTest {
    public static final String VM_NAME = "vm-name";
    private VcloudClient vcloudClient;
    private AbstractVcloud vCloudSpy;
    private VappTemplate vAppTemplate;

    @Before
    public void setUp() throws Exception {
        vcloudClient = mock(VcloudClient.class);
        AbstractVcloud vcloud = new Vcloud(vcloudClient);
        vCloudSpy = PowerMockito.spy(vcloud);
        ReferenceType networkRef = mock(ReferenceType.class);
        when(networkRef.getName()).thenReturn(AbstractVcloud.RESEAU_INTERNE);
        Vdc vdc = mock(Vdc.class);

        doReturn(vAppTemplate).when(vCloudSpy).findVmTemplateByName((QueryParams<QueryVAppTemplateField>) anyObject(),anyString());
        doReturn(networkRef).when(vCloudSpy).getOrgNetwork();
        doReturn(vdc).when(vCloudSpy).getVdc();

        vAppTemplate = mock(VappTemplate.class);
        ReferenceType vAppTemplateRef = mock(ReferenceType.class);
        when(vAppTemplateRef.getHref()).thenReturn("HREF");
        when(vAppTemplate.getReference()).thenReturn(vAppTemplateRef);

        NetworkConnectionSectionType networkConnectionSectionType = mock(NetworkConnectionSectionType.class);
        NetworkConnectionType networkConnectionType = mock(NetworkConnectionType.class);

        when(networkConnectionSectionType.getNetworkConnection()).thenReturn(Lists.newArrayList(networkConnectionType));
        when(vAppTemplate.getNetworkConnectionSection()).thenReturn(networkConnectionSectionType);
    }

    @Test
    public void testCreateVmFromTemplate() throws Exception {
        SourcedCompositionItemParamType vm = vCloudSpy.createVmFromTemplate(vAppTemplate, VM_NAME);
        Assert.assertEquals("No Network added to VM", 1, vm.getNetworkAssignment().size());
        NetworkAssignmentType networkAssignmentType = Iterables.getOnlyElement(vm.getNetworkAssignment());
        Assert.assertEquals("Wrong network added to VM ??!!",AbstractVcloud.RESEAU_INTERNE,networkAssignmentType.getContainerNetwork());
        Assert.assertEquals("Wrong VM name",VM_NAME,vm.getSource().getName());
    }
}
