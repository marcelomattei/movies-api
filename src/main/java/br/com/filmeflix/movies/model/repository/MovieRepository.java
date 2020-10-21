package br.com.filmeflix.movies.model.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.filmeflix.movies.model.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	@Query(value = "from Movie m order by m.id desc")
	public List<Movie> findLastLimitedTo(Pageable pageable);
}
