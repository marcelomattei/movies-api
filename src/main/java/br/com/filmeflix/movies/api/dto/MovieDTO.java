package br.com.filmeflix.movies.api.dto;

import javax.validation.constraints.NotNull;

public class MovieDTO {
	
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String genre;
	@NotNull
	private Integer year;
	@NotNull
	private String director;
	
	public MovieDTO() {
		
	}
	
	public MovieDTO(Long id, String name, String genre, Integer year, String director) {
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.year = year;
		this.director = director;
	}

	public static MovieDTOBuilder builder() {
		return new MovieDTOBuilder();
	}
	
	public static class MovieDTOBuilder {
		
		private Long id;
		private String name;
		private String genre;
		private Integer year;
		private String director;
		
		public MovieDTOBuilder() {
			
		}
		
		public MovieDTOBuilder id(Long id) {
			this.id = id;
			return this;
		}
		
		public MovieDTOBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public MovieDTOBuilder genre(String genre) {
			this.genre = genre;
			return this;
		}
		
		public MovieDTOBuilder year(Integer year) {
			this.year = year;
			return this;
		}
		
		public MovieDTOBuilder director(String director) {
			this.director = director;
			return this;
		}
		
		public MovieDTO build() {
			return new MovieDTO(id, name, genre, year, director);
		}
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
}
