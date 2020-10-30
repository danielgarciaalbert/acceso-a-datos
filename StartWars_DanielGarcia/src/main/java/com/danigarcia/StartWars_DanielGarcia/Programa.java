//Daniel García Albert

package com.danigarcia.StartWars_DanielGarcia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

		File archivo = new File("personajes.dat");
		if (archivo.length() > 0 && archivo.exists()) {
			personajes = deserializarPersonajes("personajes.dat");
		}

		do {
			mostrarMenu();
			opcion = sc.nextLine();

			switch (opcion) {
			case "1":
				System.out.println("\nHas elegido la opción 1 (Conversor)...");
				System.out.print("Introduce el código de una película: ");
				String codigoPelicula = sc.nextLine();
				System.out.println();
				try {
					convertir(leerUrl("https://swapi.dev/api/films/" + codigoPelicula + "/?format=json"));
				} catch (Exception e) {
					System.out.println("La conversión del archivo ha sido cancelada");
				}
				break;
			case "2":
				System.out.println("\nHas elegido la opción 2 (Añadir personaje)...");
				System.out.print("Introduce el código de un personaje: ");
				String codigoPersonaje = sc.nextLine();
				System.out.println();
				Personaje p;
				try {
					p = anyadirPersonaje("https://swapi.dev/api/people/" + codigoPersonaje + "/?format=json");

					if (p != null) {
						if (!personajes.contains(p)) {
							personajes.add(p);
							System.out.println("Personaje añadido correctamente.");
						} else {
							System.out.println("Este personaje ya ha sido añadido previamente.");
						}	
					} else {
						System.out.println("No se ha añadido ningún personaje");
					}
				} catch (Exception e) {
					System.out.println("No se ha añadido ningún personaje");
				}

				break;
			case "3":
				System.out.println("\nHas elegido la opción 3 (Guardar personajes)...");
				
				if (personajes.size() > 0) {
					serializarPersonajes(personajes, "personajes.dat");	
					System.out.println("Personajes guardados correctamente");
				} else {
					System.out.println("No hay ningún personaje añadido");
				}
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
				System.out.println("\nHas elegido la opción 5 (Mostrar datos de XML)...");
				System.out.println();

				leerXML("resultado.xml");
				break;
			case "6":
				System.out.println("¡Hasta luego!");
				break;
			default:
				System.out.println("Esta opción no es válida.");
				break;
			}
			System.out.println();
		} while (!opcion.equals("6"));

		sc.close();
	}

	// - MOSTRAR MENU

	private static void mostrarMenu() {
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

	// - 1. CONVERTIR JSON EN XML

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
		} catch (IOException e) {
			System.out.println("Error al convertir el archivo");
		}
	}

	// - 2. AÑADIR PERSONAJE

	public static Personaje anyadirPersonaje(String url) {
		try {
			String json = leerUrl(url);

			Gson gson = new Gson();
			Personaje personaje = gson.fromJson(json, Personaje.class);

			return personaje;
		} catch (Exception e) {
			System.out.println("Error al leer información del personaje");
			return null;
		}
	}

	// - 3. SERIALIZAR PERSONAJES

	public static void serializarPersonajes(List<Personaje> personajes, String archivo) {
		try {
			FileOutputStream archivoSalida = new FileOutputStream(archivo);
			ObjectOutputStream objectFile = new ObjectOutputStream(archivoSalida);
			objectFile.writeObject(personajes);
			objectFile.close();
			archivoSalida.close();
		} catch (IOException e) {
			System.out.println("Error al serializar personajes");
		}
	}

	// - 4. VER ESPECIE DE PERSONAJE

	private static void obtenerEspecies(String codigoPersonaje) {
		try {
			String json = leerUrl("https://swapi.dev/api/people/" + codigoPersonaje + "/?format=json");
			Gson gson = new Gson();
			Personaje personaje = gson.fromJson(json, Personaje.class);
			Especie especie;

			if (personaje.obtenerEspecies().length != 0) {
				for (int i = 0; i < personaje.obtenerEspecies().length; i++) {
					String newJson = leerUrl(personaje.obtenerEspecies()[i].replace("http", "https") + "?format=json");
					Gson newGson = new Gson();
					especie = newGson.fromJson(newJson, Especie.class);
					System.out.println(especie.obtenerNombre());
				}
			} else {
				System.out.println("No hay ninguna especie registrada");
			}

		} catch (NullPointerException n) {
			//Aquí no muestro ningún mensaje porque ya lo muestra la función leerUrl()
		} catch (Exception e) {
			System.out.println("Error al obtener las especies del personaje");
		}
	}

	// - 5. LEER ARCHIVO XML

	private static void leerXML(String archivo) {
		try {
			File archivoEntrada = new File(archivo);

			if (archivoEntrada.length() != 0) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document documento = dBuilder.parse(archivoEntrada);
				documento.getDocumentElement().normalize();
				NodeList nodos = documento.getElementsByTagName("root");

				for (int i = 0; i < nodos.getLength(); i++) {
					Node nodo = nodos.item(i);

					if (nodo.getNodeType() == Node.ELEMENT_NODE) {
						Element elemento = (Element) nodo;

						if (elemento.hasChildNodes()) {
							NodeList listaDeNodos = nodo.getChildNodes();

							for (int j = 0; j < listaDeNodos.getLength(); j++) {
								Node otroNodo = listaDeNodos.item(j);
								if (otroNodo.getNodeName() != "#text")
									System.out.println(otroNodo.getNodeName() + ": " + otroNodo.getTextContent());
							}
						}
					}
				}
			} else {
				System.out.println("El archivo está vacío o no existe");
			}
		} catch (Exception e) {
			System.out.println("Error al leer el archivo");
		}
	}

	// - DESERIALIZAR PERSONAJES

	public static ArrayList<Personaje> deserializarPersonajes(String archivo) {
		ArrayList<Personaje> listaPersonajes = new ArrayList<>();
		try {
			FileInputStream archivoEntrada = new FileInputStream(archivo);
			ObjectInputStream entrada = new ObjectInputStream(archivoEntrada);
			listaPersonajes = (ArrayList<Personaje>) entrada.readObject();
			entrada.close();
			archivoEntrada.close();
		} catch (FileNotFoundException e) {
			System.out.println("Archivo no encontrado");
		} catch (InvalidClassException e) {
			System.out.println("Clase no válida");
		} catch (ClassNotFoundException e) {
			System.out.println("Clase no encontrada");
		} catch (IOException e) {
			System.out.println("Error desconocido");
		}
		return listaPersonajes;
	}
	
	// - LEER URL

	private static String leerUrl(String web) {
		try {
			URL url = new URL(web);
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("User-Agent", "PostmanRuntime/7.20.1");
			uc.connect();
			String lines = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), StandardCharsets.UTF_8))
					.lines().collect(Collectors.joining());
			return lines;
		} catch (FileNotFoundException e) {
			System.out.println("No se ha encontrado información con ese código.");
		} catch (Exception e) {
			System.out.println("Error desconocido al leer el enlace.");
		}
		return null;
	}
}
