package Modelo;

import java.time.LocalDateTime;

/** Una línea es un plato dentro de un pedido. */
public record LineaPedido(
        String idPedido,
        int idPlato,
        String nombrePlato,      // se completa al final del pipeline con el menú
        int cantidad,
        double precioHistorico,  // precio con el que se hizo el pedido
        LocalDateTime fechaHora,
        String estadoPedido) {

    public LineaPedido conNombre(String nuevoNombre) {
        return new LineaPedido(idPedido, idPlato, nuevoNombre, cantidad,
                precioHistorico, fechaHora, estadoPedido);
    }
}
