package Analitica;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Strategy: pedidos (no líneas) por hora del día; informa la hora pico y las dos siguientes. */
public class PedidosPorHora implements Metrica {
    @Override
    public String nombre() {
        return "Pedidos por hora";
    }

    @Override
    public String calcular(List<LineaPedido> lineas, List<Pago> pagos) {
        // Un pedido con varios platos tiene varias líneas: se cuenta una sola vez.
        Map<Integer, Set<String>> pedidosPorHora = new TreeMap<>();
        for (LineaPedido l : lineas) {
            pedidosPorHora.computeIfAbsent(l.fechaHora().getHour(), h -> new HashSet<>()).add(l.idPedido());
        }
        if (pedidosPorHora.isEmpty()) {
            return "Sin pedidos en el rango indicado.";
        }

        List<Map.Entry<Integer, Set<String>>> orden = pedidosPorHora.entrySet().stream()
                .sorted((a, b) -> b.getValue().size() - a.getValue().size())
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("La hora pico es las %02d:00, con %d pedidos.",
                orden.get(0).getKey(), orden.get(0).getValue().size()));
        if (orden.size() > 1) {
            sb.append(" Le siguen");
            for (int i = 1; i < Math.min(3, orden.size()); i++) {
                sb.append(i == 1 ? " " : " y ")
                  .append(String.format("las %02d:00 (%d)", orden.get(i).getKey(), orden.get(i).getValue().size()));
            }
            sb.append(".");
        }
        return sb.toString();
    }
}
