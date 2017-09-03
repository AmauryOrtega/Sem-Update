#!/bin/bash
echo "Montando, deteniendo y destruyendo contenedor..."
time sh -c "docker run -d --rm -p 80:80 -p 3306:3306 --name=server xxdrackleroxx/test > /dev/null; docker stop -t 0 server > /dev/null"

echo "Peso de imagen: "; docker images | grep xxdrackleroxx/test | tail -c 6;