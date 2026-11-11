package Fabrica;

import Adaptadores.AppPedidosJsonAdapter;
import Adaptadores.FuenteDeDatos;
import Adaptadores.MenuCsvAdapter;
import Adaptadores.PasarelaCsvAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Factory: crea el adaptador correcto según el campo "tipo" de config/fuentes.json,
 * sin cadenas de if. Para soportar un tipo nuevo basta con registrarlo.
 */
public class FabricaFuentes {
    private static final Map<String, Function<Path, FuenteDeDatos<?>>> REGISTRO = new HashMap<>();

    static {
        registrar("app_json", AppPedidosJsonAdapter::new);
        registrar("pasarela_csv", PasarelaCsvAdapter::new);
        registrar("menu_csv", MenuCsvAdapter::new);
    }

    private FabricaFuentes() {}

    public static void registrar(String tipo, Function<Path, FuenteDeDatos<?>> creador) {
        REGISTRO.put(tipo, creador);
    }

    /** Crea el adaptador de una entrada de configuración. */
    public static FuenteDeDatos<?> crear(ConfigFuente config) {
        Function<Path, FuenteDeDatos<?>> creador = REGISTRO.get(config.tipo());
        if (creador == null) {
            throw new IllegalArgumentException("Tipo de fuente no soportado en config: " + config.tipo());
        }
        return creador.apply(Path.of(config.ruta()));
    }

    /**
     * Lee config/fuentes.json y crea un adaptador por cada fuente. Las rutas del JSON
     * (p. ej. "datos/pedidos_app.json") se resuelven desde la raíz del proyecto,
     * que es la carpeta que contiene a "config".
     */
    public static List<FuenteDeDatos<?>> crearTodas(Path archivoConfig) throws IOException {
        Path raiz = archivoConfig.toAbsolutePath().getParent().getParent();
        JsonNode nodo = new ObjectMapper().readTree(archivoConfig.toFile());

        List<FuenteDeDatos<?>> fuentes = new ArrayList<>();
        for (JsonNode f : nodo.path("fuentes")) {
            String ruta = raiz.resolve(f.path("ruta").asText()).toString();
            fuentes.add(crear(new ConfigFuente(f.path("tipo").asText(), ruta)));
        }
        return fuentes;
    }
}
