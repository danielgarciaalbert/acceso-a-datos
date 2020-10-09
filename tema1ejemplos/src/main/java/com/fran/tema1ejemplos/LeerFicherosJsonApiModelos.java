package com.fran.tema1ejemplos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class LeerFicherosJsonApiModelos {

	public static void apiModelos() {

		Object obj;
		try {
			// parseado el fichero "profesor.json"
			obj = new JSONParser().parse(new FileReader("profesor.json"));

			// casteando obj a JSONObject
			JSONObject jo = (JSONObject) obj;

			// cogiendo el nombre y el apellido
			String nombre = (String) jo.get("nombre");
			String apellido = (String) jo.get("apellido");

			System.out.println(nombre);
			System.out.println(apellido);

			// cogiendo la edad como long
			long edad = (long) jo.get("edad");
			System.out.println(edad);

			// cogiendo direccion
			Map domicilio = ((Map) jo.get("domicilio"));

			// iterando direccion Map
			Iterator<Map.Entry> itr1 = domicilio.entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry pareja = itr1.next();
				System.out.println(pareja.getKey() + " : " + pareja.getValue());
			}

			// cogiendo números de teléfonos
			JSONArray ja = (JSONArray) jo.get("numerosTelefonos");

			// iterando números de teléfonos
			Iterator itr2 = ja.iterator();

			while (itr2.hasNext()) {
				itr1 = ((Map) itr2.next()).entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry pareja = itr1.next();
					System.out.println(pareja.getKey() + " : " + pareja.getValue());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		// API de modelos
		apiModelos();

	}

}
