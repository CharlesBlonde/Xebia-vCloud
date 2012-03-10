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

import com.google.common.collect.Lists;
import com.vmware.vcloud.api.rest.schema.NetworkConnectionType;
import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.Task;
import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.VcloudClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.mockito.Mockito.*;

/**
 * User: charles
 * Date: 10/03/12
 * Time: 14:14
 *
 * @author charles.blonde@gmail.com
 */
public class VcloudStartVMTest {

    public static final String VM_NAME = "VM Name";
    private VcloudClient vcloudClient;
    private AbstractVcloud vcloud;
    private VM vm;
    private ReferenceType referenceType;
    private Task powerOnTask;
    private NetworkConnectionType networkConnectionType;

    @Before
    public void setUp() throws Exception {
        vcloudClient = mock(VcloudClient.class);
        vcloud = new Vcloud(vcloudClient);
        vm = mock(VM.class);
        referenceType = mock(ReferenceType.class);
        when(referenceType.getName()).thenReturn(VM_NAME);
        when(vm.getReference()).thenReturn(referenceType);

        powerOnTask = mock(Task.class);
        when(vm.powerOn()).thenReturn(powerOnTask);

        networkConnectionType = mock(NetworkConnectionType.class);
        when(networkConnectionType.getIpAddress()).thenReturn("XXX.XXX.XXX.XXX");
        when(vm.getNetworkConnections()).thenReturn(Lists.newArrayList(networkConnectionType));
    }

    @Test
    public void testStartVM() throws Exception {
        vcloud.startVM(vm);
        verify(vm,times(1)).powerOn();
        verify(powerOnTask,times(1)).waitForTask(anyInt());
    }
}
