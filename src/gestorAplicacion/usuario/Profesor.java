//Juan Diego / Daniel Hernando Zambrano Gonzales/ David Posada / Juan Miguel Ochoa Agudelo
package gestorAplicacion.usuario;
import java.util.ArrayList;
import gestorAplicacion.administracion.*;
import java.io.Serializable;

public class Profesor implements Serializable{
    private String nombre;
    private String facultad;
    private Horario horario;
    private ArrayList<Materia> materiasDadas = new ArrayList<Materia>(10);
    private ArrayList<Grupo> grupos = new ArrayList<Grupo>();
    private static ArrayList<Profesor> profesores =new ArrayList<Profesor>();
    private static final long serialVersionUID = 1L;
    
    /* En esta parte del codigo (de linea 17 a 40) se genera una sobrecarga de metodos, en los cuales se toma
    el metodo que cumpla con la estructura correspondiente */
    public Profesor(String nombre, String facultad, Horario horario, ArrayList<Materia> materiasDadas, ArrayList<Grupo> grupos){
        this.nombre = nombre;
        this.facultad = facultad;
        this.horario = horario;
        this.materiasDadas = materiasDadas;
        this.grupos = grupos;
        Profesor.profesores.add(this);
    }

    public Profesor(String nombre, String facultad, Horario horario, ArrayList<Materia> materiasDadas){
        this.nombre = nombre;
        this.facultad = facultad;
        this.horario = horario;
        this.materiasDadas = materiasDadas;
        Profesor.profesores.add(this);
    }
    
    public Profesor(String nombre, String facultad, ArrayList<Materia> materiasDadas){
        this.nombre = nombre;
        this.facultad = facultad;
        this.horario = new Horario();
        this.materiasDadas = materiasDadas;
        Profesor.profesores.add(this);
    }
    /* Este metodo añade un nuevo grupo a la materia, de la misma manera se genera su espacio en un horario determinado,
     la ubicacion en el horario del nuevo grupo depende del String que se tenga en la variable "horario" contenida
     en la clase Horario.*/ 
    public void vincularGrupo(Grupo g) {
    	this.grupos.add(g);
    	this.horario.ocuparHorario(g.getHorario(), g);
    }
    /* Este metodo busca si el grupo si se encuentra vinculado, siendo asi encuentra la posicion
      del grupo que se quiere remover. Usa el liberarHorario de la clase Horario eliminando asi
      los caracteres de la matriz, es decir, dejando el espacio en el horario libre, finalmente 
      remueve el grupo que este en la posicion definida. 
     */
    public void desvincularGrupo(Grupo g){
        if (grupos.contains(g)){
            int indice = grupos.indexOf(g); 
            ArrayList<String> horLibre = grupos.get(indice).getHorario();
            this.horario.liberarHorario(horLibre);
            grupos.remove(indice);
        }
    }
    /* Este metodo explica si se da la materia o no, es decir, si la persona coloca el nombre de una materia
      que si se esta dando, devolvera verdadero. Por el contrario, si escribe el nombre de una materia y 
      el sistema no lo encuentra, devolvera falso. Por esto se utiliza el equals, para identificar si el nombre
      que se escribio si sea de la materia que se quiere buscar*/
    public boolean daMateria(String nombre) {
    	for(Materia materia:this.getMateriasDadas()) {
    		if(materia.getNombre().equals(nombre)) {
    			return true;
    		}
    	}
    	return false;
    }
    /* Este metodo tiene que ver si se deberia recomendar a un estudiante o no, esta decision
     es tomada a partir de dos variables, la primera es la chance que tiene el estudiante para ser recomendado,
     donde se verifica si este ya ha estado en un grupo con el mismo profesor(siendo asi aumenta 5 puntos de chance),
     y si el estudiante y el profesor pertenecen a la misma factultad (siendo asi aumenta 3 puntos). 
     A partir de la cantidad de puntos que se tenga, se toma en cuenta la suerte que tenga el estudiante, donde
     se toma un valor al azar del 1 al 10 y se compara con la cantidad de puntos de chance, si los puntos de chance
     son mayores o iguales al numero al azar, se recomendara al estudiante, de lo contrario no sera recomendado*/
    public static boolean recomendarEstudiante(Estudiante estudiante){
        boolean bool = false;
        for (Profesor profesor : Profesor.getProfesores()){
            int chance = 0;
            int suerte = (int)(Math.random()*10+1);

            for(Grupo grupo : estudiante.getGruposVistos()){
                if (grupo.getProfesor().getNombre().equals(profesor.getNombre()) == true){
                    chance += 5;
                    break; 
                }
            }
            
            if (estudiante.getFacultad().equals(profesor.getFacultad()) == true){
                chance += 3;
            }
            if (chance >= suerte){
                bool= true;
            }  
        }
        return bool;
    }
    /* El metodo realiza un enumerado de todos los profesores y todas las materias que ha dado cada uno,
    cuando sea la ultima materia en el listado del profesor hace un salto de linea y prosigue con otro profesor.
    Ejmp: "1. Juan Ríos. Materias: Estadistica, POO".
          "2. Pedro Leon. Materias: Fisica, Catedra, Algebra".  
    */
    public static String mostrarProfesores() {
    	String r = "";
    	int i = 1;
    	for(Profesor profesor:Profesor.profesores) {
    		r += (i++)+". "+profesor.getNombre()+". Materias: ";
    		for (Materia materia:profesor.getMateriasDadas()) {
    			if (profesor.getMateriasDadas().indexOf(materia) == profesor.getMateriasDadas().size()-1) {
    				r += materia.getNombre()+".\n";
    			}
    			else {
    				r += materia.getNombre()+", ";
    			}
    		}
    	}
    	return r;
    }
   /* El metodo se utiliza para saber que profesores dan una materia determinada, se ingresa el nombre de la materia
   y se crea una nueva lista de profesores seleccionados que den la materia. El If se utiliza para evitar duplicados
   con los nombres de los profesores y no se agreguen nuevamente a la lista.
    */
    public static ArrayList<Profesor> profesoresDeMateria(String nombre) {
    	ArrayList<Profesor> profes = new ArrayList<Profesor>();
    	for (Profesor profesor:Profesor.getProfesores()) {
    		if(profesor.daMateria(nombre)&&!profes.contains(profesor)) {
    			profes.add(profesor);
    		}
    	}
    	return profes;
    }
    /* El metodo crea una lista con todos los profesores que dan la misma materia, se introduce el nombre de la materia
    y genera un enumerado con saltos de linea de los profesores que la dictan*/
    public static String mostrarProfesMateria(String nombre) {
    	String r = "";
    	int i = 1;
    	ArrayList<Profesor> profes = profesoresDeMateria(nombre);
    	for (Profesor profesor:profes) {
    		r += (i++)+". "+profesor.getNombre()+".\n"; 
    	}
    	return r;
    }

    // Metodos set y get para la clase
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFacultad() {
        return facultad;
    }
    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public ArrayList<Materia> getMateriasDadas() {
        return materiasDadas;
    }
    public void setMateriasDadas(ArrayList<Materia> materiasDadas) {
        this.materiasDadas = materiasDadas;
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }
    public void setGrupos(ArrayList<Grupo> grupos) {
        this.grupos = grupos;
    }

    public static ArrayList<Profesor> getProfesores() {
        return profesores;
    }
    public static void setProfesores(ArrayList<Profesor> profesores) {
        Profesor.profesores = profesores;
    }
    
    public void setHorario(Horario horario) {
    	this.horario = horario;
    }
    
    public Horario getHorario() {
    	return this.horario;
    }

}
