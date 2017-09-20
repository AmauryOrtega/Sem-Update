#!/bin/bash
echo "##### Contenedores #####"
watch -n 1 docker ps --format "'"'[{{.Names}}]: Ports[{{.Ports}}] Size: {{.Size}} {{.Status}}'"'"
#watch -n 1 docker ps --format "'"'[{{.Names}}]: {{.Status}} Ports[{{.Ports}}] Size: {{.Size}}'"'"
#ps --format "[{{.Image}}] {{.ID}}: {{.Status}} Ports[{{.Ports}}] Size: {{.Size}} NAME:{{.Names}}"
