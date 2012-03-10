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

import com.vmware.vcloud.sdk.Filter;
import com.vmware.vcloud.sdk.QueryParams;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;

/**
 * User: charles
 * Date: 10/03/12
 * Time: 11:50
 *
 * @author charles.blonde@gmail.com
 */
public class VcloudCreateVappQueryParamsTest {
    private VcloudClient vcloudClient;
    private AbstractVcloud vcloud;
    private String vAppTemplateName;
    private String exceptedFilterText;

    @Before
    public void setUp() throws Exception {
        vcloudClient = mock(VcloudClient.class);
        vcloud = new Vcloud(vcloudClient);
        vAppTemplateName = new String("vApp - Xebia");
        exceptedFilterText = "(name=="+vAppTemplateName.replaceAll(" ","+")+")";
    }

    @Test
    public void testCreateVappQueryParams() throws Exception {
        QueryParams<QueryVAppTemplateField> vappQueryParams = vcloud.createVappQueryParams(vAppTemplateName);
        Assert.assertNotNull(vappQueryParams);
        Filter filter = vappQueryParams.getFilter();

        Method getFilterText = filter.getClass().getDeclaredMethod("getFilterText");
        getFilterText.setAccessible(true);
        String filterText = (String) getFilterText.invoke(filter);
        Assert.assertEquals("The filter String is not valid",exceptedFilterText,filterText);
    }
}
