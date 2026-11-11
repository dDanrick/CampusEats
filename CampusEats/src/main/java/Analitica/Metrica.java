
package Analitica;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.util.List;

public interface Metrica {
    String nombre();
    String calcular(List<LineaPedido> lineasLimpias, List<Pago> pagosLimpios);
}
