package com.fran.tema1ejemplos;

import java.io.Serializable;

public class Persona implements Serializable {

	String nombre;
	String mail;
	String fecha;
	int sueldo;
	
	public Persona() {
		
	}
	
	public Persona(Persona p) {
		// Constructor de copia
		this.nombre = p.nombre;
		this.mail = p.mail;
		this.fecha = p.fecha;
		this.sueldo = p.sueldo;
	}
	
	public Persona(String nombre, String mail, String fecha) {
		this.nombre = nombre;
		this.mail = mail;
		this.fecha = fecha;
	}
	
	public Persona(String nombre, String mail, String fecha, int sueldo) {
		this.nombre = nombre;
		this.mail = mail;
		this.fecha = fecha;
		this.sueldo = sueldo;
	}

	/**
	 * Esta funci�n obtiene el nombre de la persona
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	
	
	/**
	 * @return the sueldo
	 */
	public int getSueldo() {
		return sueldo;
	}


	/**
	 * @param sueldo the sueldo to set
	 */
	public void setSueldo(int sueldo) {
		this.sueldo = sueldo;
	}


	@Override
	public String toString() {
		return "Persona [nombre=" + nombre + ", mail=" + mail + ", fecha=" + fecha + "]";
	}

	
	
}
