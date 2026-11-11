package Adaptadores;

import Modelo.Plato;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Adapta menu_admin.csv (separador ',') a objetos Plato. */
public class MenuCsvAdapter implements FuenteDeDatos<Plato> {
    private final Path rutaArchivo;

    public MenuCsvAdapter(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public List<Plato> leer() throws Exception {
        return Files.readAllLines(rutaArchivo).stream()
                .skip(1)
                .filter(linea -> !linea.isBlank())
                .map(linea -> linea.split(",", -1))
                .map(p -> new Plato(
                        Integer.parseInt(p[0].trim()),
                        p[1].trim(),
                        p[2].trim(),
                        Double.parseDouble(p[3].trim().replace(',', '.')),
                        p[4].trim()))
                .toList();
    }
}
