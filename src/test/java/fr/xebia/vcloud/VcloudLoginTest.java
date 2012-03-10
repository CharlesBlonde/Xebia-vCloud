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

import com.vmware.vcloud.sdk.VcloudClient;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

/**
 *
 */
@RunWith(PowerMockRunner.class)
public class VcloudLoginTest {

    private VcloudClient vcloudClient;
    private String username;
    private String password;
    private AbstractVcloud vcloud;

    @Before
    public void setUp() throws Exception {
        vcloudClient = mock(VcloudClient.class);
        vcloud = new Vcloud(vcloudClient);
        username = "username";
        password = "password";
    }

    @Test
    public void testLogin() throws Exception {
        vcloud.login(username, password);
        Assert.assertNotNull(vcloud);
        verify(vcloudClient,times(1)).registerScheme(eq("https"), eq(443), (SSLSocketFactory) anyObject());
        verify(vcloudClient,times(1)).login(username,password);
    }
}
