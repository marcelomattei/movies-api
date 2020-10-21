package br.com.filmeflix.movies.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.collect.Lists;

import br.com.filmeflix.movies.model.entity.Movie;
import br.com.filmeflix.movies.model.repository.MovieRepository;
import br.com.filmeflix.movies.service.impl.MovieServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MovieServiceTest {
	
	MovieService service;
	
	@MockBean
	MovieRepository repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new MovieServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Should save a movie")
	public void saveMovieTest() {
		Movie movie = Movie
				.builder()
				.name("Até o último homem")
				.genre("Drama")
				.year(2017)
				.director("Mel Gibson").build();
		Mockito.when(repository.save(movie))
			.thenReturn(new Movie(1L, "Até o último homem", "Drama", 2017, "Mel Gibson"));
		
		Movie savedMovie = service.save(movie);
		
		assertThat(savedMovie.getId()).isNotNull();
		assertThat(savedMovie.getName()).isEqualTo(movie.getName());
		assertThat(savedMovie.getGenre()).isEqualTo(movie.getGenre());
		assertThat(savedMovie.getYear()).isEqualTo(movie.getYear());
		assertThat(savedMovie.getDirector()).isEqualTo(movie.getDirector());
	}
	
	@Test
	@DisplayName("Should return a movie by id")
	public void findByIdTest() {
		Long id = 1l;
		Movie movie = Movie
				.builder()
				.id(id)
				.name("Até o último homem")
				.genre("Drama")
				.year(2017)
				.director("Mel Gibson").build();
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(movie));
		
		Optional<Movie> foundedMovie = service.findById(id);
		
		assertThat(foundedMovie.isPresent()).isTrue();
		assertThat(foundedMovie.get().getId()).isEqualTo(id);
		assertThat(foundedMovie.get().getName()).isEqualTo(movie.getName());
		assertThat(foundedMovie.get().getGenre()).isEqualTo(movie.getGenre());
		assertThat(foundedMovie.get().getYear()).isEqualTo(movie.getYear());
		assertThat(foundedMovie.get().getDirector()).isEqualTo(movie.getDirector());
	}
	
	@Test
	@DisplayName("Should return empty for a not existent movie id")
	public void findByNotExistentIdTest() {
		Long id = 1l;
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		Optional<Movie> foundedMovie = service.findById(id);
		assertThat(foundedMovie.isPresent()).isFalse();
	}
	
	@Test
	@DisplayName("Should delete a movie")
	public void deleteMovieTest() {
		Long id = 1l;
		Movie movie = new Movie(id, "Até o último homem", "Drama", 2017, "Mel Gibson");
		assertDoesNotThrow(() -> service.delete(movie));
		Mockito.verify(repository, Mockito.times(1)).delete(movie);
	}
	
	@Test
	@DisplayName("Should return a given number of movies by limit")
	public void findLastLimitedToTest() {
		Integer limit = 2;
		List<Movie> movies = Lists.newArrayList(
				new Movie(2L, "Resgate do soldado Ryan", "Drama", 1998, "Steven Spielberg"),
				new Movie(1L, "Até o último homem", "Drama", 2017, "Mel Gibson")
			);
		Mockito.when(repository.findLastLimitedTo(Mockito.any(PageRequest.class)))
			.thenReturn(movies);
		
		List<Movie> result = service.findLastLimitedTo(limit);
		assertThat(result.size()).isEqualTo(2);
		assertThat(result).isEqualTo(movies);
	}
}
