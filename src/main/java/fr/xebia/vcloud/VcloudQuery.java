/*
 * Copyright (c) 2012. - Xebia
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

import com.vmware.vcloud.api.rest.schema.*;
import com.vmware.vcloud.api.rest.schema.ovf.MsgType;
import com.vmware.vcloud.api.rest.schema.ovf.SectionType;
import com.vmware.vcloud.sdk.*;
import com.vmware.vcloud.sdk.constants.FenceModeValuesType;
import com.vmware.vcloud.sdk.constants.UndeployPowerActionType;
import com.vmware.vcloud.sdk.constants.VMStatus;
import com.vmware.vcloud.sdk.constants.Version;
import com.vmware.vcloud.sdk.constants.query.ExpressionType;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import org.apache.http.HttpException;

import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * Query
 */
public class VcloudQuery {

    public static VcloudClient vcloudClient;

    public static void login(String vCloudURL, String username, String password)
            throws VCloudException, HttpException, IOException,
            KeyManagementException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyStoreException {
        VcloudClient.setLogLevel(Level.WARNING);
        vcloudClient = new VcloudClient(vCloudURL, Version.V1_5);
        vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory
                .getInstance());
        vcloudClient.login(username, password);
        HashMap<String, ReferenceType> organizationsMap = vcloudClient
                .getOrgRefsByName();
        if (!organizationsMap.isEmpty()) {
            System.out.println("Organizations:");
            System.out.println("-------------");
            for (String organizationName : organizationsMap.keySet())
                System.out.println("	" + organizationName);
        } else {
            System.out.println("	Invalid login for user " + username);
            System.exit(0);
        }

        QueryService queryService = vcloudClient.getQueryService();
        RecordResult<QueryResultVAppRecordType> recordResult = queryService.queryvAppIdRecords();
        List<QueryResultVAppRecordType> records = recordResult.getRecords();
        for (QueryResultVAppRecordType record : records) {
            String vAppId = record.getId();
            Vapp vapp = Vapp.getVappById(vcloudClient, vAppId);
            List<VM> childrenVms = vapp.getChildrenVms();
            for (VM vm : childrenVms) {
                Collection<NetworkConnectionType> networkConnections = vm.getNetworkConnections();
                for (NetworkConnectionType networkConnection : networkConnections) {
                    System.out.println(networkConnection);
                }
            }
        }

    }


    public static void main(String[] args) throws Exception {
        //login(url, username, password);

    }
}
