# CampusEats – Módulo de analítica

Programa en Java que lee los datos de la cafetería, los limpia y calcula estadísticas.

## Estructura
```
config/fuentes.json     tipos y rutas de las fuentes (app_json, pasarela_csv, menu_csv)
datos/                  pedidos_app.json, pagos_pasarela.csv, menu_admin.csv
src/main/java/
  Modelo/               LineaPedido, Pago, Plato (records)
  Adaptadores/          FuenteDeDatos + 3 adaptadores           -> Adapter
  Fabrica/              FabricaFuentes, ConfigFuente             -> Factory
  Filtros/              Filtro, 5 filtros, Pipeline              -> Pipeline (Pipes & Filters)
  Analitica/            Metrica + métricas                       -> Strategy
                        RepoAnalitica(+EnMemoria)                -> Repository
                        ServicioAnalitica, Main                  -> Facade
Diagrama.puml           diagrama de clases UML
```

## Ejecutar
Desde la raíz del proyecto (requiere JDK 17+ y Maven):
```
mvn compile exec:java
```
En NetBeans/IntelliJ: ejecutar la clase `Analitica.Main` con la raíz del proyecto como directorio de trabajo.

## Resultado esperado
Limpieza: 2 repetidos, 1 cantidad cero, 35 no pagados, 2 pago no aprobado/monto distinto → 593 líneas de 388 pedidos.
Hora pico 13:00 (71 pedidos), luego 10:00 (63) y 12:00 (59). Ticket promedio 4,94 USD. Pagos rechazados: 39 de 443 (8,8 %).

## Para agregar una fuente nueva
Crear el adaptador que implemente `FuenteDeDatos<T>` y registrarlo con `FabricaFuentes.registrar("tipo", Adaptador::new)`.
