package gestorAplicacion.administracion;
import java.util.ArrayList;
import java.io.Serializable;


public class Beca implements Serializable{
    private static final long serialVersionUID = 1L;
    private int cupos;
    private String convenio;
    private double promedioRequerido;
    private double avanceRequerido;
    private int estratoMinimo;
    private int creditosInscritosRequeridos;
    private int ayudaEconomica;
    private boolean necesitaRecomendacion;
    private static ArrayList<Beca> becas = new ArrayList<Beca>();

    // La clase beca permite crear objetos ¨beca¨ con los atributos especificados y los añade a una lista estatica //
   public Beca(int cupos, String convenio, double promedioRequerido, double avanceRequerido, int estratoMinimo,
            int creditosInscritosRequeridos, int ayudaEconomica, boolean necesitaRecomendacion) {
        this.cupos = cupos;
        this.convenio = convenio;
        this.promedioRequerido = promedioRequerido;
        this.avanceRequerido = avanceRequerido;
        this.estratoMinimo = estratoMinimo;
        this.creditosInscritosRequeridos = creditosInscritosRequeridos;
        this.ayudaEconomica = ayudaEconomica;                         
        this.necesitaRecomendacion = necesitaRecomendacion;       
        becas.add(this);                                                             
    }
    // Getters y Setters, proporcionan métodos para acceder y modificar los atributos privados de la clase.//
    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {                             
        this.cupos = cupos;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public double getPromedioRequerido() {
        return promedioRequerido;
    }

    public void setPromedioRequerido(double promedioRequerido) {
        this.promedioRequerido = promedioRequerido;
    }

    public double getAvanceRequerido() {
        return avanceRequerido;
    }

    public void setAvanceRequerido(double avanceRequerido) {
        this.avanceRequerido = avanceRequerido;
    }

    public int getEstratoMinimo() {
        return estratoMinimo;
    }
    public void setEstratoMinimo(int estratoMinimo) {
        this.estratoMinimo = estratoMinimo;
    }

    public int getCreditosInscritosRequeridos() {
        return creditosInscritosRequeridos;
    }

    public void setCreditosInscritosRequeridos(int creditosInscritosRequeridos) {
        this.creditosInscritosRequeridos = creditosInscritosRequeridos;
    }

    public int getAyudaEconomica() {
        return ayudaEconomica;
    }

    public void setAyudaEconomica(int ayudaEconomica) {
        this.ayudaEconomica = ayudaEconomica;
    }

    public boolean getNecesitaRecomendacion() {
        return necesitaRecomendacion;
    }
     
    public void setNecesitaRecomendacion(boolean necesitaRecomendacion) {
        this.necesitaRecomendacion = necesitaRecomendacion;
    }
// Metodos estaticos//
    public static ArrayList<Beca> getBecas() {
        return becas;
    }

    public static void eliminarBeca (Beca beca){
        becas.remove(beca);
    }
    
}
