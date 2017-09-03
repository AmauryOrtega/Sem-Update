
[Diagramas de secuencia](https://bramp.github.io/js-sequence-diagrams/)

## Pidiendo servidor
![./Imagenes/Pidiendo-APP-UML.svg](./Imagenes/Pidiendo-APP-UML.svg)
```
Cliente.Socket->Servidor.ServidorThread: Dame server
Note right of Servidor.ServidorThread: Lo añade a la BD\nY le asigna un ID,\npuerto SQL y HTTP
Servidor.ServidorThread->Servidor.ServidorThread: docker run...+Puertos +ID
Servidor.ServidorThread->Cliente.Socket: PuertoPHP?PuertoSQL?ID
Cliente.Socket->Cliente.Socket: Muestra
```

## Deteniendo servidor
![./Imagenes/Deteniendo-APP-UML.svg](./Imagenes/Deteniendo-APP-UML.svg)
```
Cliente.Socket->Servidor.ServidorThread: Mata serverID
Note right of Servidor.ServidorThread: Lo elimina de la BD
Servidor.ServidorThread->Servidor.ServidorThread: docker stop serverID
```
