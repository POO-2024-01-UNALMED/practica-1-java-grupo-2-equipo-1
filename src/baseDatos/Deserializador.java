//Juan Diego / Daniel Hernando Zambrano Gonzales/ David Posada / Juan Miguel Ochoa Agudelo
package baseDatos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import gestorAplicacion.administracion.Grupo;
import gestorAplicacion.administracion.Horario;
import gestorAplicacion.administracion.Materia;
import gestorAplicacion.administracion.Salon;
import gestorAplicacion.usuario.Coordinador;
import gestorAplicacion.usuario.Estudiante;
import gestorAplicacion.usuario.Profesor;
import gestorAplicacion.usuario.Usuario;
import gestorAplicacion.administracion.Beca;

public class Deserializador {

    private static <T> void deserializar(ArrayList<T> lista, String nombre){
        
        File archivo = new File("");
        FileInputStream fis;   //abre el archivo 
        ObjectInputStream ois; // convierte los bytes en objetos

        try {

            File path = new File(archivo.getAbsolutePath()+"/src/baseDatos/temp/"+nombre+".txt");
            //creamos un archivo con la ruta de acceso completa
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            lista.addAll((ArrayList<T>) ois.readObject());
            ois.close();
            fis.close();

        } catch (FileNotFoundException e) { //EXCEPSIONES O ERRORES
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

    }

    public static void deserializarListas(){

        deserializar(Estudiante.getEstudiantes(), "Estudiantes");
        deserializar(Grupo.getGruposTotales(), "Grupos");
        deserializar(Materia.materiasTotales, "Materias");
        deserializar(Usuario.getUsuariosTotales(), "Usuarios");
        deserializar(Coordinador.getCoordinadoresTotales(), "Coordinadores");
        deserializar(Horario.getHorariosTotales(), "Horarios");
        deserializar(Salon.salones, "Salones");
        deserializar(Beca.getBecas(), "Becas");
        deserializar(Profesor.getProfesores(), "Profesores");
    }
    
}
