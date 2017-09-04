#!/bin/bash
echo "Montando, deteniendo y destruyendo 50 contenedores"
START=$(date +%s.%N)
for i in {1..50}
do
	#docker run -d --rm -p 80:80 -p 3306:3306 --name=server$i xxdrackleroxx/test > /dev/null
done
END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)
echo "Tiempo montando: $DIFF"

echo "\nEsprando 5...4...3...2..1.."

START=$(date +%s.%N)
for i in {1..50}
do
	#docker stop -t 0 server$i > /dev/null
done
END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)
echo "Tiempo destruyendo: $DIFF"

for i in {1..50}
do
	PUERTO_PHP=$((i+2000))
	PUERTO_SQL=$((i+3000))
	echo "$i $PUERTO $PUERTO2"
done