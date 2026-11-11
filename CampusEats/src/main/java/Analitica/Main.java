package Analitica;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepoAnalitica repo = new RepoAnaliticaEnMemoria();

        List<Metrica> metricas = List.of(
                new PlatosTop(),
                new PedidosPorHora(),
                new TicketPromedio(),
                new PagosRechazados());

        ServicioAnalitica servicio = new ServicioAnalitica(repo, metricas);
        servicio.cargar();
        servicio.resumen(LocalDate.of(2026, 10, 19), LocalDate.of(2026, 10, 30));
    }
}
