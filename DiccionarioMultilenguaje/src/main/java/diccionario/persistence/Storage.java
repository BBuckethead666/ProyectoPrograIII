package diccionario.persistence;



import java.io.IOException;

/**
 * Interfaz que define las operaciones de almacenamiento
 * y recuperación de datos para el diccionario multilenguaje.
 */
public interface Storage {

    /**
     * Guarda los datos en el archivo de persistencia.
     *
     * @param data objeto que se desea almacenar (generalmente el modelo principal).
     * @param filePath ruta del archivo donde se guardarán los datos.
     * @throws IOException si ocurre un error durante la escritura.
     */
    void save(Object data, String filePath) throws IOException;

    /**
     * Carga los datos desde un archivo de persistencia.
     *
     * @param filePath ruta del archivo desde donde se leerán los datos.
     * @param type clase del objeto esperado al deserializar.
     * @param <T> tipo genérico que representa el objeto a devolver.
     * @return el objeto cargado desde el archivo.
     * @throws IOException si ocurre un error durante la lectura.
     */
    <T> T load(String filePath, Class<T> type) throws IOException;
}
