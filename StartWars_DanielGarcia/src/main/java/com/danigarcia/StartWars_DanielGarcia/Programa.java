//Daniel García Albert

package com.danigarcia.StartWars_DanielGarcia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

public class Programa {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String opcion;
		List<Personaje> personajes = new ArrayList<>();
		personajes = desSerializarPersonajes("personajes.dat");

		do {
			MostrarMenu();
			opcion = sc.nextLine();

			switch (opcion) {
			case "1":
				System.out.println("\nHas elegido la opción 1 (Conversor)...");
				System.out.print("Introduce el código de una película: ");
				String codigoPelicula = sc.nextLine();
				System.out.println();
				String infoPelicula = leerUrl("https://swapi.dev/api/films/" + codigoPelicula + "/?format=json");

				String infoPeliculaParseada = "";
				try {
					Object o = new JSONParser().parse(infoPelicula);
					JSONObject jo = (JSONObject) o;
					infoPeliculaParseada = jo.toJSONString();
				} catch (Exception e) {
					// ERROR
				}

				convertir(infoPeliculaParseada);
				break;
			case "2":
				System.out.println("\nHas elegido la opción 2 (Añadir personaje)...");
				System.out.print("Introduce el código de un personaje: ");
				String codigoPersonaje = sc.nextLine();
				System.out.println();
				Personaje p;
				try {
					p = anyadirPersonaje("https://swapi.dev/api/people/" + codigoPersonaje + "/?format=json");

					if (!personajes.contains(p)) {
						personajes.add(p);
						System.out.println("Personaje añadido correctamente.");
					} else {
						System.out.println("Este personaje ya ha sido añadido previamente.");
					}
				} catch (Exception e) {
					System.out.println("Ese personaje no existe");
				}

				break;
			case "3":
				System.out.println("\nHas elegido la opción 3 (Guardar personajes)...");
				serializarPersonajes(personajes, "personajes.dat");
				System.out.println("Personajes guardados correctamente");
				break;
			case "4":
				System.out.println("\nHas elegido la opción 4 (Mostrar especie de personaje)...");
				System.out.print("Introduce el código de un personaje: ");
				codigoPersonaje = sc.nextLine();
				System.out.println();

				try {
					obtenerEspecies(codigoPersonaje);
				} catch (Exception e) {
					System.out.println("Error al obtener la especie de este personaje");
				}
				break;
			case "5":
				break;
			case "6":
				System.out.println("¡Hasta luego!");
				break;
			case "7": // MOSTRAR TEMPORALMENTE LA LISTA DE PERSONAJES
				for (int i = 0; i < personajes.size(); i++) {
					System.out.println(personajes.get(i));
				}
				break;
			default:
				System.out.println("default");
				break;
			}

			System.out.println();
		} while (!opcion.equals("6"));

		sc.close();
	}

	// - MOSTRAR MENU
	// ----------------------------------------------------------------------------------------------

	private static void MostrarMenu() {
		System.out.println("***************");
		System.out.println("** STAR WARS **");
		System.out.println("***************");

		System.out.println("1. Conversor");
		System.out.println("2. Añadir personaje");
		System.out.println("3. Guardar personaje");
		System.out.println("4. Conocer especie de personaje");
		System.out.println("5. Mostrar datos XML");
		System.out.println("6. Salir");
		System.out.print("Elige una opción: ");
	}

	// - LEER URL
	// ----------------------------------------------------------------------------------------------

	private static String leerUrl(String web) {
		try {
			URL url = new URL(web);
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("User-Agent", "PostmanRuntime/7.20.1");
			uc.connect();
			String lines = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8))
					.lines().collect(Collectors.joining());
			return lines;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// - 1. CONVERTIR JSON EN XML
	// ----------------------------------------------------------------------------------------------

	private static void convertir(String info) {
		String[] contenido = info.split(",\"");
		contenido[0] = contenido[0].substring(1);
		contenido[contenido.length - 1] = contenido[contenido.length - 1].substring(0,
				contenido[contenido.length - 1].length() - 1);

		List<String> etiquetas = new ArrayList<>();
		// El siguiente bucle elimina los caracteres '[', ']' y '"'
		// y rellena el ArrayList de etiquetas
		for (int i = 0; i < contenido.length; i++) {
			contenido[i] = contenido[i].replaceAll("\"", "");
			contenido[i] = contenido[i].replaceAll("[\\[\\]]", "");

			if (!etiquetas.contains(contenido[i].split(":")[0]) && !contenido[i].split(":")[0].equals("http")) {
				etiquetas.add(contenido[i].split(":")[0]);
			}
		}

		try {
			BufferedWriter resultado = new BufferedWriter(new FileWriter(new File("resultado.xml")));

			resultado.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<root>\n");

			String ultimaEtiqueta = "";
			for (int i = 0; i < contenido.length; i++) {
				if (etiquetas.contains(contenido[i].split(":")[0])) {
					resultado.write("<" + contenido[i].split(":")[0] + "> ");
					resultado.write(contenido[i].substring(contenido[i].split(":")[0].length() + 1));
					resultado.write(" </" + contenido[i].split(":")[0] + ">\n");

					ultimaEtiqueta = contenido[i].split(":")[0];
				} else {

					resultado.write("<" + ultimaEtiqueta + "> ");
					resultado.write(contenido[i]);
					resultado.write(" </" + ultimaEtiqueta + ">\n");
				}
			}
			resultado.write("</root>");

			resultado.close();
			System.out.println("El archivo se ha generado correctamente.");
		} catch (IOException errorDeFichero) {
			System.out.println("Ha habido problemas: " + errorDeFichero.getMessage());
		}

//		ESTA LÍNEA ES PARA MOSTRAR EN CONSOLA EL RESULTADO
//		String ultimaEtiqueta = "";
//		for (int i = 0; i < contenido.length; i++) {
//			if (etiquetas.contains(contenido[i].split(":")[0])) {
//				System.out.print("<" + contenido[i].split(":")[0] + "> ");
//				System.out.print(contenido[i].substring(
//						contenido[i].split(":")[0].length() + 1));
//				System.out.println(" </" + contenido[i].split(":")[0] + ">");
//				
//				ultimaEtiqueta = contenido[i].split(":")[0];
//			} else {
//				
//				System.out.print("<" + ultimaEtiqueta + "> ");
//				System.out.print(contenido[i]);
//				System.out.println(" </" + ultimaEtiqueta + ">");
//			}
//		}
	}

	// - 2. AÑADIR PERSONAJE
	// ----------------------------------------------------------------------------------------------

	public static Personaje anyadirPersonaje(String url) {
		try {
			String json = leerUrl(url);

			Gson gson = new Gson();
			Personaje p = gson.fromJson(json, Personaje.class);

			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// - 3. SERIALIZAR PERSONAJES
	// ----------------------------------------------------------------------------------------------

	public static void serializarPersonajes(List<Personaje> personajes, String archivo) {
		try {
			FileOutputStream archivoSalida = new FileOutputStream(archivo);
			ObjectOutputStream objectFile = new ObjectOutputStream(archivoSalida);
			objectFile.writeObject(personajes);
			objectFile.close();
			archivoSalida.close();
		} catch (IOException e) {
			e.printStackTrace();
			// System.out.println("Error al serializar personajes");
		}
	}

	// - 4. VER ESPECIE DE PERSONAJE
	// ----------------------------------------------------------------------------------------------

	private static void obtenerEspecies(String codigoPersonaje) {
		try {
			List<String> nombreDeEspecies = new ArrayList<>();
			String json = leerUrl("https://swapi.dev/api/people/" + codigoPersonaje + "/?format=json");

			Gson gson = new Gson();
			Personaje personaje = gson.fromJson(json, Personaje.class);
			Especie especie;

			if (!personaje.species.isEmpty()) {
				for (int i = 0; i < personaje.species.size(); i++) {
//					System.out.println(personaje.species.get(i) + "?format=json");
					String newJson = leerUrl(personaje.species.get(i) + "?format=json");
					Gson newGson = new Gson();
					especie = newGson.fromJson(newJson, Especie.class);

				}
			} else {
				System.out.println("No hay ninguna especie registrada");
			}

//			System.out.println("----- PRUEBA -----");
//			System.out.println(personaje.species);
//			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// - DESERIALIZAR PERSONAJES
	// ----------------------------------------------------------------------------------------------

	public static ArrayList<Personaje> desSerializarPersonajes(String archivo) {
		ArrayList<Personaje> listaPersonajes = new ArrayList<Personaje>();
		try {
			FileInputStream archivoEntrada = new FileInputStream(archivo);
			ObjectInputStream entrada = new ObjectInputStream(archivoEntrada);
			listaPersonajes = (ArrayList) entrada.readObject();
			archivoEntrada.close();
			archivoEntrada.close();
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
		return listaPersonajes;
	}
}
