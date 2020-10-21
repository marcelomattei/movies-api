package br.com.filmeflix.movies.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.filmeflix.movies.model.entity.Movie;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MovieRepositoryTest {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	MovieRepository repository;
	
	@Test
	@DisplayName("Should return a movie by id")
	public void findByIdTest() {
		Movie movie = Movie
				.builder()
				.name("Até o último homem")
				.genre("Drama")
				.year(2017)
				.director("Mel Gibson").build();
		entityManager.persist(movie);
		Optional<Movie> foundedMovie = repository.findById(movie.getId());
		assertThat(foundedMovie.isPresent()).isTrue();
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
		Movie savedMovie = repository.save(movie);
		assertThat(savedMovie.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Should delete a movie")
	public void deleteMovieTest() {
		Movie movie = Movie
				.builder()
				.name("Até o último homem")
				.genre("Drama")
				.year(2017)
				.director("Mel Gibson").build();
		entityManager.persist(movie);
		Movie foundedMovie = entityManager.find(Movie.class, movie.getId());
		repository.delete(foundedMovie);
		Movie deletedMovie = entityManager.find(Movie.class, movie.getId());
		assertThat(deletedMovie).isNull();
	}
	
	@Test
	@DisplayName("Should get last 2 movies added")
	public void findLastMoviesLimitedToTest() {
		Integer limit = 2;
		
		Movie movie1 = Movie
				.builder()
				.name("Até o último homem")
				.genre("Drama")
				.year(2017)
				.director("Mel Gibson").build();
		repository.save(movie1);
		
		Movie movie2 = Movie
				.builder()
				.name("Rocky")
				.genre("Ação")
				.year(1982)
				.director("Silvester Stalone").build();
		repository.save(movie2);
		
		Movie movie3 = Movie
				.builder()
				.name("Resgate do soldado Ryan")
				.genre("Drama")
				.year(1998)
				.director("Steven Spielberg").build();
		repository.save(movie3);
		
		PageRequest pageRequest = PageRequest.of(0, limit);
		List<Movie> result = repository.findLastLimitedTo(pageRequest);
		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(limit);
	}
	
}
