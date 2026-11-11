package Analitica;

import Modelo.LineaPedido;
import Modelo.Pago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoAnaliticaEnMemoria implements RepoAnalitica {
    private List<LineaPedido> lineas = new ArrayList<>();
    private List<Pago> pagos = new ArrayList<>();

    @Override
    public void guardarLineas(List<LineaPedido> lineas) {
        this.lineas = new ArrayList<>(lineas);
    }

    @Override
    public void guardarPagos(List<Pago> pagos) {
        this.pagos = new ArrayList<>(pagos);
    }

    @Override
    public List<LineaPedido> lineasEntre(LocalDate desde, LocalDate hasta) {
        return lineas.stream()
                .filter(l -> !l.fechaHora().toLocalDate().isBefore(desde)
                          && !l.fechaHora().toLocalDate().isAfter(hasta))
                .toList();
    }

    @Override
    public List<Pago> pagosEntre(LocalDate desde, LocalDate hasta) {
        return pagos.stream()
                .filter(p -> !p.fechaHora().toLocalDate().isBefore(desde)
                          && !p.fechaHora().toLocalDate().isAfter(hasta))
                .toList();
    }
}
