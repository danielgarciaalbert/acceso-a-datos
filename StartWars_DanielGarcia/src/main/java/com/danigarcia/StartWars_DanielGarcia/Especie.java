package com.danigarcia.StartWars_DanielGarcia;

import java.util.List;

class Especie {
	String name;
	String classification;
	String designation;
	String average_height;
	String skin_colors;
	String hair_colors;
	String eye_colors;
	String average_lifespan;
	String homeworld;
	String language;
	List<String> people;
	List<String> films;
	String created;
	String edited;
	String url;

	public Especie(String name, String classification, String designation, String average_height, String skin_colors,
			String hair_colors, String eye_colors, String average_lifespan, String homeworld, String language,
			List<String> people, List<String> films, String created, String edited, String url) {
		this.name = name;
		this.classification = classification;
		this.designation = designation;
		this.average_height = average_height;
		this.skin_colors = skin_colors;
		this.hair_colors = hair_colors;
		this.eye_colors = eye_colors;
		this.average_lifespan = average_lifespan;
		this.homeworld = homeworld;
		this.language = language;
		this.people = people;
		this.films = films;
		this.created = created;
		this.edited = edited;
		this.url = url;
	}

	public String obtenerNombre() {
		return name;
	}
}