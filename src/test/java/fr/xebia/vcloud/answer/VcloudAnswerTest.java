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

package fr.xebia.vcloud.answer;

import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.constants.Version;
import org.junit.Before;
import org.junit.Test;

import java.util.ResourceBundle;

/**
 * @author cblonde@xebia.fr
 */
public class VcloudAnswerTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void startVapp() throws Exception {
        ResourceBundle ressources = ResourceBundle.getBundle("vmware");
        String url = ressources.getString("vcloud.url");
        VcloudAnswer vcloudAnswer = new VcloudAnswer(new VcloudClient(url, Version.V1_5));
        vcloudAnswer.startVapp();
    }
}
