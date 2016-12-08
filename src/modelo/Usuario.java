package modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String dir;
	private String mail;
	private String pass;
	private char permisos;

	public Usuario() {
	}

	public Usuario(String nombre, String dir, String mail, String pass,
			char permisos) {
		this.nombre = nombre;
		this.dir = dir;
		this.mail = mail;
		this.pass = pass;
		this.permisos = permisos;
	}

	public String getDir() {
		return dir;
	}

	public String getMail() {
		return mail;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPass() {
		return pass;
	}

	public char getPermisos() {
		return permisos;
	}

	public String getPrintablePermissions() {
		if (this.permisos == 'a') {
			return "Administrador";
		} else {
			return "Cliente registrado";
		}
	}
}
