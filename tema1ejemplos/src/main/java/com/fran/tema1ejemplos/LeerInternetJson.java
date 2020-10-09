package com.fran.tema1ejemplos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;


public class LeerInternetJson {
	
	
	private static List<Tarea> listaTareas;
	private static List<Task> listaTareasGson;
	private static List<Task> listaTareasGsonDesSerializar;
	

	/**
	 * Metodo auxiliar que recibe un String correspondiente a una Url y devuelve el contenido
	 * de la misma como un String
	 * @param web
	 * @return
	 */
	public static String readUrl(String web) {
		try {
			URL url = new URL(web);
			URLConnection uc = url.openConnection();			
			uc.setRequestProperty("User-Agent", "PostmanRuntime/7.20.1");			
			uc.connect();
			String lines = new BufferedReader(
					new InputStreamReader(uc.getInputStream(), 
							StandardCharsets.UTF_8))
					.lines()
					.collect(Collectors.joining());
			//System.out.println(lines);
			return lines;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void leerInternet() {
		Object obj;
		try {
			String cadena = readUrl("https://jsonplaceholder.typicode.com/todos");
			//System.out.println(cadena);
			// parseado el fichero "profesor.json"
			obj = new JSONParser().parse(cadena);
			// casteando obj a JSONArray, tengo en jo el array completo
			JSONArray jo = (JSONArray) obj; 
			// Recorremos el Array
			Iterator itr1 = jo.iterator();
			while (itr1.hasNext()) {
				JSONObject c = (JSONObject) itr1.next();
				Tarea tarea = new Tarea((long) c.get("userId"),(long) c.get("id"),(String) c.get("title"),(boolean) c.get("completed"));				
				listaTareas.add(tarea);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static List<Task> readFromJson()
	{
		try
		{
			String json = readUrl("https://jsonplaceholder.typicode.com/todos");
			
			Gson gson = new Gson();        
			Task[] tasksAux = gson.fromJson(json, Task[].class);
			
			List<Task> tasks = new ArrayList<Task>();
			
			for(Task t: tasksAux)
			{
				tasks.add(t);
			}
			
			return tasks;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void serializeTasks(List<Task> tasks, String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream objectFile = new ObjectOutputStream(fileOut);
			objectFile.writeObject(tasks);
			objectFile.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Task> desSerializeTasks(String fileName) {
		ArrayList<Task> listaTask = new ArrayList<Task>();
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			listaTask = (ArrayList) in.readObject();
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaTask;
	}
	
	public static void main(String[] args) {
		
		/* En el siguiente ejemplo: */
		/* leo una url de internet cogiendo su Json y tratándolo como una cadena */
		/* posteriormente parseo dicho Json y construyo objetos Tarea con sus datos */
		/* Meto dichos objetos en un ArrayList y me quedo solo con las tareas completadas */
		System.out.println("-------Comienzo ejemplo Json desde Internet-----");
		listaTareas = new ArrayList();
		leerInternet();
		listaTareas.stream()
		.filter(e->e.isCompletado()==true)
		.forEach(e->System.out.println(e));
		
		/* En el siguiente ejemplo de la librería GSON: */
		/* leo una url de internet cogiendo su Json y tratándolo como una cadena */
		/* posteriormente asigno las Task del Json con la clase que tiene los mismos campos y nombres */
		/* Devuelvo las tareas y luego filtro las completadas */
		System.out.println("-------Comienzo ejemplo Json desde Internet con librería Gson-----");		
		listaTareasGson = new ArrayList();
		listaTareasGson = readFromJson();
		listaTareasGson.stream()
		.filter(e->e.isCompleted()==true)
		.forEach(e->System.out.println(e));
		
		/* En el siguiente ejemplo: */
		/* serializo una lista de tareas, posteriormente la desserializo */
		/* asignando la lectura del fichero a una nueva lista y la muestro */
		System.out.println("-------Comienzo ejemplo Serialización-----");				
		listaTareasGsonDesSerializar = new ArrayList();
		serializeTasks(listaTareasGson,"task.dat");
		listaTareasGsonDesSerializar = desSerializeTasks("task.dat");
		desSerializeTasks("task.dat").stream()
		.forEach(e->System.out.println(e));
		
	}
}
