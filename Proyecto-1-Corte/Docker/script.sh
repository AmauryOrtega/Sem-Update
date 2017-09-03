#!/bin/bash
sed -i "s/.*bind-address.*/bind-address = 0.0.0.0/" /etc/mysql/my.cnf
mysqladmin shutdown
mysqld_safe &
sleep 5
echo "FLUSH privileges;" > sql.sql; echo "CREATE USER 'root'@'%';" >> sql.sql; echo "GRANT ALL PRIVILEGES ON * . * TO  'root'@'%' WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;" >> sql.sql
mysql < sql.sql
echo -ne "<Directory /usr/share/phpMyAdmin/>\n\torder allow,deny\n\tallow from all\n</Directory>\n" >> /etc/phpmyadmin/apache.conf
service apache2 restart