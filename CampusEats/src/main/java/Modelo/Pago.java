package Modelo;

import java.time.LocalDateTime;
public record Pago(
    String idTransaccion,
    double monto,
    String estado,
    String codAutorizacion,
    String motivoRechazo,
    LocalDateTime fechaHora
) {}