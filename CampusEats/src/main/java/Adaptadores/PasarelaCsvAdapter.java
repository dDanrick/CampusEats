package Adaptadores;

import Modelo.Pago;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Adapta pagos_pasarela.csv (separador ';', coma decimal, fechas dd/MM/yyyy). */
public class PasarelaCsvAdapter implements FuenteDeDatos<Pago> {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final Path rutaArchivo;

    public PasarelaCsvAdapter(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public List<Pago> leer() throws Exception {
        return Files.readAllLines(rutaArchivo).stream()
                .skip(1)
                .filter(linea -> !linea.isBlank())
                .map(linea -> linea.split(";", -1))
                .map(p -> new Pago(
                        p[0].trim(),
                        Double.parseDouble(p[1].trim().replace(',', '.')),
                        p[2].trim(),
                        p[3].trim(),
                        p[4].trim(),
                        LocalDateTime.parse(p[5].trim(), FORMATO)))
                .toList();
    }
}
