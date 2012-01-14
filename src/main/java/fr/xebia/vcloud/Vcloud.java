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

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.vmware.vcloud.api.rest.schema.*;
import com.vmware.vcloud.api.rest.schema.ovf.MsgType;
import com.vmware.vcloud.api.rest.schema.ovf.SectionType;
import com.vmware.vcloud.sdk.*;
import com.vmware.vcloud.sdk.constants.FenceModeValuesType;
import com.vmware.vcloud.sdk.constants.Version;
import com.vmware.vcloud.sdk.constants.query.ExpressionType;
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import fr.xebia.vcloud.predicate.VmPredicate;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/**
 * Xebia vCloud SDK workshop<br/>
 * <p/>
 * Create a Petclinic vApp from template, configure and run it
 */
public class Vcloud {
    private static final Logger LOGGER = LoggerFactory.getLogger(Vcloud.class);

    /**
     * Name of the vApp templare
     */
    public static final String V_APP_TEMPLATE_XEBIA = "vApp - Xebia";

    /**
     * MySQL VM template name
     */
    public static final String VM_TEMPLATE_MYSQL = "vm-mysql";

    /**
     * Apache reverse proxy VM template name
     */
    public static final String VM_TEMPLATE_APACHE = "vm-apache-rp";

    /**
     * Tomcat VM template name
     */
    public static final String VM_TEMPLATE_TOMCAT = "vm-tomcat";

    /**
     * MySQL VM name
     */
    public static final String VM_MYSQL = "vm-mysql";

    /**
     * Apache reverse proxy VM name
     */
    public static final String VM_APACHE_RP = "vm-apache-rp";

    /**
     * First Tomcat VM VM name
     */
    public static final String VM_TOMCAT_1 = "vm-tomcat-1";

    /**
     * Second Tomcate VM name
     */
    public static final String VM_TOMCAT_2 = "vm-tomcat-2";

    /**
     * Root password for each VM
     */
    public static final String ADMIN_PASSWORD = "xebia2012";

    /**
     * The vCloud client
     */
    private VcloudClient vcloudClient;

    /**
     * The virtual Datacenter
     */
    private Vdc vdc;

    /**
     * The Org internal network
     */
    private ReferenceType orgNetworkRef;

    /**
     * Prepare for creating a new vApp
     */
    public Vcloud() {
        // Too verbose if not disabled
        VcloudClient.setLogLevel(Level.OFF);
    }

    /**
     * Login in vCloud Director
     *
     * @param vCloudURL vCloud URL. eg : https://62.23.45.217
     * @param username  user@organization
     * @param password  password
     * @throws Exception vCloud or SSL Exception
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#login(String, String, String) Soluce
     */
    public void login(String vCloudURL, String username, String password)
            throws Exception {
        throw new RuntimeException("To be implemented");
        // Create a new VcloudClient, register sheme with the fake SSL Factory and login
        /**
         * @see vcloudClient
         * @see FakeSSLSocketFactory SSL connection with self-signed certificate
         * @see VcloudClient#VcloudClient(String, com.vmware.vcloud.sdk.constants.Version)
         * @see VcloudClient#registerScheme(String, Integer, org.apache.http.conn.ssl.SSLSocketFactory)
         * @see VcloudClient#login(String, String)
         */
    }

    /**
     * Set Virtual Datacenter as class field.<br/>
     * VDC is used many times
     *
     * @throws VCloudException vCloud Error
     * @see fr.xebia.vcloud.answer.VcloudAnswer#initVdc() Soluce
     */
    private void initVdc() throws VCloudException {
        throw new RuntimeException("To be implemented");
        // Init VDC field with the only VDC in the only organisation
        /**
         * @see com.vmware.vcloud.sdk.VcloudClient#getOrgRefs()
         * @see Organization#getOrganizationByReference(com.vmware.vcloud.sdk.VcloudClient, com.vmware.vcloud.api.rest.schema.ReferenceType)
         * @see com.vmware.vcloud.sdk.Organization#getVdcRefs()
         * @see Vdc#getVdcByReference(com.vmware.vcloud.sdk.VcloudClient, com.vmware.vcloud.api.rest.schema.ReferenceType)
         */
    }

    /**
     * set Organisation network<bt/>
     * @see fr.xebia.vcloud.answer.VcloudAnswer#initVdc() Soluce
     */
    private void initOrgNetwork() {
        throw new RuntimeException("To be implemented");
        // Init the organisation network
        // In the Xebia org, the namùe is "Reseau interne"
        /**
         * @see Vdc#getAvailableNetworkRefByName(String) 
         */
    }

    /**
     * Find VM template in catalogue
     *
     * @param templateName The vApp Template name
     * @param name         The VM name inside the vApp Template
     * @return the vAppTemplate for this VM
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud error
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#findVmTemplateByName(String, String) Soluce
     */
    public VappTemplate findVmTemplateByName(String templateName, String name) throws VCloudException {
        throw new RuntimeException("To be implemented");
        // Find and return a vApp template
        // Use Query service and Filter and Expression to find objects by name

        /**
         * Find vApp Template 
         * @see com.vmware.vcloud.sdk.VcloudClient#getQueryService()
         * @see com.vmware.vcloud.sdk.QueryService#queryvAppTemplateIdRecords(com.vmware.vcloud.sdk.QueryParams)
         * @see QueryParams#setFilter(com.vmware.vcloud.sdk.Filter)
         * @see com.vmware.vcloud.sdk.RecordResult#getRecords() 
         * @see Filter#Filter(com.vmware.vcloud.sdk.Expression) 
         * @see Expression#Expression(com.vmware.vcloud.sdk.constants.query.QueryField, String, com.vmware.vcloud.sdk.constants.query.ExpressionType) 
         * @see QueryVAppTemplateField#NAME
         * @see ExpressionType#EQUALS 
         */

        /**
         * Search inside VM vApp template for the one with <i>name</i>
         * @see VappTemplate#getVappTemplateById(com.vmware.vcloud.sdk.VcloudClient, String)
         */        
    }

    /**
     * Compose a vApp with Org network as the only one network, no vApp network<br/>
     * No VM is added in this configuration at this time
     *
     * @param vAppName The name of the vApp
     * @return a new vApp configuration
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud Error
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#createComposeParams(String) Soluce
     */
    public ComposeVAppParamsType createComposeParams(String vAppName) throws VCloudException {
        throw new RuntimeException("To be implemented");
        // Create a network configuration with the Organisation network        
        // Create vApp configuration
        // Add the network configuration to the vApp

        /**
         * Network configuration : create a bridge configuration with the Org network
         * @see orgNetworkRef The ref of the organisation network
         * @see FenceModeValuesType#BRIDGED
         * @see com.vmware.vcloud.api.rest.schema.NetworkConfigurationType#NetworkConfigurationType() 
         * @see NetworkConfigurationType#setFenceMode(String)
         * @see NetworkConfigurationType#setParentNetwork(com.vmware.vcloud.api.rest.schema.ReferenceType)
         *
         * Vapp Network configuration 
         * @see com.vmware.vcloud.api.rest.schema.VAppNetworkConfigurationType#VAppNetworkConfigurationType() 
         * @see VAppNetworkConfigurationType#setConfiguration(com.vmware.vcloud.api.rest.schema.NetworkConfigurationType) 
         * @see VAppNetworkConfigurationType#setNetworkName(String)
         * @see com.vmware.vcloud.api.rest.schema.ReferencesType#getName()
         * 
         * Network config section
         * @see com.vmware.vcloud.api.rest.schema.NetworkConfigSectionType#NetworkConfigSectionType() 
         * @see com.vmware.vcloud.api.rest.schema.ovf.MsgType#MsgType() 
         * @see NetworkConfigSectionType#setInfo(com.vmware.vcloud.api.rest.schema.ovf.MsgType) 
         * @see com.vmware.vcloud.api.rest.schema.NetworkConfigSectionType#getNetworkConfig()
         */

        /**
         * Create Config param
         * @see com.vmware.vcloud.api.rest.schema.InstantiationParamsType#InstantiationParamsType()
         * Add Network to vApp
         * @see ObjectFactory#createNetworkConfigSection(com.vmware.vcloud.api.rest.schema.NetworkConfigSectionType) 
         * @see com.vmware.vcloud.api.rest.schema.InstantiationParamsType#getSection()
         */

        /**
         * Create vApp
         * @see com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType#ComposeVAppParamsType() 
         * @see ComposeVAppParamsType#setDeploy(Boolean) 
         * @see ComposeVAppParamsType#setInstantiationParams(com.vmware.vcloud.api.rest.schema.InstantiationParamsType) 
         * @see ComposeVAppParamsType#setName(String) 
         */

    }

    /**
     * Create a VM from a VM Template
     *
     * @param vmTemplate the VM Template
     * @param vmName     the name of the new VM
     * @return a new VM configuration
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud Error
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#createVmFromTemplate(com.vmware.vcloud.sdk.VappTemplate, String) Soluce
     */
    public SourcedCompositionItemParamType createVmFromTemplate(VappTemplate vmTemplate, String vmName) throws VCloudException {
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
     * Add a MySQL VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud Error
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#addVmMySQL(com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType) Soluce
     */
    public void addVmMySQL(ComposeVAppParamsType vAppParamsType) throws VCloudException {
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
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud Error
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#addVmApache(com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType) Soluce
     */
    public void addVmApache(ComposeVAppParamsType vAppParamsType) throws VCloudException {
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
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud Error
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#addTwoVmsTomcat(com.vmware.vcloud.api.rest.schema.ComposeVAppParamsType) Soluce
     */
    public void addTwoVmsTomcat(ComposeVAppParamsType vAppParamsType) throws VCloudException {
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
     * Start a VM and wait until online.<br/>
     * The VM is not yet available (eg : SSH) while this method return.
     *
     * @param vm the VM to start
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud Error
     * @throws java.util.concurrent.TimeoutException Timeout while waiting for the VM to start
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#startVM(com.vmware.vcloud.sdk.VM) Soluce
     */
    private static void startVM(VM vm) throws VCloudException, TimeoutException {
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

    /**
     * Configure each VM :
     * <ul>
     * <li>Set root password</li>
     * <li>Set bootstrap script</li>
     * <li>Start VMs</li>
     * </ul>
     * Warning : The Database server must be up and running while Tomcat server start. If not,
     * Petclinic app will be down.
     *
     * @param vapp the vApp
     * @throws com.vmware.vcloud.sdk.VCloudException
     *
     * @throws java.io.IOException
     * @throws java.util.concurrent.TimeoutException
     *
     * @see fr.xebia.vcloud.answer.VcloudAnswer#configureAndStartVMs(com.vmware.vcloud.sdk.Vapp) Soluce
     *
     */
    private void configureAndStartVMs(Vapp vapp) throws VCloudException, IOException, TimeoutException {
        // Refresh vApp
        vapp = Vapp.getVappByReference(vcloudClient, vapp.getReference());
        List<VM> vms = vapp.getChildrenVms();
        VM vmMySQL = Iterables.find(vms, new VmPredicate(VM_MYSQL));
        VM vmApache = Iterables.find(vms, new VmPredicate(VM_APACHE_RP));
        VM vmTomcat1 = Iterables.find(vms, new VmPredicate(VM_TOMCAT_1));
        VM vmTomcat2 = Iterables.find(vms, new VmPredicate(VM_TOMCAT_2));

        /**
         * Configure MySQL VM
         * @see com.vmware.vcloud.sdk.VM#getGuestCustomizationSection()
         * @see GuestCustomizationSectionType#setCustomizationScript(String)
         * @see loadScriptByVmName()
         * @see VM_MYSQL
         * @see GuestCustomizationSectionType#setAdminPasswordAuto(Boolean)
         * @see GuestCustomizationSectionType#setAdminPassword(String)
         * @see ADMIN_PASSWORD
         *
         * Apply
         * @see VM#updateSection(com.vmware.vcloud.api.rest.schema.ovf.SectionType)
         * @see Task#waitForTask(long)
         *
         * Start MySQL VM now : it must be up and running before Tomcat start !
         * @see startVM();
         */

        /**
         * Configure Tomcat VMs
         * Configure bootstrap script with MySQL IP.
         * Tomcat bootstrap script is ready to be formated with MySQL IP
         * @see loadScriptByVmName()
         * @see com.vmware.vcloud.sdk.VM#getNetworkConnections()
         * @see com.vmware.vcloud.api.rest.schema.NetworkConnectionType#getIpAddress()
         * @see String#format(String, Object...)
         *
         * repeat steps from MySQL VM
         */

        /**
         * Configure Apache VM
         * Apache bootstrap script is ready to be formated with the 2 Tomcat IPs
         * Repeat steps from other VMs
         */

        /**
         * Starts Tomcats and Apache VMs
         * @see startVM();
         */
    }

    /**
     * Create vApp, configure and run it
     *
     * @throws Exception vCloud Error
     */
    public void startVapp() throws Exception {

        // Ressources from properties
        ResourceBundle ressources = ResourceBundle.getBundle("vmware");

        String url = ressources.getString("vcloud.url");
        String username = ressources.getString("vloud.username");
        String password = ressources.getString("vloud.password");
        String vAppName = ressources.getString("vapp.name");

        if (Strings.isNullOrEmpty(vAppName)) {
            throw new IllegalStateException("You must set vApp name in vmware.properties");
        }

        // Login
        login(url, username, password);

        // Init Org objects
        initVdc();
        initOrgNetwork();

        // Create vApp configuration
        ComposeVAppParamsType composeParams = createComposeParams(vAppName);

        // Add VMs
        addVmApache(composeParams);
        addVmMySQL(composeParams);
        addTwoVmsTomcat(composeParams);

        // Create vApp
        Vapp vapp = vdc.composeVapp(composeParams);
        LOGGER.info("Composing vApp : " + vapp.getResource().getName());
        List<Task> tasks = vapp.getTasks();
        Iterables.getOnlyElement(tasks).waitForTask(0);

        // Configure VMs and run them
        configureAndStartVMs(vapp);
    }

    /**
     * Load the bootstrap script for a VM
     *
     * @param vmName the Name of the VM
     * @return The bootstrap script associated to this VM
     * @throws java.io.IOException Error while reading script
     */
    public static String loadScriptByVmName(String vmName) throws IOException {
        Map<String, String> mapScripts = Maps.newHashMap();
        mapScripts.put(VM_APACHE_RP, "bootstrap_rp.sh");
        mapScripts.put(VM_MYSQL, "bootstrap_mysql.sh");
        mapScripts.put(VM_TOMCAT_1, "bootstrap_tomcat.sh");
        mapScripts.put(VM_TOMCAT_2, "bootstrap_tomcat.sh");
        String scriptName = mapScripts.get(vmName);
        URL resource = Resources.getResource(scriptName);
        String script = Resources.toString(resource, Charset.defaultCharset());
        return script;
    }
}
