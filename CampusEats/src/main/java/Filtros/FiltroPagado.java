package Filtros;

import Modelo.LineaPedido;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FiltroPagado implements Filtro {
    // Estados que indican que el pedido ya pasó por "Pagado" (CU-2.1)
    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "pagado",
            "en preparacion",
            "listo para retiro",
            "listo retiro",
            "entregado"
    );

    private int descartados = 0;

    // Quita tildes, espacios sobrantes y mayúsculas para comparar sin problemas
    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('_', ' ')
                .trim()
                .toLowerCase();
    }

    @Override
    public List<LineaPedido> filtrar(List<LineaPedido> lineas) {
        List<LineaPedido> resultado = new ArrayList<>();
        descartados = 0;

        for (LineaPedido linea : lineas) {
            if (ESTADOS_VALIDOS.contains(normalizar(linea.estadoPedido()))) {
                resultado.add(linea);
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
        return "Pedidos no pagados";
    }
}