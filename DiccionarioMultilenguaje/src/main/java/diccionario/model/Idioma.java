package diccionario.model;

/**
 * Representa un idioma con su nombre y código ISO.
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 *     Idioma esp = new Idioma("Español", "es");
 *     System.out.println(esp); // Español (es)
 * </pre>
 */
public class Idioma {

    /** Nombre del idioma (ejemplo: "Español"). */
    private String nombre;

    /** Código ISO del idioma (ejemplo: "es"). */
    private String codigoISO;

    /** Constructor vacío requerido para serialización. */
    public Idioma() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param nombre nombre del idioma.
     * @param codigoISO código ISO del idioma.
     */
    public Idioma(String nombre, String codigoISO) {
        this.nombre = nombre;
        this.codigoISO = codigoISO;
    }

    // ================== Getters y Setters ==================

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoISO() {
        return codigoISO;
    }

    public void setCodigoISO(String codigoISO) {
        this.codigoISO = codigoISO;
    }

    // ================== Métodos útiles ==================

    @Override
    public String toString() {
        return nombre + " (" + codigoISO + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Idioma)) return false;
        Idioma idioma = (Idioma) o;
        return codigoISO != null && codigoISO.equalsIgnoreCase(idioma.codigoISO);
    }

    @Override
    public int hashCode() {
        return codigoISO != null ? codigoISO.toLowerCase().hashCode() : 0;
    }
}
