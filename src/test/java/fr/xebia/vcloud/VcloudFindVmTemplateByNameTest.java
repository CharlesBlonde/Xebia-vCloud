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
import com.vmware.vcloud.api.rest.schema.QueryResultVAppTemplateRecordType;
import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.*;
import com.vmware.vcloud.sdk.constants.query.ExpressionType;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * User: charles
 * Date: 10/03/12
 * Time: 10:42
 *
 * @author charles.blonde@gmail.com
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VappTemplate.class})
public class VcloudFindVmTemplateByNameTest {

    private VcloudClient vcloudClient;
    private AbstractVcloud vcloud;
    private QueryService queryService;
    private String templateName;
    private String vmTemplateName;
    private RecordResult recordResult;
    private QueryResultVAppTemplateRecordType vAppTemplateRecord1;
    private QueryResultVAppTemplateRecordType vAppTemplateRecord2;
    private ArrayList<QueryResultVAppTemplateRecordType> vmTemplateList;
    private String idVappTemplate1;
    private String idVappTemplate2;
    private VappTemplate vappTemplate1;
    private VappTemplate vappTemplate2;
    private VappTemplate vmTemplate1;
    private VappTemplate vmTemplate2;
    private ReferenceType referenceTemplate1;
    private ReferenceType referenceTemplate2;
    private QueryParams<QueryVAppTemplateField> queryParams;

    @Before
    public void setUp() throws Exception {
        vcloudClient = mock(VcloudClient.class);
        vcloud = new Vcloud(vcloudClient);

        templateName = "vApp - Xebia";
        vmTemplateName = "vm-mysql";

        queryService = mock(QueryService.class);
        recordResult = mock(RecordResult.class);
        vAppTemplateRecord1 = mock(QueryResultVAppTemplateRecordType.class);
        vAppTemplateRecord2 = mock(QueryResultVAppTemplateRecordType.class);
        vappTemplate1 = mock(VappTemplate.class);
        vappTemplate2 = mock(VappTemplate.class);

        vmTemplate1 = mock(VappTemplate.class);
        vmTemplate2 = mock(VappTemplate.class);

        referenceTemplate1 = mock(ReferenceType.class);
        when(referenceTemplate1.getName()).thenReturn("wrongName");
        referenceTemplate2 = mock(ReferenceType.class);
        when(referenceTemplate2.getName()).thenReturn(vmTemplateName);

        when(vmTemplate1.getReference()).thenReturn(referenceTemplate1);
        when(vmTemplate2.getReference()).thenReturn(referenceTemplate2);

        when(vappTemplate1.getChildren()).thenReturn(Lists.<VappTemplate>newArrayList());
        when(vappTemplate2.getChildren()).thenReturn(Lists.newArrayList(vmTemplate1, vmTemplate2));

        vmTemplateList = Lists.newArrayList(vAppTemplateRecord1, vAppTemplateRecord2);
        idVappTemplate1 = new String("idVappTemplate1");
        idVappTemplate2 = new String("idVappTemplate2");

        when(vAppTemplateRecord1.getId()).thenReturn(idVappTemplate1);
        when(vAppTemplateRecord2.getId()).thenReturn(idVappTemplate2);
        when(recordResult.getRecords()).thenReturn(vmTemplateList);

        mockStatic(VappTemplate.class);
        when(VappTemplate.getVappTemplateById(vcloudClient, idVappTemplate1)).thenReturn(vappTemplate1);
        when(VappTemplate.getVappTemplateById(vcloudClient, idVappTemplate2)).thenReturn(vappTemplate2);

        when(queryService.queryvAppTemplateIdRecords(Matchers.<QueryParams<QueryVAppTemplateField>>anyObject())).thenReturn(recordResult);
        when(vcloudClient.getQueryService()).thenReturn(queryService);

        Expression expression = new Expression(QueryVAppTemplateField.NAME, templateName, ExpressionType.EQUALS);
        Filter filter = new Filter(expression);
        queryParams = new QueryParams<QueryVAppTemplateField>();
        queryParams.setFilter(filter);
    }

    @Test
    public void testFindVmTemplateByName() throws Exception {
        VappTemplate vmTemplateByName = vcloud.findVmTemplateByName(queryParams, vmTemplateName);
        Assert.assertNotNull(vmTemplateByName);
        Assert.assertEquals(vmTemplate2, vmTemplateByName);
    }
}
