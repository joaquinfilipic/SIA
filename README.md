# Manual de uso del gps

## Intrucciones para levantar implementación 0hh1

1. Posicionarse en la carpeta base del proyecto `cd gps`
2. Empaquetar el proyecto en un ejecutable .jar, ejecutando `mvn clean package`
3. Levantar el servicio con el comando `java -jar target/gps-1.0.jar`

El servicio quedará levantado, escuchando en el puerto 8080.

## Instrucciones para llamar al servicio

Se debe crear un archivo .txt que siga los siguientes lineamientos:

1. La primer línea del archivo debe contener un número entero par N, que representa la dimensión del tablero.
2. Las siguientes N líneas corresponden al tablero a resolver. Los colores se representan con números enteros (0 para el BLANCO (vacío), 1 para el ROJO, 2 para el AZUL), que deben estar separados por espacios.

Ejemplo de archivo *board_4x4.txt* de dimensión 4x4:

```
4
0 0 0 0
0 0 1 0
0 0 1 1
1 0 0 1
```

3. Elegir una estrategia de búsqueda de entre las siguientes: DFS, BFS, IDDFS, ASTAR and GREEDY.
4. Ejecutar el siguiente curl:

Para las estrategias DFS, BFS y IDDFS (reemplazando *{path_to_the_file}* por la ruta relativa al archivo creado):

`
curl -X POST 'http://localhost:8080/resolve?strategy=dfs' -H 'cache-control: no-cache' -F 
'file=@{path_to_the_file}/board_4x4.txt'
`

Para las estrategias ASTAR y GREEDY, se puede elegir (opcionalmente) una herística. De no especificarse, se tomará la número 1.
Opciones de heurísticas: números enteros (1, 2).

Se debe reemplazar *{heuristic}* por el número de heurística elegido.

`
curl -X POST 'http://localhost:8080/resolve?strategy=dfs&heuristic={heuristic}' -H 'cache-control: no-cache' -F 
'file=@{path_to_the_file}/board_4x4.txt'
`


`
curl -X POST 'http://localhost:8080/resolve?strategy=dfs' -H 'cache-control: no-cache' -F 
'file=@{full_path_to_the_file}/board_4x4.txt'
`
