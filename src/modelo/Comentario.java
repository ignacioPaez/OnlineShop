package modelo;

import java.io.Serializable;

public class Comentario implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String codigoComentario;
    String fecha;
    String hora;
    String codigoProducto;
    String Email;
    String nombre;
    String comentario;
    Usuario usuario;
    Producto producto;

    public Comentario(String codigoComentario, String fecha, String hora, String codigoProducto, String Email, String nombre, String comentario) {
        this.codigoComentario = codigoComentario;
        this.fecha = fecha;
        this.hora = hora;
        this.codigoProducto = codigoProducto;
        this.Email = Email;
        this.nombre = nombre;
        this.comentario = comentario;
    }

    public String getEmail() {
        return Email;
    }

    public String getCodigoComentario() {
        return codigoComentario;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getComentario() {
        return comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getNombre() {
        return nombre;
    }
    
    public String getFechaHora (){
        return fecha + " " + hora;
    }
}

