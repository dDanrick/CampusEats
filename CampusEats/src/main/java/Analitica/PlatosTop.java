package Analitica;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Strategy: los 5 platos más vendidos (suma de cantidades, no de filas). */
public class PlatosTop implements Metrica {
    @Override
    public String nombre() {
        return "Platos mas vendidos";
    }

    @Override
    public String calcular(List<LineaPedido> lineas, List<Pago> pagos) {
        Map<String, Integer> unidades = lineas.stream().collect(
                Collectors.groupingBy(LineaPedido::nombrePlato, Collectors.summingInt(LineaPedido::cantidad)));

        List<Map.Entry<String, Integer>> top = unidades.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .toList();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < top.size(); i++) {
            if (i > 0) sb.append(" / ");
            sb.append(i + 1).append(". ").append(top.get(i).getKey())
              .append(", ").append(top.get(i).getValue()).append(" unidades");
        }
        return sb.toString();
    }
}
