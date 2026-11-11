package Filtros;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltroPago implements Filtro {
    private final Map<String, Pago> pagosAprobados = new HashMap<>();
    private int descartados = 0;

    public FiltroPago(List<Pago> pagos) {
        // Solo guardamos los pagos APROBADA. Así, si un pedido tuvo un intento
        // rechazado y luego uno aprobado, nos quedamos con el aprobado.
        for (Pago p : pagos) {
            if ("APROBADA".equalsIgnoreCase(p.estado())) {
                pagosAprobados.putIfAbsent(p.idTransaccion(), p);
            }
        }
    }

    @Override
    public List<LineaPedido> filtrar(List<LineaPedido> lineas) {
        // Total de cada pedido en centavos = suma de cantidad * precio histórico
        Map<String, Long> totalesCentavos = new HashMap<>();
        for (LineaPedido l : lineas) {
            long subtotal = Math.round(l.cantidad() * l.precioHistorico() * 100);
            totalesCentavos.merge(l.idPedido(), subtotal, Long::sum);
        }

        List<LineaPedido> resultado = new ArrayList<>();
        descartados = 0;

        for (LineaPedido l : lineas) {
            Pago pago = pagosAprobados.get(l.idPedido());

            // Regla CU-2.1: pago APROBADO y monto cobrado igual al total del pedido
            boolean valido = pago != null
                    && Math.round(pago.monto() * 100) == totalesCentavos.get(l.idPedido());

            if (valido) {
                resultado.add(l);
            } else {
                descartados++;
            }
        }
        return resultado;
    }

    @Override
    public int getRegistrosDescartados() {
        return descartados;
    }

    @Override
    public String getNombre() {
        return "Pago no aprobado o monto distinto";
    }
}