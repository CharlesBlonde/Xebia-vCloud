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

import com.vmware.vcloud.api.rest.schema.*;
import com.vmware.vcloud.sdk.*;
import com.vmware.vcloud.sdk.constants.query.ExpressionType;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Xebia vCloud SDK workshop<br/>
 * <p/>
 * Create a Petclinic vApp from template, configure and run it
 */
public class Vcloud extends AbstractVcloud {
    private static final Logger LOGGER = LoggerFactory.getLogger(Vcloud.class);

    /**
     * Prepare for creating a new vApp
     *
     * @param vcloudClient The vCloud Client
     */
    public Vcloud(VcloudClient vcloudClient) {
        super(vcloudClient);
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
     * @see fr.xebia.vcloud.answer.VcloudAnswer#startVM(com.vmware.vcloud.sdk.VM) Soluce
     */
    @Override
    public void startVM(VM vm) throws VCloudException, TimeoutException {
        //TODO implement
        throw new RuntimeException("To be implemented");
        // Start VM
        /**
         * @see com.vmware.vcloud.sdk.VM#powerOn()
         * @see Task#waitForTask(long)
         * 0 for unlimited timeout
         */

        // Optional : print VM IP address
        /**
         * @see com.vmware.vcloud.sdk.VM#getNetworkConnections()
         * @see com.vmware.vcloud.api.rest.schema.NetworkConnectionType#getIpAddress()
         */
    }

    @Override
    public void configureVmMySQL(VM vm) throws VCloudException, IOException, TimeoutException {
        //TODO implement
        /**
         * Configure MySQL VM
         * @see com.vmware.vcloud.sdk.VM#getGuestCustomizationSection()
         * @see GuestCustomizationSectionType#setCustomizationScript(String)
         * @see AbstractVcloud#loadScriptByVmName(String)
         * @see VM_MYSQL
         * @see GuestCustomizationSectionType#setAdminPasswordAuto(Boolean)
         * @see GuestCustomizationSectionType#setAdminPassword(String)
         * @see ADMIN_PASSWORD
         *
         * Apply
         * @see VM#updateSection(com.vmware.vcloud.api.rest.schema.ovf.SectionType)
         * @see Task#waitForTask(long)
         *
         */
    }

    @Override
    public void configureVmTomcat(VM vmTomcat, String mySQLIpAddress) throws VCloudException, IOException, TimeoutException {
        //TODO implement
        /**
         * Configure Tomcat VMs
         * Configure bootstrap script with MySQL IP.
         * Tomcat bootstrap script is ready to be formatted with MySQL IP
         * @see AbstractVcloud#loadScriptByVmName(String)
         * @see com.vmware.vcloud.sdk.VM#getNetworkConnections()
         * @see com.vmware.vcloud.api.rest.schema.NetworkConnectionType#getIpAddress()
         * @see String#format(String, Object...)
         *
         * repeat steps from MySQL VM
         */
    }

    @Override
    public void configureVmApache(VM vmApache, String tomcatIp1, String tomcatIp2) throws VCloudException, IOException, TimeoutException {
        //TODO implement
        /**
         * Configure Apache VM
         * Apache bootstrap script is ready to be formatted with the 2 Tomcat IPs
         * Repeat steps from other VMs
         */
    }

    /**
     * Add a MySQL VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     * @see fr.xebia.vcloud.answer.VcloudAnswer#addVmMySQL(com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType) Soluce
     */
    @Override
    public void addVmMySQL(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        //TODO implement
        throw new RuntimeException("To be implemented");
        // Find Mysql Template and create a VM with it
        /**
         * @see V_APP_TEMPLATE_XEBIA
         * @see VM_TEMPLATE_MYSQL
         * @see findVmTemplateByName()
         * @see VM_MYSQL
         * @see createVmFromTemplate()
         * Add VM to vApp
         * @see com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType#getSourcedItem()
         */
    }

    /**
     * Add an Apache Reverse Proxy VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     * @see fr.xebia.vcloud.answer.VcloudAnswer#addVmApache(com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType) Soluce
     */
    @Override
    public void addVmApache(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        //TODO implement
        throw new RuntimeException("To be implemented");
        // Find Apache Template and create a VM with it
        /**
         * @see V_APP_TEMPLATE_XEBIA
         * @see VM_TEMPLATE_APACHE
         * @see findVmTemplateByName()
         * @see VM_APACHE_RP
         * @see createVmFromTemplate()
         * Add VM to vApp
         * @see com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType#getSourcedItem()
         */
    }

    /**
     * Add 2 Tomcat VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     * @see fr.xebia.vcloud.answer.VcloudAnswer#addTwoVmsTomcat(com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType) Soluce
     */
    @Override
    public void addTwoVmsTomcat(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        //TODO implement
        throw new RuntimeException("To be implemented");
        // Find Tomcat Template and create 2 VMs with it
        /**
         * @see V_APP_TEMPLATE_XEBIA
         * @see VM_TEMPLATE_TOMCAT
         * @see findVmTemplateByName()
         * @see VM_TOMCAT_1
         * @see VM_TOMCAT_2
         * @see createVmFromTemplate()
         * Add VMs to vApp
         * @see com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType#getSourcedItem()
         */
    }

    /**
     * Create a VM from a VM Template
     *
     * @param vmTemplate the VM Template
     * @param vmName     the name of the new VM
     * @return a new VM configuration
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     * @see fr.xebia.vcloud.answer.VcloudAnswer#createVmFromTemplate(com.vmware.vcloud.sdk.VappTemplate, String) Soluce
     */
    @Override
    public SourcedCompositionItemParamType createVmFromTemplate(VappTemplate vmTemplate, String vmName) throws VCloudException {
        //TODO implement
        throw new RuntimeException("To be implemented");
        //Create a VM configuration and add Network to this VM

        /**
         * Create VM
         * @see com.vmware.vcloud.api.rest.schema.SourcedCompositionItemParamType#SourcedCompositionItemParamType()
         * @see com.vmware.vcloud.api.rest.schema.ReferenceType#ReferenceType()
         * @see ReferenceType#setHref(String)
         * @see com.vmware.vcloud.sdk.VappTemplate#getReference()
         * @see com.vmware.vcloud.api.rest.schema.ReferenceType#getHref()
         * @see ReferenceType#setName(String)
         * @see SourcedCompositionItemParamType#setSource(com.vmware.vcloud.api.rest.schema.ReferenceType)
         */

        /**
         * Add the vApp Network to the only Network VM interface :
         * Create Network assignment
         * @see com.vmware.vcloud.sdk.VappTemplate#getNetworkConnectionSection()
         * @see com.vmware.vcloud.api.rest.schema.NetworkConnectionSectionType#getNetworkConnection()
         * @see com.vmware.vcloud.api.rest.schema.NetworkAssignmentType#NetworkAssignmentType()
         * @see NetworkAssignmentType#setInnerNetwork(String)
         * @see NetworkAssignmentType#setContainerNetwork(String)
         *
         * Add the network to the VM
         * @see com.vmware.vcloud.api.rest.schema.SourcedCompositionItemParamType#getNetworkAssignment()
         */
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
        //TODO implement
        throw new RuntimeException("To be implemented");
        // Find and return a vApp template
        // Use Query service and Filter and Expression to find objects by name

        /**
         * Find vApp Template
         * @see com.vmware.vcloud.sdk.VcloudClient#getQueryService()
         * @see com.vmware.vcloud.sdk.QueryService#queryvAppTemplateIdRecords(com.vmware.vcloud.sdk.QueryParams)
         * @see com.vmware.vcloud.sdk.RecordResult#getRecords()
         */

        /**
         * Search inside VM vApp template for the one with <i>name</i>
         * @see VappTemplate#getVappTemplateById(com.vmware.vcloud.sdk.VcloudClient, String)
         */
    }

    /**
     * Create a Query params to find a Vapp Template by name
     *
     * @param vAppTemplateName The name of the vApp Template to find
     * @return a Query params that find <i>vAppTemplateName</i>
     */
    @Override
    public QueryParams<QueryVAppTemplateField> createVappQueryParams(String vAppTemplateName) {
        //TODO implement
        throw new RuntimeException("To be implemented");
        /**
         * @see QueryParams
         * @see QueryParams#setFilter(com.vmware.vcloud.sdk.Filter)
         * @see Filter#Filter(com.vmware.vcloud.sdk.Expression)
         * @see Expression#Expression(com.vmware.vcloud.sdk.constants.query.QueryField, String, com.vmware.vcloud.sdk.constants.query.ExpressionType)
         * @see QueryVAppTemplateField#NAME
         * @see ExpressionType#EQUALS
         */
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
        //TODO implement
        throw new RuntimeException("To be implemented");
        // Create a new VcloudClient, register sheme with the fake SSL Factory and login
        /**
         * @see vcloudClient
         * @see FakeSSLSocketFactory SSL connection with self-signed certificate
         * @see VcloudClient#registerScheme(String, Integer, org.apache.http.conn.ssl.SSLSocketFactory)
         * @see VcloudClient#login(String, String)
         */
    }
}
