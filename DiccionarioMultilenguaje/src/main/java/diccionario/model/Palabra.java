package diccionario.model;

/**
 * Representa una palabra en un idioma específico, junto con su categoría gramatical.
 * Esta clase se asocia directamente a un objeto {@link Idioma}.
 *
 * <p>Ejemplo de uso:</p>
 * <pre>
 *     Idioma esp = new Idioma("Español", "es");
 *     Palabra p1 = new Palabra("casa", esp, "sustantivo");
 *     System.out.println(p1); // "casa [es]"
 * </pre>
 */
public class Palabra {

    /** Texto de la palabra (ejemplo: "casa"). */
    private String texto;

    /** Idioma al que pertenece la palabra. */
    private Idioma idioma;

    /** Categoría gramatical de la palabra (ejemplo: "sustantivo", "verbo"). */
    private String categoriaGramatical;

    /**
     * Constructor vacío.
     * Requerido para algunos frameworks y procesos de serialización.
     */
    public Palabra() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param texto texto de la palabra (ejemplo: "casa").
     * @param idioma objeto {@link Idioma} asociado a la palabra.
     * @param categoriaGramatical categoría gramatical (ejemplo: "sustantivo").
     */
    public Palabra(String texto, Idioma idioma, String categoriaGramatical) {
        this.texto = texto;
        this.idioma = idioma;
        this.categoriaGramatical = categoriaGramatical;
    }

    // ================== Getters y Setters ==================

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public String getCategoriaGramatical() {
        return categoriaGramatical;
    }

    public void setCategoriaGramatical(String categoriaGramatical) {
        this.categoriaGramatical = categoriaGramatical;
    }

    // ================== Métodos útiles ==================

    /**
     * Compara si dos palabras son iguales en función de su texto e idioma.
     *
     * @param o objeto a comparar.
     * @return {@code true} si tienen el mismo texto y pertenecen al mismo idioma.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Palabra)) return false;

        Palabra palabra = (Palabra) o;

        if (!texto.equalsIgnoreCase(palabra.texto)) return false;
        return idioma != null && idioma.equals(palabra.idioma);
    }

    @Override
    public int hashCode() {
        int result = texto != null ? texto.toLowerCase().hashCode() : 0;
        result = 31 * result + (idioma != null ? idioma.hashCode() : 0);
        return result;
    }

    /**
     * Devuelve una representación en formato "texto [códigoISO]".
     *
     * @return representación de la palabra.
     */
    @Override
    public String toString() {
        return texto + " [" + (idioma != null ? idioma.getCodigoISO() : "??") + "]";
    }

    /**
     * Verifica si la palabra pertenece al mismo idioma que otra.
     *
     * @param otra palabra a comparar.
     * @return {@code true} si ambas son del mismo idioma.
     */
    public boolean esDelMismoIdioma(Palabra otra) {
        if (otra == null || this.idioma == null) return false;
        return this.idioma.equals(otra.idioma);
    }
}
