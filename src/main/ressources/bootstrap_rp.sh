#!/bin/bash
if [ x$1 = x"precustomization" ]
then
    echo "Precustomization - Nothing to do"
elif [ x$1 = x"postcustomization" ]
then
    echo "Starting Tomcat bootstrap script"
    export IP_TOMCAT_1="%s"
    export IP_TOMCAT_2="%s"
    echo "<IfModule mod_proxy_balancer.c>
         <Proxy balancer://tomcat-cluster>
            BalancerMember ajp://$IP_TOMCAT_1:8009 min=10 max=100 route=jvm1 loadfactor=1
            BalancerMember ajp://$IP_TOMCAT_2:8009 min=10 max=100 route=jvm1 loadfactor=1
            Order deny,allow
            Allow from all
         </Proxy>
         ProxyPass / balancer://tomcat-cluster/
    </IfModule>" > /etc/apache2/mods-enabled/proxy_balancer.conf
    /etc/init.d/apache2 restart
fi


