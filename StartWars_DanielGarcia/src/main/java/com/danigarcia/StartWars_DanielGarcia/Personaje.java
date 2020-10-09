package com.danigarcia.StartWars_DanielGarcia;

import java.io.Serializable;
import java.util.List;

class Personaje implements Serializable {
	String name;
	String height;
	String mass;
	String hair_color;
	String skin_color;
	String eye_color;
	String birth_year;
	String gender;
	String homeworld;
	List<String> films;
	List<String> species;
	List<String> vehicles;
	List<String> starships;
	String created;
	String edited;
	String url;
	
	public Personaje(String name, String height, String mass, String hair_color,
			String skin_color, String eye_color, String birth_year, 
			String gender, String homeworld, List<String> films, List<String> species, 
			List<String> vehicles, List<String> starships, String created, String edited,
			String url) {
		this.name = name;
		this.height = height;
		this.mass = mass;
		this.hair_color = hair_color;
		this.skin_color = skin_color;
		this.eye_color = eye_color;
		this.birth_year = birth_year;
		this.gender = gender;
		this.homeworld = homeworld;
		this.films = films;
		this.species = species;
		this.vehicles = vehicles;
		this.starships = starships;
		this.created = created;
		this.edited = edited;
		this.url = url;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.name.equals( ((Personaje) o).name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}