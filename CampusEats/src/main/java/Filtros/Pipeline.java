package Filtros;

import Modelo.LineaPedido;
import java.util.ArrayList;
import java.util.List;

/** Pipeline (Pipes & Filters): ejecuta los filtros en orden e informa los descartes. */
public class Pipeline {
    private final List<Filtro> filtros = new ArrayList<>();

    public void agregarFiltro(Filtro filtro) {
        filtros.add(filtro);
    }

    public List<LineaPedido> ejecutar(List<LineaPedido> lineas) {
        List<LineaPedido> resultado = lineas;

        System.out.println("--- INICIO DE LIMPIEZA ---");
        System.out.println("Lineas iniciales: " + resultado.size());

        for (Filtro f : filtros) {
            resultado = f.filtrar(resultado);
            System.out.println("Descartadas por [" + f.getNombre() + "]: " + f.getRegistrosDescartados());
        }

        long pedidos = resultado.stream().map(LineaPedido::idPedido).distinct().count();
        System.out.println("Quedan al final: " + resultado.size() + " lineas de " + pedidos + " pedidos");
        return resultado;
    }
}
