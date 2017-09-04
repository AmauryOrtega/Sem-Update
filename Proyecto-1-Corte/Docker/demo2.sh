#!/bin/bash
N=$1
echo "Montando, deteniendo y destruyendo $N contenedores"
START=$(date +%s.%N)
for id in $(eval echo "{1..$N}")
do
	PUERTO_PHP=$((id+2000))
	PUERTO_SQL=$((id+3000))
	docker run -d --rm -p $PUERTO_PHP:80 -p $PUERTO_SQL:3306 --name=server$id xxdrackleroxx/test > /dev/null
done
END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)
echo "Tiempo montando: $DIFF segundos"

echo "Esperando 5...4...3...2...1..."

START=$(date +%s.%N)
for id in $(eval echo "{1..$N}")
do
	docker stop -t 0 server$id > /dev/null
done
END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)
echo "Tiempo destruyendo: $DIFF segundos"