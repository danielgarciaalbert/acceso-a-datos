package com.fran.tema1ejemplos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

class LeerApiFutbol {
	public static void main(String[] args) {
		leerApiFutbol();
	}
	
	public static String readUrl(String web, String token) {
		try {
			URL url = new URL(web);
			URLConnection uc = url.openConnection();			
			uc.setRequestProperty("User-Agent", "PostmanRuntime/7.20.1");
			uc.setRequestProperty("X-Auth-Token", token);
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
	
	public static void leerApiFutbol() {
		String cadena = readUrl("https://api.football-data.org/v2/teams/90", 
			"9bb83c7801f2438096630b7bce7c201b");
		System.out.println(cadena);
	}
}