package br.com.filmeflix.movies.api.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.filmeflix.movies.api.dto.MovieDTO;
import br.com.filmeflix.movies.model.entity.Movie;
import br.com.filmeflix.movies.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/movies")
@Api("Movie API")
public class MovieController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);
	
	private MovieService service;
	
	public MovieController(MovieService service) {
		this.service = service;
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Create a new movie")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Movie successfully created")
	})
	public MovieDTO create(@RequestBody @Valid MovieDTO dto) {
		LOGGER.info("Creating a new movie for genre: {}", dto.getGenre());
		
		Movie entity = Movie
				.builder()
				.name(dto.getName())
				.genre(dto.getGenre())
				.year(dto.getYear())
				.director(dto.getDirector()).build();
		entity = service.save(entity);
		
		return MovieDTO.builder()
				.id(entity.getId())
				.name(entity.getName())
				.genre(entity.getGenre())
				.year(entity.getYear())
				.director(entity.getDirector())
				.build();
	}
	
	@RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation("Delete a movie by id")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Movie successfully deleted")
	})
	public void delete(@PathVariable Long id) {
		Movie movie = service
				.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		service.delete(movie);
	}
	
	@RequestMapping(value = "/last/{limit}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('CLIENT')")
	@ApiOperation("Find last added movies for a given limit")
	public List<MovieDTO> last(@PathVariable Integer limit) {
		return service.findLastLimitedTo(limit)
			.stream()
			.map(entity -> 
				MovieDTO.builder()
					.id(entity.getId())
					.name(entity.getName())
					.genre(entity.getGenre())
					.year(entity.getYear())
					.director(entity.getDirector())
				.build())
			.collect(Collectors.toList());
	}
}
