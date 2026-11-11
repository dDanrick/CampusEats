package Analitica;

import Adaptadores.FuenteDeDatos;
import Fabrica.FabricaFuentes;
import Filtros.*;
import Modelo.LineaPedido;
import Modelo.Pago;
import Modelo.Plato;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade: única puerta de entrada al módulo. Solo dos métodos de trabajo:
 * cargar() (lee, limpia y guarda) y resumen(desde, hasta) (calcula las métricas).
 * El repositorio y las métricas llegan por constructor (inversión de dependencias).
 */
public class ServicioAnalitica {
    private static final Path CONFIG_POR_DEFECTO = Path.of("config", "fuentes.json");

    private final RepoAnalitica repositorio;
    private final List<Metrica> metricas;
    private final Path archivoConfig;

    public ServicioAnalitica(RepoAnalitica repositorio, List<Metrica> metricas) {
        this(repositorio, metricas, CONFIG_POR_DEFECTO);
    }

    public ServicioAnalitica(RepoAnalitica repositorio, List<Metrica> metricas, Path archivoConfig) {
        this.repositorio = repositorio;
        this.metricas = metricas;
        this.archivoConfig = archivoConfig;
    }

    public void cargar() {
        try {
            // 1. Leer: la fábrica crea un adaptador por cada fuente de config/fuentes.json
            List<LineaPedido> lineas = new ArrayList<>();
            List<Pago> pagos = new ArrayList<>();
            List<Plato> menu = new ArrayList<>();

            for (FuenteDeDatos<?> fuente : FabricaFuentes.crearTodas(archivoConfig)) {
                for (Object dato : fuente.leer()) {
                    if (dato instanceof LineaPedido l) lineas.add(l);
                    else if (dato instanceof Pago p) pagos.add(p);
                    else if (dato instanceof Plato pl) menu.add(pl);
                }
            }

            // 2. Limpiar: primero se descartan registros malos, al final se completa el nombre
            Pipeline pipeline = new Pipeline();
            pipeline.agregarFiltro(new Duplicados());
            pipeline.agregarFiltro(new Cantidad());
            pipeline.agregarFiltro(new FiltroPagado());
            pipeline.agregarFiltro(new FiltroPago(pagos));
            pipeline.agregarFiltro(new CompletarNombrePlato(menu));

            // 3. Guardar
            repositorio.guardarLineas(pipeline.ejecutar(lineas));
            repositorio.guardarPagos(pagos);

        } catch (Exception e) {
            throw new IllegalStateException("Error al cargar y limpiar los datos: " + e.getMessage(), e);
        }
    }

    public void resumen(LocalDate desde, LocalDate hasta) {
        List<LineaPedido> lineas = repositorio.lineasEntre(desde, hasta);
        List<Pago> pagos = repositorio.pagosEntre(desde, hasta);

        System.out.println();
        System.out.println("========================================================");
        System.out.println(" RESUMEN DE ANALITICA CAMPUSEATS (" + desde + " a " + hasta + ")");
        System.out.println("========================================================");
        for (Metrica m : metricas) {
            System.out.println("- " + m.nombre() + ":");
            System.out.println("  -> " + m.calcular(lineas, pagos));
            System.out.println("--------------------------------------------------------");
        }
    }
}
