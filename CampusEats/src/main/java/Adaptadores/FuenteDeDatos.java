package Adaptadores;

import java.util.List;

/** Interfaz común (Adapter): cada fuente traduce su formato al modelo. */
public interface FuenteDeDatos<T> {
    List<T> leer() throws Exception;
}
