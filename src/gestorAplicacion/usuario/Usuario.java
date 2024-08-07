//Juan Diego / Daniel Hernando Zambrano Gonzales/ David Posada / Juan Miguel Ochoa Agudelo
package gestorAplicacion.usuario;

import java.io.Serializable;
import java.util.ArrayList;

import gestorAplicacion.administracion.Materia;

//clase abstracta que implementa serializable para guardar los objetos creados en posteriores clases
public abstract class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    protected long id;
    protected String nombre;
    protected String tipo;
    protected String pw;
    protected String facultad;
    protected static ArrayList<Usuario> usuariosTotales = new ArrayList<Usuario>();
   
    //constructores
    public Usuario(long id, String nombre, String facultad) {
        this.id = id;
        this.nombre = nombre;
        this.facultad = facultad;
        usuariosTotales.add(this);
    }

    public Usuario(long id, String nombre, String pw, String facultad){
        this.id = id;
        this.nombre = nombre;
        this.pw = pw;
        this.facultad = facultad;
        usuariosTotales.add(this);
    }
    // metodo abstracto que sera usado en las clases que hereden de usuario
    public abstract String toString();

    public static String mostrarUsuarios() {
    	String retorno = "";
    	int i = 1;
    	for(Usuario usuario:usuariosTotales) {
    		retorno += (i++)+". "+usuario.nombre+", id: "+usuario.id+"\n";
    	}
    	return retorno;
    }

    public boolean comprobacionFacultad(Usuario usuario){
        String facultad1 = this.getFacultad().toLowerCase();
        String facultad2 = usuario.getFacultad().toLowerCase();
        if (facultad1.equals(facultad2)){
            return true;
        }
        return false;
    }

    public void desmatricularDelSistema(Usuario usuario){

        for (Usuario u: Usuario.getUsuariosTotales()){

            if (usuario.equals(u)){
                    Usuario.getUsuariosTotales().remove(usuario);
                    break;
                }
        }
    }
    //metodos para eliminar y agregar materias nuevas
    public void eliminarMateria(Materia materia){
    	Materia.getMateriasTotales().remove(materia);
    }

    public void agregarMateria(String nombre, int codigo, String descripcion,int creditos, String facultad, ArrayList<Materia> prerrequisitos){
    	Materia nMateria = new Materia(nombre, codigo, descripcion, creditos, facultad, prerrequisitos);
    	Materia.getMateriasTotales().add(nMateria);
    }

    
    // getters y setters
    public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    
    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public static ArrayList<Usuario> getUsuariosTotales() {
        return Usuario.usuariosTotales;
    }

    public static void setUsuariosTotales(ArrayList<Usuario> usuariosTotales) {
        Usuario.usuariosTotales = usuariosTotales;
    }

}
