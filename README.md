# CampusEats – Módulo de analítica

> Taller de **Principios y Patrones de Diseño** · ISID432 Desarrollo y Mantenimiento de Software · Escuela Politécnica Nacional (2026B)

**Equipo:** Daniel Hidalgo, Joselyn Pauta, Génesis Tufiño y Valeria Zapata

## ¿De qué se trata?
CampusEats es una app con la que los estudiantes piden comida, pagan con tarjeta y retiran su pedido en la cafetería con un código. Todo queda registrado, y ahora el administrador quiere usar esos datos para decidir. Este módulo, hecho en **Java**, lee los datos, los limpia y responde tres preguntas:

| Pregunta del administrador | Métrica |
|---|---|
| ¿Qué platos se venden más? | `PlatosTop` |
| ¿A qué hora hay más pedidos? | `PedidosPorHora` |
| ¿Cuánto vale en promedio un pedido? (y cuántos pagos se rechazan) | `TicketPromedio`, `PagosRechazados` |

> En este taller **no se califica el análisis de datos, sino cómo está organizado el código**: cada problema de diseño se resuelve con un patrón.

## Patrones aplicados

| Patrón | Problema que resuelve | Clases principales | Principio |
|---|---|---|---|
| **Adapter** | Tres archivos con formatos distintos (JSON, CSV con `;` y CSV con `,`) | `FuenteDeDatos`, `AppPedidosJsonAdapter`, `PasarelaCsvAdapter`, `MenuCsvAdapter` | SRP, OCP, programar a una interfaz |
| **Factory** | Elegir el adaptador según `config/fuentes.json` sin llenar el código de `if` | `FabricaFuentes`, `ConfigFuente` | OCP |
| **Pipeline (Pipes & Filters)** | Los datos traen errores y la limpieza no debe ser un método gigante | `Filtro`, `Duplicados`, `Cantidad`, `FiltroPagado`, `FiltroPago`, `CompletarNombrePlato`, `Pipeline` | SRP, OCP, DIP |
| **Strategy** | Cada usuario pide estadísticas distintas y siempre aparecen nuevas | `Metrica`, `PlatosTop`, `PedidosPorHora`, `TicketPromedio`, `PagosRechazados` | OCP |
| **Repository** | Las métricas no deben depender de dónde se guardan los datos | `RepoAnalitica`, `RepoAnaliticaEnMemoria` | DIP |
| **Facade** | Una sola puerta de entrada al módulo | `ServicioAnalitica`, `Main` | DIP, SRP |

## Cómo fluyen los datos
```
Main → ServicioAnalitica.cargar()
         ├─ FabricaFuentes (config/fuentes.json) → adaptadores → LineaPedido, Pago, Plato
         ├─ Pipeline: Duplicados → Cantidad → FiltroPagado → FiltroPago → CompletarNombrePlato
         └─ RepoAnalitica (en memoria)
     ServicioAnalitica.resumen(desde, hasta) → cada Metrica calcula su resultado
```

## Resultados con los datos del taller (19 al 30 de octubre)
- Limpieza: 2 repetidos, 1 cantidad cero, 35 no pagados, 2 con pago no aprobado o monto distinto → **593 líneas de 388 pedidos**.
- Más vendidos: Almuerzo ejecutivo (138), Jugo natural (116), Café pasado (114), Seco de pollo (93), Batido de mora (73).
- Hora pico: **13:00 con 71 pedidos** (luego 10:00 con 63 y 12:00 con 59).
- Ticket promedio: **4,94 dólares**. Pagos rechazados: **39 de 443 (8,8 %)**.

## Contenido del repositorio
| Ruta | Qué es |
|---|---|
| `src/main/java/` | Código fuente, un paquete por patrón |
| `datos/` | `pedidos_app.json`, `pagos_pasarela.csv`, `menu_admin.csv` |
| `config/fuentes.json` | Tipo y ruta de cada fuente de datos |
| `Diagrama.puml` | Diagrama de clases UML con una nota por patrón |
| `pom.xml` | Maven (Java 17+, Jackson 2.15.2) |
| `README.md` | **Guía técnica:** estructura detallada y cómo ejecutar el programa |

## Cómo ejecutarlo (resumen)
Desde la raíz del proyecto: `mvn compile exec:java` (o ejecutar `Analitica.Main` en el IDE). Los detalles están en [`README.md`](README.md).
