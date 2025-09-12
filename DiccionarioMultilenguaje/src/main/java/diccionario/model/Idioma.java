package diccionario.model;
import java.util.Objects;

public class Idioma {
    private String nombre;
    private String codigoISO;
    
    public Idioma(String nombre, String codigoISO){
        this.nombre = nombre;
        this.codigoISO = codigoISO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoISO(){
        return codigoISO;
    }

    public void setCodigoISO(String codigoISO){
        this.codigoISO = codigoISO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // misma referencia
        if (o == null || getClass() != o.getClass()) return false; // no es un Idioma
        Idioma idioma = (Idioma) o;
        // Se considera que dos idiomas son iguales si tienen el mismo código ISO
        return Objects.equals(codigoISO, idioma.codigoISO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoISO); // coherente con equals
    }

    @Override
    public String toString() {
        return nombre + " (" + codigoISO + ")";
    }
}
