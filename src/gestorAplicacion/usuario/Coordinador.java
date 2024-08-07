//Juan Diego / Daniel Hernando Zambrano Gonzales/ David Posada / Juan Miguel Ochoa Agudelo
package gestorAplicacion.usuario;

import java.util.ArrayList;
import gestorAplicacion.administracion.*;
import java.io.Serializable;

public class Coordinador extends Usuario implements Serializable{
    private final static int limitesCreditos=20;
    private static final long serialVersionUID = 1L;
    private static ArrayList<Coordinador> coordinadoresTotales = new ArrayList<Coordinador>();
    private static String[] facultades = {"Facultad de arquitectura", "Facultad de ciencias", "Facultad de ciencias agrarias" , "Facultad de ciencias humanas y economicas", "Facultad de minas", "Sede"};

    public Coordinador(String facultad,long id, String nombre, String pw) {
        super(id, nombre,pw, facultad);
        super.setTipo("Coordinador");
        Coordinador.coordinadoresTotales.add(this);
    }
    
    /**El coordinador tiene la facultad de eliminar un estudiante de un grupo, esto lo hara accediendo 
     al metoto "existenciaEstudiante" y "eliminarEstudiante"*/
    public String desmatricular(Estudiante estudiante, Grupo grupo){
        boolean estaMatriculado = grupo.existenciaEstudiante(estudiante);

        if (estaMatriculado){
            grupo.eliminarEstudiante(estudiante);
            return "El estudiante ha sido desmatriculado de la materia y su respectivo grupo";
        }
        else{
            return "El estudiante no estaba matriculado";
        }
    }
    /**Primero se recorren los grupos asociados a la materia, a cada grupo se le obtiene
     su profesor,luego se llama al metodo desvincularGrupo, liberando asi al profesor de dicho grupo
    finalmente en el proximo for se recorre cada estudiante perteneciente al grupo en cuestion
     desmatriculando asi uno por uno*/
    public void resturarMateria(Materia materia){
    	
        for (int i=0;i<materia.getGrupos().size();i++){
            Grupo puntero_Grupo = materia.getGrupos().get(i);
            puntero_Grupo.getProfesor().desvincularGrupo(puntero_Grupo);
  
            for (int j=0;j<puntero_Grupo.getEstudiantes().size();j++){
                Estudiante puntero_Estudiante = puntero_Grupo.getEstudiantes().get(j);
                this.desmatricular(puntero_Estudiante, puntero_Grupo);
                //puntero_Estudiante.desmatricularMateria(materia);
            }   
        }
    }
    /**el metodo se encarga de eliminar un estudiante de un grupo y tambien de la lista de usuarios totales
    Primero recorre la lista de estudiantes y compara, en caso de que el estudiante de la lista sea igual a "e"
    hara que "e1" tome este valor, posteriormente comprueba si "e1" es diferente a null lo que significa que
    no es nulo, en este caso removera al estudiante de la lista de estudiantes...
    finalmente en el ultimo for recorremos la lista de usuario y comprobamos uno por uno si esos usuarios son instancias
    de estudiantes, esto a traves del "instanceof"
    */
    public void desmatricularDelSistema(Usuario estudiante){
        Estudiante e1 = null;
        for (Estudiante e: Estudiante.getEstudiantes()){
            if (e ==(Estudiante)estudiante){                              //casting para poder comparar
                e1 = e;
            }
        }
        if (e1 != null){
            Estudiante.getEstudiantes().remove(e1);
        }
        for (Usuario usuario: Usuario.getUsuariosTotales()){
            if (usuario instanceof Estudiante){
                if (((Estudiante) usuario).equals(estudiante)){
                    Usuario.getUsuariosTotales().remove(usuario);
                    break;
                }
            }
        }
    }

    
    /**
    -Toma una lista de materias que se desean ver. 
    -Crear un horario aleatorio en base de los grupos disponibles.
    -Retorna una lista estatica de tres elementos: Un booleano que nos dira si fue posible o no
    -crear el horaio, el horario generado y la materia que no permitio crear el horario en caso de existir.
    */
    public static Object[] crearHorario(ArrayList<Materia> materias){
        Object[] resultado = new Object[3];
        Horario horario =  new Horario();
        boolean ok = true;
        Materia materiaObstaculo = null;
        
        int[] gPosible = new int[materias.size()];   
        int[] mPosibles = new int[materias.size()];    
        int i =0; // indice de materias
        
        while (true){
            ArrayList<String> pClases = materias.get(i).getGrupos().get(gPosible[i]).getHorario();
            if (horario.comprobarDisponibilidad(pClases)){
                
                // Se la asignamos al horario
                horario.ocuparHorario(materias.get(i).getGrupos().get(gPosible[i]));
                // Al ponerse 1 significa que si es posible poner dicha materia
                mPosibles[i] =1;
                // Pasamos a la siguiente materia
                i++;
                // Comprobamos si es la ultima 
                if (i==materias.size()){
                    break;
                }

            }else{
                // Pasamos al siguiente grupo en la materia i
                gPosible[i]++;

                // Comprobamos si la materia es imposible de dar
                if (gPosible[i]==materias.get(i).getGrupos().size()){
                    // Tenemos que probar todas las posibilidades por lo tanto probamos con el siguiente grupo de la maeria i-1
                    i--;
                    horario.liberarHorario(materias.get(i).getGrupos().get(gPosible[i]).getHorario());
                    gPosible[i]++;
                    gPosible[i+1]=0;

                    // Comprobamos si, aunque iteramos todas las posibilidades no se puede poner la materia i
                    if (gPosible[i]==materias.get(i).getGrupos().size()){
                        int m =0;
                        for (int k:mPosibles){
                            if (k==0){
                                materiaObstaculo = materias.get(m);
                                ok = false;
                            }else{
                                m++;
                            }
                        }
                        break;
                    }
                }

            }
        }

        resultado[0] = ok;
        resultado[1] = horario;
        resultado[2] = materiaObstaculo;

        return resultado;

    }

    
    
    /*Metodo eliminarMateria: Recibira una materia y rectificara si la encuentre en la base de datos, posteriormente
     ejecuta el codigo dentro del if, donde remueve la materia y finaliza usando el metodo definido anteriomnrte
     "resturarmateria"*/

    public void eliminarMateria(Materia materia){
        if(Materia.getMateriasTotales().contains(materia)){
            Materia.getMateriasTotales().remove(materia);
            resturarMateria(materia);
        }
        
    }

    
    
    /*Metodo agregarMateria: Recibira los parámetros necesarios para crear una materia, si esta no se encuentra en
    la base de datos, la creara con sus respectivos atributos*/
    public void agregarMateria(String nombre, int codigo, String descripcion,int creditos, String facultad, ArrayList<Materia> prerrequisitos){
        ArrayList<String> nombreMaterias = new ArrayList<String>();
        for (Materia materia : Materia.getMateriasTotales()){
          nombreMaterias.add(materia.getNombre());
        }
        if (nombreMaterias.contains(nombre) == false){
            Materia nMateria = new Materia(nombre, codigo, descripcion, creditos, facultad, prerrequisitos);
        }
    }

    
    
    
    /*Metodo candidato a beca: el metodo recibe un objeto de tipo estudiante y uno de tipo beca, el cual verifica que el estudiante cumpla con los requisitos necesarios para
    aplicar a la beca que se ingresa en los parametros y devuelve un booleano que indica si este es candidato a beca o no*/
    public boolean candidatoABeca(Estudiante estudiante, Beca tipoDeBeca){
        if (tipoDeBeca.getCupos() > 0){
            if ((estudiante.getPromedio() >= tipoDeBeca.getPromedioRequerido()) && (estudiante.getAvance() >= tipoDeBeca.getAvanceRequerido()) &&
             (estudiante.getCreditos() >= tipoDeBeca.getCreditosInscritosRequeridos()) && (estudiante.getEstrato() <= tipoDeBeca.getEstratoMinimo())){
                if (tipoDeBeca.getNecesitaRecomendacion()){
                    if (Profesor.recomendarEstudiante(estudiante)){
                        return true;
                    }else return false;
                } else {
                    return true; //no necesita recomendacion, pero cumple los demás requisitos
                 } 
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    public static String mostrarFacultades() {
    	String retorno = "";
    	int i = 1;
    	for(String facultad:facultades) {
    		retorno += (i++)+". "+facultad+"\n";
    	}
    	return retorno;
    }

    public String toString(){
        return "Nombre: "+ getNombre()+ "\nDocumento: "+ getId();
    }
        

                                                                                                                     
    // Getters - Setters

    public static int getLimitesCreditos() {
        return limitesCreditos;
    }

    public static ArrayList<Coordinador> getCoordinadoresTotales() {
        return coordinadoresTotales;
    }


	public static String[] getFacultades() {
		return facultades;
	}


	public static void setFacultades(String[] facultades) {
		Coordinador.facultades = facultades;
	}
    
    
}


