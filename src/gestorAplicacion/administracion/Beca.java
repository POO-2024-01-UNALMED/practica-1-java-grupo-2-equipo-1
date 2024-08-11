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
