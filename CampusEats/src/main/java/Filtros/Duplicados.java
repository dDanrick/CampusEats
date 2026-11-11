package Filtros;

import Modelo.LineaPedido;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Duplicados implements Filtro {
    private int descartados = 0;

    @Override
    public List<LineaPedido> filtrar(List<LineaPedido> lineas) {
        List<LineaPedido> resultado = new ArrayList<>();
        Set<String> vistos = new HashSet<>();
        descartados = 0;

        for (LineaPedido linea : lineas) {
            // Un pedido tiene varias líneas (una por plato), por eso la clave
            // es idPedido + idPlato. Si esa combinación ya apareció,
            // la línea es una repetición del mismo pedido.
            String clave = linea.idPedido() + "|" + linea.idPlato();

            if (!vistos.add(clave)) {
                descartados++;
            } else {
                resultado.add(linea);
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
        return "Pedidos repetidos";
    }
}