/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filtros;

import Modelo.LineaPedido;
import java.util.List;

public interface Filtro {
    
    List<LineaPedido> filtrar(List<LineaPedido> lineas);
    
    int getRegistrosDescartados();

    String getNombre();
}