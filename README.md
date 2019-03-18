# Manual de uso del gps

## Intrucciones para probar implementación 0hh1

1. Ejecutar el main, encontrado en ohh1.App.java (levantará el programa en el puerto 8080).
2. Crear un archivo que contenga un tablero de NxN.

Ejemplo: board_4x4.txt conteniendo los siguientes caracteres.

```
0 0 0 0
0 0 1 0
0 0 1 1
1 0 0 1
```

3. Ejecutar el siguiente curl:

`
curl -X POST 'http://localhost:8080/resolve?size=4' -H 'cache-control: no-cache' -F 
'file=@{full_path_to_the_file}/board_4x4.txt'
`
