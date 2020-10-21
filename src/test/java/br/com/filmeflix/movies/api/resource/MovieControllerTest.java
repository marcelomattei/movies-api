package br.com.filmeflix.movies.api.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import br.com.filmeflix.movies.api.dto.MovieDTO;
import br.com.filmeflix.movies.model.entity.Movie;
import br.com.filmeflix.movies.service.MovieService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = MovieController.class)
@AutoConfigureMockMvc
public class MovieControllerTest {

	private static final String MOVIE_API = "/api/movies";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	MovieService service;
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should create a new movie sucessfully")
	public void createMovieTest() throws Exception {
		
		MovieDTO dto = createMovieDTO();
		Movie savedMovie = new Movie(1L, "Até o último homem", "Drama", 2017, "Mel Gibson");
		
		BDDMockito.given(service.save(Mockito.any(Movie.class))).willReturn(savedMovie);
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(MOVIE_API.concat("/admin"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
			.andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should throw a validation exception given movie missing data")
	public void createInvalidMovieTest() throws Exception {
		String json = new ObjectMapper().writeValueAsString(new MovieDTO());
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(MOVIE_API.concat("/admin"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		mvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errors", hasSize(4)));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should delete a given movie successfully")
	public void deleteMovieTest() throws Exception {
		Long id = 1l;
		BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.of(Movie
				.builder().id(1l).build())
		);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(MOVIE_API.concat("/admin/"+id));
		mvc.perform(request)
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Should return resource not found when movie id is not found during the delete process")
	public void deleteNotExistentMovieTest() throws Exception {
		Long id = 1l;
		BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(MOVIE_API.concat("/admin/"+id));
		mvc.perform(request)
			.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser(roles = "CLIENT")
	@DisplayName("Should return last added movies limited to 2 results")
	public void findLastMoviesAddedTest() throws Exception {
		Integer limit = 2;
		BDDMockito.given(service.findLastLimitedTo(Mockito.anyInt())).willReturn(Lists.newArrayList(
			new Movie(2L, "Resgate do soldado Ryan", "Drama", 1998, "Steven Spielberg"),
			new Movie(1L, "Até o último homem", "Drama", 2017, "Mel Gibson")
		));
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(MOVIE_API.concat("/last/"+ limit));
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[*]", hasSize(2)))
		    .andExpect(jsonPath("$[0].id", is(2)))
		    .andExpect(jsonPath("$[0].name", is("Resgate do soldado Ryan")))
		    .andExpect(jsonPath("$[1].id", is(1)))
		    .andExpect(jsonPath("$[1].name", is("Até o último homem")));
	}
	
	private MovieDTO createMovieDTO() {
		return MovieDTO
				.builder()
					.id(1L)
					.name("Até o último homem")
					.genre("Drama")
					.year(2017)
					.director("Mel Gibson")
				.build();
	}
}
