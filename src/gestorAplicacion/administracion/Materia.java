//Juan Diego Sanchez / Daniel Hernando Zambrano Gonzales/ David Posada Salazar/ Juan Miguel Ochoa Agudelo/ Sebastian Martinez Sequeira
package gestorAplicacion.administracion;
import java.util.ArrayList;
import gestorAplicacion.usuario.*;
import java.io.Serializable;

public class Materia implements Serializable{
    private static final long serialVersionUID = 1L;
    private String nombre;
    private final int codigo;
    private String descripcion;
    private int creditos;
    private String facultad;
    private int cupos;
    private ArrayList<Materia> prerrequisitos;
    private ArrayList<Grupo> grupos;
    public static ArrayList<Materia> materiasTotales = new ArrayList<Materia>();
    private String abreviatura;
    
    
/* desde la linea 23 a la 44 se genera una sobrecarga de constructores, donde se toma el constructor 
 * que tenga la estructura correspondiente*/
    public Materia(String nombre, int codigo, String descripcion, int creditos, String facultad) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.facultad = facultad;
        this.prerrequisitos = new ArrayList<Materia>();
        this.grupos = new ArrayList<Grupo>();
        this.hacerAbreviatura(nombre);
        materiasTotales.add(this);

    }

    public Materia(String nombre, int codigo, String descripcion, int creditos, String facultad, ArrayList<Materia> prerrequisitos) {
        this(nombre, codigo, descripcion, creditos, facultad);
        this.prerrequisitos = prerrequisitos;
    }

    public Materia(String nombre, int codigo, String descripcion,int creditos, String facultad, ArrayList<Materia> prerrequisitos, ArrayList<Grupo> grupos) {
        this(nombre, codigo, descripcion, creditos, facultad, prerrequisitos);
        this.grupos = grupos;
    }
    /* Este metodo indica la cantidad de cupos que se tiene en un grupo determinado*/
    public int cantidadCupos(){
        int cantidad = 0;
        for (Grupo pGrupo:getGrupos()){
            cantidad+=pGrupo.getCupos();
        }
        return cantidad;
    }
/* Este es un metodo de instancia el cual crea nuevos grupos con los parametros que se proporcionen, devolviendo asi el grupo generado*/
    public Grupo crearGrupo(int numero, Profesor profesor, ArrayList<String> horario, int cupos, Salon salon){

    	Grupo grupo = new Grupo(this, numero, profesor, horario, cupos, salon);
    	
    	cantidadCupos();
    	this.grupos.add(grupo);
    	
    	return grupo;
    }
    /* Este metodo muestra más informacion acerca de la materia */
    public String mostrarContenidos(){
        String contenido =  "Materia: " + this.nombre +
        					"\nCodigo: " + this.codigo +
        					"\nCreditos: " + this.creditos +
        					"\nFacultad: " + this.facultad +
        					"\nDescripcion:\n" + this.descripcion;
        return contenido;
        					
    }
    /* Este metodo afirma si existe el grupo que se busca*/
    public boolean existenciaGrupo(Grupo grupoBuscado){
        
        if (!grupos.isEmpty()){
            
            for (Grupo grupo: grupos){
                
                if (grupo.equals(grupoBuscado)){
                    return true;
                }
            }
            
            return false;
        }
        else{
            return false;
        }
    }
    /* Metodos utilizados para eliminar y agregar un grupo paso por paso*/
    public void eliminarGrupo(int numero){
    	Grupo grupo = this.grupos.get(numero-1);
    	grupo.getProfesor().desvincularGrupo(grupo);
    	grupo.getSalon().getHorario().liberarHorario(grupo.getHorario());
        this.grupos.remove(grupo);
        this.cupos -= grupo.getCupos();
        for(int i=numero-1;i<this.grupos.size();i++) {
        	Grupo grupoCamb = this.grupos.get(i);
        	int nGrupoAnt = grupoCamb.getNumero();
        	grupoCamb.setNumero(nGrupoAnt-1);
        }
        
    }
    
    public void agregarGrupo(int numero, Profesor profesor, ArrayList<String> horario, int cupos, Salon salon) {
    	/*el metodo recibe los parametros necesarios para crear un nuevo grupo*/
    	boolean dispSalon = true;
    	boolean dispProfesor = true;
    	boolean daMateria = profesor.daMateria(this.nombre);
    	
    	/*Se comprueba la disponibilidad del profesor y el salon para el horario ingresado, si almenos uno
    	no se cumple, no se agrega el grupo*/
    	
    	for(String hor:horario) {
    		dispProfesor = profesor.getHorario().comprobarDisponibilidad(hor);
    		dispSalon = salon.getHorario().comprobarDisponibilidad(hor);
    		
    		if(!dispProfesor||!dispSalon) {
    			break;
    		}
    	}
    	

    	/*En caso de contar con disponibilidad, se procede a declarar el nuevo grupo y agregarselo a su respectiva meteria, profesor y salon*/
    	if(dispProfesor&&dispSalon&&daMateria) {
    		Grupo nGrupo = crearGrupo(numero,profesor,horario,cupos,salon);
    		this.cupos += cupos;
    		salon.getHorario().ocuparHorario(horario, nGrupo);
    		profesor.vincularGrupo(nGrupo);
    	}
    }
    /*Metodo que retorna el grupo de un estudiante en especifico*/

    public Grupo buscarGrupoDeEstudiante(Estudiante estudiante){

        for (Grupo grupo: this.grupos){
            for (Estudiante e: grupo.getEstudiantes()){
                if (e == estudiante){
                    return grupo;
                }
            }
        }
        return null;
    }
    
     /* Busca una materia, teniendo en cuenta su nombre y su codigo.
      Si no existe, retorna -1 */
    public static int buscarMateria(String nombre, int codigo){
        
        
        for (int i = 0; i < materiasTotales.size(); i++){
            if (materiasTotales.get(i).getNombre().equals(nombre) && materiasTotales.get(i).getCodigo() == codigo){
                return i;
            }
        }
        return -1;
    }

         /* Comprueba si un estudiante puede estar en un grupo, dependiendo de los cupos, creditos, prerrequisitos y disponibilidad*/
    public static boolean puedeVerMateria(Estudiante estudiante,Grupo grupo){
        
        if (!(estudiante.getCreditos()+grupo.getMateria().getCreditos()<=Coordinador.getLimitesCreditos())){
            return false;
        }
        if (!estudiante.getHorario().comprobarDisponibilidad(grupo.getHorario())){
            return false;
        }
        if (grupo.getCupos()==0){
            return false;
        }
        if (!comprobarPrerrequisitos(estudiante,grupo.getMateria())){
            return false;
        }
        return true;
    } 
    
       /*  Comprueba si un estudiante cumple los pre-requisitos de una materia identificadas por el codigo de la materia*/
    public static boolean comprobarPrerrequisitos(Estudiante estudiante,Materia materia){
        
        
        ArrayList<Materia> materiasVistas = new ArrayList<Materia>();
        
        for (Grupo pGrupo:estudiante.getGruposVistos()){
            materiasVistas.add(pGrupo.getMateria());
        }

        for (Materia pMateria:materia.getPrerrequisitos()){
            boolean flag = false;
            for(Materia pVistas:materiasVistas){
                if (pMateria.getCodigo()==pVistas.getCodigo()){
                    flag =true;
                    break;
                }
            }
            if(!flag){
                return false;
            }
        }
        return true;
    }
    /* Metodo que separa el nombre de la materia entre las palabras que lo componen si tiene mas de 13 caracteres,
     luego cada palabra queda con los 3 primero caracteres que se tengan*/

    public void hacerAbreviatura(String nombre){
        String abreviatura = "";

        if (nombre.length() <= 13){
            this.abreviatura = nombre;
        }
        else{
            String[] palabras = nombre.split("\\s");
            for (String palabra : palabras){
                if (palabra.length() >= 3){
                    abreviatura += palabra.substring(0, 3) + " ";
                }
                else{
                    abreviatura += palabra.substring(0, palabra.length()) + " ";
                }
            }
            if (abreviatura.length() <= 13){
                this.abreviatura = abreviatura;
            }
            else{
                this.abreviatura = abreviatura.substring(0, 13);
            }
        }
    }
    /* Coloca en una lista los numeros de los grupos de una materia*/
    public String mostrarGrupos() {
    	String retorno = "";
    	int i = 1;
    	for(Grupo grupo:this.grupos) {
    		retorno += (i++)+". "+grupo.getNumero()+".\n";
    	}
    	return retorno;
    }
    /* Retorna la materia que se esta buscando */
    public static Materia encontrarMateria(String nombre) {
    	Materia mater = null;
    	for(Materia materia:Materia.getMateriasTotales()) {
    		if(materia.getNombre().equals(nombre)) {
    			mater = materia;
    		}
    	}
    	return mater;
    }
    /* Enumera todas las materias disponibles */
    public static String mostrarMaterias() {
    	String retorno = "";
    	int i = 1;
    	for(Materia materia:Materia.materiasTotales) {
    		retorno += (i++)+". "+materia.nombre+".\n";
    	}
    	return retorno;
    }
   
    /* Metodos set y get de la clase */
    
    public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getCodigo() {
        return this.codigo;
    }
    
    public int getCreditos() {
        return this.creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getFacultad() {
        return this.facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public int getCupos() {
        return this.cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public ArrayList<Materia> getPrerrequisitos() {
        return this.prerrequisitos;
    }

    public void setPrerrequisitos(ArrayList<Materia> prerrequisitos) {
        this.prerrequisitos = prerrequisitos;
    }

    public ArrayList<Grupo> getGrupos() {
        return this.grupos;
    }

    public void setGrupos(ArrayList<Grupo> grupos) {
        this.grupos = grupos;
    }

    public static ArrayList<Materia> getMateriasTotales(){
        return materiasTotales;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
}
