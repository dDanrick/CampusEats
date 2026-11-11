package Analitica;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.util.List;
import java.util.Locale;

/** Strategy (métrica adicional): porcentaje de transacciones RECHAZADAS por la pasarela. */
public class PagosRechazados implements Metrica {
    @Override
    public String nombre() {
        return "Pagos rechazados";
    }

    @Override
    public String calcular(List<LineaPedido> lineas, List<Pago> pagos) {
        if (pagos.isEmpty()) {
            return "Sin transacciones en el rango indicado.";
        }
        long rechazados = pagos.stream().filter(p -> "RECHAZADA".equalsIgnoreCase(p.estado())).count();
        double porcentaje = 100.0 * rechazados / pagos.size();
        return String.format(Locale.ROOT, "%d de %d transacciones (%.1f %%)", rechazados, pagos.size(), porcentaje)
                .replace('.', ',');
    }
}
