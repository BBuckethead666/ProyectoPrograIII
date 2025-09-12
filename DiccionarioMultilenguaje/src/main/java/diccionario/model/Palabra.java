package diccionario.model;

import java.util.List;

public class Palabra {
    public String texto;
    public Idioma idioma;
    public List<Palabra> traducciones;
    
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

    public List<Palabra> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(List<Palabra> traducciones) {
        this.traducciones = traducciones;
    }

    public Palabra(String texto, Idioma idioma, List<Palabra> traducciones) {
        this.texto = texto;
        this.idioma = idioma;
        this.traducciones = traducciones;
    }

    @Override
    public String toString() {
        return "Palabra [texto=" + texto + ", idioma=" + idioma + ", traducciones=" + traducciones + "]";
    }

}
