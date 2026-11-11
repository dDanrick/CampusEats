package Filtros;

import Modelo.LineaPedido;
import java.util.ArrayList;
import java.util.List;

public class Cantidad implements Filtro {
    private int descartados = 0;

    @Override
    public List<LineaPedido> filtrar(List<LineaPedido> lineas) {
        List<LineaPedido> resultado = new ArrayList<>();
        descartados = 0;

        for (LineaPedido linea : lineas) {
            // Regla de negocio CU-1.4: La cantidad debe ser un entero mayor que cero
            if (linea.cantidad() <= 0) {
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
        return "Cantidad igual o menor a cero";
    }
}