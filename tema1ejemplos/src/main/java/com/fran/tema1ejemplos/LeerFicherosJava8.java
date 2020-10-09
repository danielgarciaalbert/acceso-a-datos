package com.fran.tema1ejemplos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LeerFicherosJava8 {
	
	public static void main(String[] args) {
		try {
			List<String> lines = Files.readAllLines(Paths.get("frases.txt"), StandardCharsets.UTF_8);
			lines.forEach(x -> System.out.println(x));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
