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

import com.google.common.collect.Iterables;
import com.vmware.vcloud.api.rest.schema.*;
import com.vmware.vcloud.sdk.*;
import com.vmware.vcloud.sdk.constants.query.ExpressionType;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import fr.xebia.vcloud.AbstractVcloud;
import fr.xebia.vcloud.FakeSSLSocketFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Xebia vCloud SDK workshop<br/>
 * <p/>
 * Create a Petclinic vApp from template, configure and run it
 */
public class VcloudAnswer extends AbstractVcloud {

    /**
     * Prepare for creating a new vApp
     *
     * @param vcloudClient The vCloud Client
     */
    public VcloudAnswer(VcloudClient vcloudClient) {
        super(vcloudClient);
    }

    /**
     * Login in vCloud Director
     *
     * @param username user@organization
     * @param password password
     * @throws Exception vCloud or SSL Exception
     */
    @Override
    public void login(String username, String password) throws Exception {
        vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory.getInstance());
        vcloudClient.login(username, password);
    }

    /**
     * Find VM template in catalogue
     *
     * @param queryVappTemplate The query to find the template
     * @param name              The VM name inside the vApp Template
     * @return the vAppTemplate for this VM
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud error
     */
    @Override
    public VappTemplate findVmTemplateByName(QueryParams<QueryVAppTemplateField> queryVappTemplate, String name) throws VCloudException {
        QueryService queryService = vcloudClient.getQueryService();
        RecordResult<QueryResultVAppTemplateRecordType> result = queryService.queryvAppTemplateIdRecords(queryVappTemplate);
        List<QueryResultVAppTemplateRecordType> records = result.getRecords();
        for (QueryResultVAppTemplateRecordType record : records) {
            VappTemplate vappTemplate = VappTemplate.getVappTemplateById(vcloudClient, record.getId());
            for (VappTemplate template : vappTemplate.getChildren()) {
                if (name.equalsIgnoreCase(template.getReference().getName())) {
                    return template;
                }
            }
        }
        return null;
    }

    /**
     * Create a Query params to find a Vapp Template by name
     *
     * @param vAppTemplateName The name of the vApp Template to find
     * @return a Query params that find <i>vAppTemplateName</i>
     */
    @Override
    public QueryParams<QueryVAppTemplateField> createVappQueryParams(String vAppTemplateName) {
        Expression expression = new Expression(QueryVAppTemplateField.NAME, vAppTemplateName,
                ExpressionType.EQUALS);
        Filter filter = new Filter(expression);
        QueryParams<QueryVAppTemplateField> queryParams = new QueryParams<QueryVAppTemplateField>();
        queryParams.setFilter(filter);
        return queryParams;
    }

    /**
     * Add a MySQL VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    @Override
    public void addVmMySQL(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        QueryParams<QueryVAppTemplateField> vappQueryParams = createVappQueryParams(V_APP_TEMPLATE_XEBIA);
        VappTemplate vmTemplateMysql = findVmTemplateByName(vappQueryParams, VM_TEMPLATE_MYSQL);
        SourcedCompositionItemParamType vmMysql = createVmFromTemplate(vmTemplateMysql, VM_MYSQL);
        vAppParamsType.getSourcedItem().add(vmMysql);
    }

    /**
     * Add an Apache Reverse Proxy VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    @Override
    public void addVmApache(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        QueryParams<QueryVAppTemplateField> vappQueryParams = createVappQueryParams(V_APP_TEMPLATE_XEBIA);
        VappTemplate vmTemplateMysql = findVmTemplateByName(vappQueryParams, VM_TEMPLATE_APACHE);
        SourcedCompositionItemParamType vmApache = createVmFromTemplate(vmTemplateMysql, VM_APACHE_RP);
        vAppParamsType.getSourcedItem().add(vmApache);
    }

    /**
     * Add 2 Tomcat VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    @Override
    public void addTwoVmsTomcat(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        QueryParams<QueryVAppTemplateField> vappQueryParams = createVappQueryParams(V_APP_TEMPLATE_XEBIA);
        VappTemplate vmTemplateMysql = findVmTemplateByName(vappQueryParams, VM_TEMPLATE_TOMCAT);
        SourcedCompositionItemParamType vmTomcat1 = createVmFromTemplate(vmTemplateMysql, VM_TOMCAT_1);
        SourcedCompositionItemParamType vmTomcat2 = createVmFromTemplate(vmTemplateMysql, VM_TOMCAT_2);
        List<SourcedCompositionItemParamType> sourcedItem = vAppParamsType.getSourcedItem();
        sourcedItem.add(vmTomcat1);
        sourcedItem.add(vmTomcat2);
    }

    /**
     * Create a VM from a VM Template
     *
     * @param vmTemplate the VM Template
     * @param vmName     the name of the new VM
     * @return a new VM configuration
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    @Override
    public SourcedCompositionItemParamType createVmFromTemplate(VappTemplate vmTemplate, String vmName) throws VCloudException {
        //Create a VM configuration
        SourcedCompositionItemParamType vAppTemplateItem = new SourcedCompositionItemParamType();
        ReferenceType vappTemplateVMRef = new ReferenceType();
        vappTemplateVMRef.setHref(vmTemplate.getReference().getHref());
        vappTemplateVMRef.setName(vmName);
        vAppTemplateItem.setSource(vappTemplateVMRef);

        // Add the Org internal network
        NetworkConnectionType networkConnection = Iterables.getOnlyElement(vmTemplate.getNetworkConnectionSection().getNetworkConnection());
        NetworkAssignmentType networkAssignment = new NetworkAssignmentType();
        networkAssignment.setInnerNetwork(networkConnection.getNetwork());
        networkAssignment.setContainerNetwork(getOrgNetwork().getName());
        List<NetworkAssignmentType> networkAssignments = vAppTemplateItem.getNetworkAssignment();
        networkAssignments.add(networkAssignment);

        return vAppTemplateItem;
    }

    /**
     * Start a VM and wait until online.<br/>
     * The VM is not yet available (eg : SSH) while this method return.
     *
     * @param vm the VM to start
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     * @throws java.util.concurrent.TimeoutException
     *          Timeout while waiting for the VM to start
     */
    @Override
    public void startVM(VM vm) throws VCloudException, TimeoutException {
        LOGGER.info("Starting VM : " + vm.getReference().getName());
        vm.powerOn().waitForTask(0);
        String ipAddress = Iterables.getOnlyElement(vm.getNetworkConnections()).getIpAddress();
        LOGGER.info("VM " + vm.getReference().getName() + " started (ip : " + ipAddress + ")");
    }

    /**
     * Configure the MySQL VM :
     * <ul>
     * <li>Add boostrap script</li>
     * <li>Set Root password</li>
     * </ul>
     *
     * @param vmMySQL The VM to configure
     * @throws VCloudException  VCloud Error
     * @throws IOException      Error while reading bootstrap script
     * @throws TimeoutException Error while updating VM
     */
    @Override
    public void configureVmMySQL(VM vmMySQL) throws VCloudException, IOException, TimeoutException {
        // Configure MySQL VM
        GuestCustomizationSectionType configMySQL = vmMySQL.getGuestCustomizationSection();
        // Set bootstrap script
        configMySQL.setCustomizationScript(loadScriptByVmName(VM_MYSQL));
        // Set root password
        configMySQL.setAdminPasswordAuto(false);
        configMySQL.setAdminPassword(ADMIN_PASSWORD);
        LOGGER.info("Updating VM MySQL");
        vmMySQL.updateSection(configMySQL).waitForTask(0);
    }

    /**
     * Configure a Tomcat VM :
     * <ul>
     * <li>Add boostrap script</li>
     * <li>Set Root password</li>
     * </ul>
     *
     * @param vmTomcat       The VM to configure
     * @param mySQLIpAddress The MySQL Ip Address
     * @throws VCloudException  VCloud Error
     * @throws IOException      Error while reading bootstrap script
     * @throws TimeoutException Error while updating VM
     */
    public void configureVmTomcat(VM vmTomcat, String mySQLIpAddress) throws VCloudException, IOException, TimeoutException {
        GuestCustomizationSectionType configTomcat1 = vmTomcat.getGuestCustomizationSection();
        // Get boostrap script
        String scriptTomcat = loadScriptByVmName(VM_TOMCAT_1);
        // set MySQL IP address in bootstrap script and configure VM
        configTomcat1.setCustomizationScript(String.format(scriptTomcat, mySQLIpAddress));
        configTomcat1.setAdminPasswordAuto(false);
        configTomcat1.setAdminPassword(ADMIN_PASSWORD);
        LOGGER.info("Updating VM Tomcat");
        vmTomcat.updateSection(configTomcat1).waitForTask(0);
    }

    /**
     * Configure a Tomcat VM :
     * <ul>
     * <li>Add boostrap script</li>
     * <li>Set Root password</li>
     * </ul>
     *
     * @param vmApache  The VM to configure
     * @param tomcatIp1 The Tomcat 1 Ip Address
     * @param tomcatIp2 The Tomcat 2 Ip Address
     * @throws VCloudException  VCloud Error
     * @throws IOException      Error while reading bootstrap script
     * @throws TimeoutException Error while updating VM
     */
    public void configureVmApache(VM vmApache, String tomcatIp1, String tomcatIp2) throws VCloudException, IOException, TimeoutException {
        GuestCustomizationSectionType configApache = vmApache.getGuestCustomizationSection();
        String scriptApache = loadScriptByVmName(VM_APACHE_RP);
        configApache.setCustomizationScript(String.format(scriptApache, tomcatIp1, tomcatIp2));
        configApache.setAdminPasswordAuto(false);
        configApache.setAdminPassword(ADMIN_PASSWORD);
        LOGGER.info("Updating VM Apache");
        vmApache.updateSection(configApache).waitForTask(0);
    }

}
