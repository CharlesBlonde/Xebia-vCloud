#!/bin/bash
IPMYSQL="%s"

echo "# PETCLINIC ENVIRONMENT VARIABLES
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://$IPMYSQL:3306/petclinic
jdbc.username=xebia
jdbc.password=xebia

# Properties that control the population of schema and data for a new data source
jdbc.initLocation=classpath:db/mysql/initDB.txt
jdbc.dataLocation=classpath:db/mysql/populateDB.txt

# Property that determines which Hibernate dialect to use
# (only applied with "applicationContext-hibernate.xml")
hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Property that determines which database to use with an AbstractJpaVendorAdapter
jpa.database=MYSQL" >> /opt/tomcat/conf/catalina.properties

/etc/init.d/tomcat stop

su tomcat -c "wget http://192.168.70.101/xebia-petclinic.war -O /opt/tomcat/webapps/xebia-petclinic.war"

/etc/init.d/tomcat start

