#!/bin/bash
if [ x$1 = x"precustomization" ]
then
    echo "Precustomization - Nothing to do"
elif [ x$1 = x"postcustomization" ]
then
    echo "Starting MySQL bootstrap script"
    /etc/init.d/mysql restart
    mysql -u root --password=xebia -e "CREATE DATABASE petclinic;"
    mysql -u root --password=xebia -e "GRANT ALL PRIVILEGES ON *.* TO 'xebia'@'%' IDENTIFIED BY 'xebia' WITH GRANT OPTION;"
fi

