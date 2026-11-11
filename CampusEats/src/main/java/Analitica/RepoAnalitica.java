package Analitica;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.time.LocalDate;
import java.util.List;

/** Repository: las métricas piden datos por rango de fechas sin saber dónde se guardan. */
public interface RepoAnalitica {
    void guardarLineas(List<LineaPedido> lineas);
    void guardarPagos(List<Pago> pagos);

    /** Líneas con fecha entre desde y hasta, ambos días incluidos. */
    List<LineaPedido> lineasEntre(LocalDate desde, LocalDate hasta);

    /** Pagos con fecha entre desde y hasta, ambos días incluidos. */
    List<Pago> pagosEntre(LocalDate desde, LocalDate hasta);
}
