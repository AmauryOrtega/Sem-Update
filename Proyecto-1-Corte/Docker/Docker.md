## Apache
Se cambia /etc/phpmyadmin/apache.conf añadiendo `allow from all`.
```
<Directory /usr/share/phpMyAdmin/>
   	order allow,deny
   	allow from all
</Directory>
```
Se reinica el servicio con `service apache2 restart`.

## Mysql
Se cambia `/etc/mysql/my.cnf` modificando `bind-address = 127.0.0.1` a `bind-address = *` usando:
```
sed -i "s/.*bind-address.*/bind-address = 0.0.0.0/" /etc/mysql/my.cnf
```
Se reinicia el servicio asi:
```
mysqladmin shutdown
mysqld_safe
```
Se cambian los permisos del usuario `root@'%'` con el siguiente script:
```
echo "FLUSH privileges;" > sql.sql; echo "CREATE USER 'root'@'%';" >> sql.sql; echo "GRANT ALL PRIVILEGES ON * . * TO  'root'@'%' WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;" >> sql.sql
mysqld < sql.sql
```

## Resumido
Una mejor opcion es usar este script dentro del servidor. 
```
wget https://raw.githubusercontent.com/AmauryOrtega/Sem-Update/master/Proyecto-1-Corte/Docker/script.sh && chmod +x script.sh && ./script.sh && rm script.sh
```

## Comandos docker
```
docker run -d --rm -p 49161:80 -p 49162:3306 --name=server xxdrackleroxx/test
docker stop -t 0 server
```

## Comandos docker para demo
```
docker run -d --rm -p 80:80 -p 3306:3306 --name=server xxdrackleroxx/test
docker stop -t 0 server
```
