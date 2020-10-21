package br.com.filmeflix.movies.service;

import java.util.List;
import java.util.Optional;

import br.com.filmeflix.movies.model.entity.Movie;

public interface MovieService {
	
	Movie save(Movie movie);

	Optional<Movie> findById(Long id);

	void delete(Movie movie);

	List<Movie> findLastLimitedTo(Integer limit);
}
