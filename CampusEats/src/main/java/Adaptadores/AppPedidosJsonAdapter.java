package Adaptadores;

import Modelo.LineaPedido;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Adapta pedidos_app.json (un objeto por pedido) a líneas de pedido. */
public class AppPedidosJsonAdapter implements FuenteDeDatos<LineaPedido> {
    private final Path rutaArchivo;

    public AppPedidosJsonAdapter(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public List<LineaPedido> leer() throws Exception {
        JsonNode raiz = new ObjectMapper().readTree(rutaArchivo.toFile());
        List<LineaPedido> lineas = new ArrayList<>();

        for (JsonNode pedido : raiz) {
            String idPedido = pedido.path("numeroPedido").asText();
            String estado = pedido.path("estado").asText();
            LocalDateTime fechaHora = fechaDelPedido(pedido);

            for (JsonNode item : pedido.path("lineas")) {
                lineas.add(new LineaPedido(
                        idPedido,
                        item.path("platoId").asInt(),
                        null, // el nombre lo aporta el menú más adelante
                        item.path("cantidad").asInt(),
                        item.path("precioUnitario").asDouble(),
                        fechaHora,
                        estado));
            }
        }
        return lineas;
    }

    /**
     * Momento de la venta: cuando el pedido pasó al estado PAGADO (CU-2.1).
     * Si no tiene ese cambio (p. ej. sigue pendiente de pago), se usa la fecha de registro;
     * esos pedidos se descartan después en el pipeline.
     */
    private LocalDateTime fechaDelPedido(JsonNode pedido) {
        String texto = "";
        for (JsonNode h : pedido.path("historialEstados")) {
            if ("PAGADO".equals(h.path("estado").asText())) {
                texto = h.path("fechaHora").asText();
                break;
            }
        }
        if (texto.isEmpty()) {
            texto = pedido.path("registradoEn").asText();
        }
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("El pedido "
                    + pedido.path("numeroPedido").asText() + " no tiene fecha");
        }
        return LocalDateTime.parse(texto.length() > 19 ? texto.substring(0, 19) : texto);
    }
}
