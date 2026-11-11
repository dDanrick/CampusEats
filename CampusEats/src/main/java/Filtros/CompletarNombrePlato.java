package Filtros;

import Modelo.LineaPedido;
import Modelo.Plato;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Último paso del pipeline: ya limpios los datos, completa el nombre del plato
 * desde el menú. No descarta nada. Un plato RETIRADO sigue en el menú, así que
 * se conserva en el historial (CU-7.3).
 */
public class CompletarNombrePlato implements Filtro {
    private final Map<Integer, String> nombres = new HashMap<>();

    public CompletarNombrePlato(List<Plato> menu) {
        for (Plato p : menu) {
            nombres.put(p.idPlato(), p.nombre());
        }
    }

    @Override
    public List<LineaPedido> filtrar(List<LineaPedido> lineas) {
        return lineas.stream()
                .map(l -> l.conNombre(nombres.getOrDefault(l.idPlato(), "Plato " + l.idPlato())))
                .toList();
    }

    @Override
    public int getRegistrosDescartados() {
        return 0;
    }

    @Override
    public String getNombre() {
        return "Completar nombre del plato (menu)";
    }
}
