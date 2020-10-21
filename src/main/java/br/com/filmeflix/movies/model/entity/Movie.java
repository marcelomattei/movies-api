package br.com.filmeflix.movies.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Movie {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String name;
	@Column
	private String genre;
	@Column
	private Integer year;
	@Column
	private String director;
	
	public Movie() {
		
	}
	
	public Movie(Long id, String name, String genre, Integer year, String director) {
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.year = year;
		this.director = director;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getGenre() {
		return genre;
	}

	public Integer getYear() {
		return year;
	}

	public String getDirector() {
		return director;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public static MovieBuilder builder() {
		return new MovieBuilder();
	}
	
	public static class MovieBuilder {
		
		private Long id;
		private String name;
		private String genre;
		private Integer year;
		private String director;
		
		public MovieBuilder() {
			
		}
		
		public MovieBuilder id(Long id) {
			this.id = id;
			return this;
		}
		
		public MovieBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public MovieBuilder genre(String genre) {
			this.genre = genre;
			return this;
		}
		
		public MovieBuilder year(Integer year) {
			this.year = year;
			return this;
		}
		
		public MovieBuilder director(String director) {
			this.director = director;
			return this;
		}
		
		public Movie build() {
			return new Movie(id, name, genre, year, director);
		}
	}
}
