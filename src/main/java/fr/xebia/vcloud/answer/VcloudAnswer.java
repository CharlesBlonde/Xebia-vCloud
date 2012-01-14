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
import fr.xebia.vcloud.FakeSSLSocketFactory;
import fr.xebia.vcloud.predicate.VmPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/**
 * Xebia vCloud SDK workshop<br/>
 * <p/>
 * Create a Petclinic vApp from template, configure and run it
 */
public class VcloudAnswer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VcloudAnswer.class);

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
    public VcloudAnswer() {
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
     */
    public void login(String vCloudURL, String username, String password)
            throws Exception {
        vcloudClient = new VcloudClient(vCloudURL, Version.V1_5);
        vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory.getInstance());
        vcloudClient.login(username, password);
    }

    /**
     * Set Virtual Datacenter as class field.<br/>
     * VDC is used many times
     *
     * @throws com.vmware.vcloud.sdk.VCloudException vCloud Error
     */
    private void initVdc() throws VCloudException {
        Collection<ReferenceType> orgRefs = vcloudClient.getOrgRefs();
        ReferenceType orgRef = Iterables.getOnlyElement(orgRefs);
        Organization org = Organization.getOrganizationByReference(vcloudClient, orgRef);
        Collection<ReferenceType> vdcRefs = org.getVdcRefs();
        ReferenceType vdcRef = Iterables.getOnlyElement(vdcRefs);
        vdc = Vdc.getVdcByReference(vcloudClient, vdcRef);
    }

    /**
     * set Organisation network<bt/>
     */
    private void initOrgNetwork() {
        orgNetworkRef = vdc.getAvailableNetworkRefByName("Reseau interne");
    }

    /**
     * Find VM template in catalogue
     *
     * @param templateName The vApp Template name
     * @param name         The VM name inside the vApp Template
     * @return the vAppTemplate for this VM
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud error
     */
    public VappTemplate findVmTemplateByName(String templateName, String name) throws VCloudException {
        QueryService queryService = vcloudClient.getQueryService();
        Expression expression = new Expression(QueryVAppTemplateField.NAME, templateName,
                ExpressionType.EQUALS);
        Filter filter = new Filter(expression);
        QueryParams<QueryVAppTemplateField> queryParams = new QueryParams<QueryVAppTemplateField>();
        queryParams.setFilter(filter);
        RecordResult<QueryResultVAppTemplateRecordType> result = queryService.queryvAppTemplateIdRecords(queryParams);
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
     * Compose a vApp with Org network as the only one network, no vApp network<br/>
     * No VM is added in this configuration at this time
     *
     * @param vAppName The name of the vApp
     * @return a new vApp configuration
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    public ComposeVAppParamsType createComposeParams(String vAppName) throws VCloudException {
        //retreive Org Network
        //ReferenceType networkRefByName = getOrgNetworkRef(getVdc());

        // Configure network
        NetworkConfigurationType netConfiguration = new NetworkConfigurationType();
        netConfiguration.setFenceMode(FenceModeValuesType.BRIDGED.value());
        netConfiguration.setParentNetwork(orgNetworkRef);

        VAppNetworkConfigurationType vAppNetConfiguration = new VAppNetworkConfigurationType();
        vAppNetConfiguration.setConfiguration(netConfiguration);
        vAppNetConfiguration.setNetworkName(orgNetworkRef.getName());

        NetworkConfigSectionType networkConfigSectionType = new NetworkConfigSectionType();
        MsgType networkInfo = new MsgType();
        networkConfigSectionType.setInfo(networkInfo);
        List<VAppNetworkConfigurationType> vAppNetworkConfigs = networkConfigSectionType.getNetworkConfig();
        vAppNetworkConfigs.add(vAppNetConfiguration);

        // Create vApp config
        InstantiationParamsType vappOrvAppTemplateInstantiationParamsType = new InstantiationParamsType();
        List<JAXBElement<? extends SectionType>> vappSections = vappOrvAppTemplateInstantiationParamsType.getSection();
        // Add Network to vApp config
        vappSections.add(new ObjectFactory().createNetworkConfigSection(networkConfigSectionType));

        ComposeVAppParamsType composeVAppParamsType = new ComposeVAppParamsType();
        composeVAppParamsType.setDeploy(false);
        composeVAppParamsType.setInstantiationParams(vappOrvAppTemplateInstantiationParamsType);
        composeVAppParamsType.setName(vAppName);

        return composeVAppParamsType;
    }

    /**
     * Add a MySQL VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    public void addVmMySQL(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        VappTemplate vmTemplateMysql = findVmTemplateByName(V_APP_TEMPLATE_XEBIA, VM_TEMPLATE_MYSQL);
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
    public void addVmApache(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        VappTemplate vmTemplateMysql = findVmTemplateByName(V_APP_TEMPLATE_XEBIA, VM_TEMPLATE_APACHE);
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
    public void addTwoVmsTomcat(ComposeVAppParamsType vAppParamsType) throws VCloudException {
        VappTemplate vmTemplateMysql = findVmTemplateByName(V_APP_TEMPLATE_XEBIA, VM_TEMPLATE_TOMCAT);
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
    public SourcedCompositionItemParamType createVmFromTemplate(VappTemplate vmTemplate, String vmName) throws VCloudException {
        //Create a VM configuration
        SourcedCompositionItemParamType vAppTemplateItem = new SourcedCompositionItemParamType();
        ReferenceType vappTemplateVMRef = new ReferenceType();
        vappTemplateVMRef.setHref(vmTemplate.getReference().getHref());
        vappTemplateVMRef.setName(vmName);
        vAppTemplateItem.setSource(vappTemplateVMRef);

        // Add the Org internal network
        if (vmTemplate.getNetworkConnectionSection().getNetworkConnection().size() > 0) {
            for (NetworkConnectionType networkConnection : vmTemplate
                    .getNetworkConnectionSection().getNetworkConnection()) {
                if (networkConnection.getNetworkConnectionIndex() == vmTemplate
                        .getNetworkConnectionSection()
                        .getPrimaryNetworkConnectionIndex()) {
                    NetworkAssignmentType networkAssignment = new NetworkAssignmentType();
                    networkAssignment.setInnerNetwork(networkConnection
                            .getNetwork());
                    networkAssignment.setContainerNetwork(orgNetworkRef.getName());
                    List<NetworkAssignmentType> networkAssignments = vAppTemplateItem
                            .getNetworkAssignment();
                    networkAssignments.add(networkAssignment);
                }
            }
        }

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
    private static void startVM(VM vm) throws VCloudException, TimeoutException {
        LOGGER.info("Starting VM : " + vm.getReference().getName());
        vm.powerOn().waitForTask(0);
        String ipAddress = Iterables.getOnlyElement(vm.getNetworkConnections()).getIpAddress();
        LOGGER.info("VM " + vm.getReference().getName() + " started (ip : " + ipAddress + ")");
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
     */
    private void configureAndStartVMs(Vapp vapp) throws VCloudException, IOException, TimeoutException {
        // Refresh vApp
        vapp = Vapp.getVappByReference(vcloudClient, vapp.getReference());
        List<VM> vms = vapp.getChildrenVms();
        VM vmMySQL = Iterables.find(vms, new VmPredicate(VM_MYSQL));
        VM vmApache = Iterables.find(vms, new VmPredicate(VM_APACHE_RP));
        VM vmTomcat1 = Iterables.find(vms, new VmPredicate(VM_TOMCAT_1));
        VM vmTomcat2 = Iterables.find(vms, new VmPredicate(VM_TOMCAT_2));

        // Configure MySQL VM
        GuestCustomizationSectionType configMySQL = vmMySQL.getGuestCustomizationSection();
        // Set bootstrap script
        configMySQL.setCustomizationScript(loadScriptByVmName(VM_MYSQL));
        // Set root password
        configMySQL.setAdminPasswordAuto(false);
        configMySQL.setAdminPassword(ADMIN_PASSWORD);
        LOGGER.info("Updating VM MySQL");
        vmMySQL.updateSection(configMySQL).waitForTask(0);
        // Start MySQL VM now to be sure database server it is up and running while
        // Tomcat VMs start. If not, Petclinic app will be down
        startVM(vmMySQL);

        // Get MySQL IP
        String ipMysql = Iterables.getOnlyElement(vmMySQL.getNetworkConnections()).getIpAddress();
        GuestCustomizationSectionType configTomcat1 = vmTomcat1.getGuestCustomizationSection();
        // Get boostrap script
        String scriptTomcat = loadScriptByVmName(VM_TOMCAT_1);
        // set MySQL IP address in bootstrap script and configure VM
        configTomcat1.setCustomizationScript(String.format(scriptTomcat, ipMysql));
        configTomcat1.setAdminPasswordAuto(false);
        configTomcat1.setAdminPassword(ADMIN_PASSWORD);
        LOGGER.info("Updating VM Tomcat 1");
        vmTomcat1.updateSection(configTomcat1).waitForTask(0);

        GuestCustomizationSectionType configTomcat2 = vmTomcat1.getGuestCustomizationSection();
        configTomcat2.setCustomizationScript(String.format(scriptTomcat, ipMysql));
        configTomcat2.setAdminPasswordAuto(false);
        configTomcat2.setAdminPassword(ADMIN_PASSWORD);
        LOGGER.info("Updating VM Tomcat 2");
        vmTomcat2.updateSection(configTomcat2).waitForTask(0);

        // Get Tomcat IPs
        String ipTomcat1 = Iterables.getOnlyElement(vmTomcat1.getNetworkConnections()).getIpAddress();
        String ipTomcat2 = Iterables.getOnlyElement(vmTomcat2.getNetworkConnections()).getIpAddress();
        GuestCustomizationSectionType configApache = vmTomcat1.getGuestCustomizationSection();
        String scriptApache = loadScriptByVmName(VM_APACHE_RP);
        configApache.setCustomizationScript(String.format(scriptApache, ipTomcat1, ipTomcat2));
        configApache.setAdminPasswordAuto(false);
        configApache.setAdminPassword(ADMIN_PASSWORD);
        LOGGER.info("Updating VM Apache");
        vmApache.updateSection(configApache).waitForTask(0);

        // Start Tomcat and Apache VM
        startVM(vmTomcat1);
        startVM(vmTomcat2);
        startVM(vmApache);

        String ipApache = Iterables.getOnlyElement(vmApache.getNetworkConnections()).getIpAddress();
        LOGGER.info("Petclinic app available :  http://" + ipApache + "/xebia-petclinic");
    }

    /**
     * Create vApp, configure and run it
     *
     * @throws Exception vCloud Error
     */
    public void startVapp() throws Exception {
        ResourceBundle ressources = ResourceBundle.getBundle("vmware");

        String url = ressources.getString("vcloud.url");
        String username = ressources.getString("vloud.username");
        String password = ressources.getString("vloud.password");
        String vAppName = ressources.getString("vapp.name");

        if (Strings.isNullOrEmpty(vAppName)) {
            throw new IllegalStateException("You must set vApp name in vmware.properties");
        }

        login(url, username, password);
        initVdc();
        initOrgNetwork();

        ComposeVAppParamsType composeParams = createComposeParams(vAppName);
        addVmApache(composeParams);
        addVmMySQL(composeParams);
        addTwoVmsTomcat(composeParams);

        Vapp vapp = vdc.composeVapp(composeParams);

        LOGGER.info("Composing vApp : " + vapp.getResource().getName());
        List<Task> tasks = vapp.getTasks();

        Iterables.getOnlyElement(tasks).waitForTask(0);
        configureAndStartVMs(vapp);
    }
}
