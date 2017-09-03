# Apache
Change /etc/phpmyadmin/apache.conf.
```
<Directory /usr/share/phpMyAdmin/>
   	order allow,deny
   	allow from all
</Directory>
```
Use `service apache2 restart`.

# Mysql
Default options are read from the following files in the given order: `/etc/my.cnf`, `/etc/mysql/my.cnf`, `/usr/etc/my.cnf` and `~/.my.cnf`

Change bind-address from 127.0.0.1 to * in `/etc/mysql/my.cnf`
```
sed -i "s/.*bind-address.*/bind-address = 0.0.0.0/" /etc/mysql/my.cnf
```

Shutdown mysqld with `mysqladmin shutdown` and turn it back on with `mysqld_safe`

Change user permissions
```
	echo "FLUSH privileges;" > sql.sql; echo "CREATE USER 'root'@'%';" >> sql.sql; echo "GRANT ALL PRIVILEGES ON * . * TO  'root'@'%' WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;" >> sql.sql
```

`mysqld < sql.sql`

```
wget https://gist.githubusercontent.com/AmauryOrtega/88ed9233d6b826547ff537e89fc79ec7/raw/7492569413e5b02d5a91b8e79a192015b4be4366/script.sh && chmod +x script.sh && ./script.sh && rm script.sh
```

# Debuging
`docker run -d --rm -p 49161:80 -p 49162:3306 --name=server wnameless/mysql-phpmyadmin`
`docker exec -it server bash`
`docker stop -t 0 server`

# Time
`time sh -c "docker run -d --rm -p 49161:80 -p 49162:3306 --name=server wnameless/mysql-phpmyadmin;docker stop -t 0 server"`

# Final
```
docker run -d --rm -p 49161:80 -p 49162:3306 --name=server xxdrackleroxx/test
docker stop -t 0 server
```
# Final for server
```
docker run -d --rm -p 80:80 -p 3306:3306 --name=server xxdrackleroxx/test
docker stop -t 0 server
```