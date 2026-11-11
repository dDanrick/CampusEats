package Analitica;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/** Strategy (métrica propia del equipo): valor medio de un pedido, con el precio histórico. */
public class TicketPromedio implements Metrica {
    @Override
    public String nombre() {
        return "Ticket promedio";
    }

    @Override
    public String calcular(List<LineaPedido> lineas, List<Pago> pagos) {
        Map<String, Double> totalPorPedido = lineas.stream().collect(Collectors.groupingBy(
                LineaPedido::idPedido,
                Collectors.summingDouble(l -> l.cantidad() * l.precioHistorico())));

        double promedio = totalPorPedido.values().stream()
                .mapToDouble(Double::doubleValue).average().orElse(0.0);

        return String.format(Locale.ROOT, "%.2f dolares por pedido", promedio).replace('.', ',');
    }
}
