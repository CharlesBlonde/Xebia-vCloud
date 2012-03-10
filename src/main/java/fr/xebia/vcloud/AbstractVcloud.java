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
import com.vmware.vcloud.sdk.constants.query.QueryVAppTemplateField;
import fr.xebia.vcloud.answer.VcloudAnswer;
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
 * User: charles
 * Date: 10/03/12
 * Time: 10:14
 *
 * @author charles.blonde@gmail.com
 */
public abstract class AbstractVcloud {
    protected static final Logger LOGGER = LoggerFactory.getLogger(VcloudAnswer.class);
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
     * The name of the internal network of the Virtual Datacenter
     */
    public static final String RESEAU_INTERNE = "Reseau interne";
    /**
     * The vCloud client
     */
    protected VcloudClient vcloudClient;

    /**
     * Prepare for creating a new vApp
     *
     * @param vcloudClient The vCloud Client
     */
    public AbstractVcloud(VcloudClient vcloudClient) {
        // Too verbose if not disabled
        VcloudClient.setLogLevel(Level.OFF);
        this.vcloudClient = vcloudClient;
    }

    /**
     * Login in vCloud Director
     *
     * @param username user@organization
     * @param password password
     * @throws Exception vCloud or SSL Exception
     */
    public abstract void login(String username, String password) throws Exception;

    /**
     * Find VM template in catalogue
     *
     * @param queryVappTemplate The query to find the template
     * @param name              The VM name inside the vApp Template
     * @return the vAppTemplate for this VM
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud error
     */
    public abstract VappTemplate findVmTemplateByName(QueryParams<QueryVAppTemplateField> queryVappTemplate, String name) throws VCloudException;

    /**
     * Create a Query params to find a Vapp Template by name
     *
     * @param vAppTemplateName The name of the vApp Template to find
     * @return a Query params that find <i>vAppTemplateName</i>
     */
    public abstract QueryParams<QueryVAppTemplateField> createVappQueryParams(String vAppTemplateName);

    /**
     * Add a MySQL VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    public abstract void addVmMySQL(ComposeVAppParamsType vAppParamsType) throws VCloudException;

    /**
     * Add an Apache Reverse Proxy VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    public abstract void addVmApache(ComposeVAppParamsType vAppParamsType) throws VCloudException;

    /**
     * Add 2 Tomcat VM to the vApp config
     *
     * @param vAppParamsType the vApp config
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    public abstract void addTwoVmsTomcat(ComposeVAppParamsType vAppParamsType) throws VCloudException;

    /**
     * Create a VM from a VM Template
     *
     * @param vmTemplate the VM Template
     * @param vmName     the name of the new VM
     * @return a new VM configuration
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    public abstract SourcedCompositionItemParamType createVmFromTemplate(VappTemplate vmTemplate, String vmName) throws VCloudException;

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
    public abstract void startVM(VM vm) throws VCloudException, TimeoutException;

    /**
     * Configure the MySQL VM :
     * <ul>
     * <li>Add boostrap script</li>
     * <li>Set Root password</li>
     * </ul>
     *
     * @param vm The VM to configure
     * @throws VCloudException  VCloud Error
     * @throws IOException      Error while reading bootstrap script
     * @throws TimeoutException Error while updating VM
     */
    public abstract void configureVmMySQL(VM vm) throws VCloudException, IOException, TimeoutException;

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
    public abstract void configureVmTomcat(VM vmTomcat, String mySQLIpAddress) throws VCloudException, IOException, TimeoutException;

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
    public abstract void configureVmApache(VM vmApache, String tomcatIp1, String tomcatIp2) throws VCloudException, IOException, TimeoutException;

    /**
     * Get Virtual Datacenter<br/>
     *
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    protected Vdc getVdc() throws VCloudException {
        Collection<ReferenceType> orgRefs = vcloudClient.getOrgRefs();
        ReferenceType orgRef = Iterables.getOnlyElement(orgRefs);
        Organization org = Organization.getOrganizationByReference(vcloudClient, orgRef);
        Collection<ReferenceType> vdcRefs = org.getVdcRefs();
        ReferenceType vdcRef = Iterables.getOnlyElement(vdcRefs);
        return Vdc.getVdcByReference(vcloudClient, vdcRef);
    }

    /**
     * Get Organisation network<bt/>
     *
     * @throws com.vmware.vcloud.sdk.VCloudException
     *          vCloud Error
     */
    protected ReferenceType getOrgNetwork() throws VCloudException {
        return getVdc().getAvailableNetworkRefByName(RESEAU_INTERNE);
    }

    /**
     * Create vApp, configure and run it
     *
     * @throws Exception vCloud Error
     */
    public void startVapp() throws Exception {
        ResourceBundle ressources = ResourceBundle.getBundle("vmware");

        String username = ressources.getString("vloud.username");
        String password = ressources.getString("vloud.password");
        String vAppName = ressources.getString("vapp.name");

        if (Strings.isNullOrEmpty(vAppName)) {
            throw new IllegalStateException("You must set vApp name in vmware.properties");
        }

        login(username, password);

        ComposeVAppParamsType composeParams = createComposeParams(vAppName);
        addVmApache(composeParams);
        addVmMySQL(composeParams);
        addTwoVmsTomcat(composeParams);

        Vapp vapp = getVdc().composeVapp(composeParams);

        LOGGER.info("Composing vApp : " + vapp.getResource().getName());
        List<Task> tasks = vapp.getTasks();

        Iterables.getOnlyElement(tasks).waitForTask(0);
        configureAndStartVMs(vapp);
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
    protected void configureAndStartVMs(Vapp vapp) throws VCloudException, IOException, TimeoutException {
        // Refresh vApp
        vapp = Vapp.getVappByReference(vcloudClient, vapp.getReference());
        List<VM> vms = vapp.getChildrenVms();
        VM vmMySQL = Iterables.find(vms, new VmPredicate(VM_MYSQL));
        VM vmApache = Iterables.find(vms, new VmPredicate(VM_APACHE_RP));
        VM vmTomcat1 = Iterables.find(vms, new VmPredicate(VM_TOMCAT_1));
        VM vmTomcat2 = Iterables.find(vms, new VmPredicate(VM_TOMCAT_2));

        configureVmMySQL(vmMySQL);
        // Start MySQL VM now to be sure database server it is up and running while
        // Tomcat VMs start. If not, Petclinic app will be down
        startVM(vmMySQL);

        // Get MySQL IP
        String ipMysql = Iterables.getOnlyElement(vmMySQL.getNetworkConnections()).getIpAddress();
        configureVmTomcat(vmTomcat1,ipMysql);
        configureVmTomcat(vmTomcat2,ipMysql);

        // Get Tomcat IPs
        String ipTomcat1 = Iterables.getOnlyElement(vmTomcat1.getNetworkConnections()).getIpAddress();
        String ipTomcat2 = Iterables.getOnlyElement(vmTomcat2.getNetworkConnections()).getIpAddress();
        configureVmApache(vmApache,ipTomcat1,ipTomcat2);

        // Start Tomcat and Apache VM
        startVM(vmTomcat1);
        startVM(vmTomcat2);
        startVM(vmApache);

        String ipApache = Iterables.getOnlyElement(vmApache.getNetworkConnections()).getIpAddress();
        LOGGER.info("Petclinic app available :  http://" + ipApache + "/xebia-petclinic");
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
        netConfiguration.setParentNetwork(getOrgNetwork());

        VAppNetworkConfigurationType vAppNetConfiguration = new VAppNetworkConfigurationType();
        vAppNetConfiguration.setConfiguration(netConfiguration);
        vAppNetConfiguration.setNetworkName(getOrgNetwork().getName());

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
