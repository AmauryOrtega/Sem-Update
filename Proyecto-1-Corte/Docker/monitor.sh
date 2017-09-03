#!/bin/bash
echo "##### Contenedores #####"
watch -n 1 docker ps --format "'"'[{{.Image}}] "{{.Names}}" {{.Status}} Ports[{{.Ports}}] Size: {{.Size}}'"'"
#ps --format "[{{.Image}}] {{.ID}}: {{.Status}} Ports[{{.Ports}}] Size: {{.Size}} NAME:{{.Names}}"