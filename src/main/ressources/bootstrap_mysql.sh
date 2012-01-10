#!/bin/bash
mysql -u root --password=xebia -e "CREATE DATABASE petclinic;"
mysql -u root --password=xebia -e "GRANT ALL PRIVILEGES ON *.* TO 'xebia'@'%' IDENTIFIED BY 'xebia' WITH GRANT OPTION;"

