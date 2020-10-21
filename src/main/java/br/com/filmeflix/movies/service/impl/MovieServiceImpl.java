package br.com.filmeflix.movies.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.filmeflix.movies.model.entity.Movie;
import br.com.filmeflix.movies.model.repository.MovieRepository;
import br.com.filmeflix.movies.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	private MovieRepository repository;
	
	public MovieServiceImpl(MovieRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Movie save(Movie movie) {
		return repository.save(movie);
	}

	@Override
	public Optional<Movie> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public void delete(Movie movie) {
		if (movie == null || movie.getId() == null) {
			throw new IllegalArgumentException("Movie id can't be null");
		}
		repository.delete(movie);

	}

	@Override
	public List<Movie> findLastLimitedTo(Integer limit) {
		if (limit <= 0) {
			throw new IllegalArgumentException("Movie limit can't be less or equal than zero");
		}
		PageRequest pageRequest = PageRequest.of(0, limit);
		return repository.findLastLimitedTo(pageRequest);
	}

}
