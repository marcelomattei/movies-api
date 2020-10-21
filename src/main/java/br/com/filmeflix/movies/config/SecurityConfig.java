package br.com.filmeflix.movies.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic()
			.authenticationEntryPoint((request, response, exp) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser("claudio")
			.password("$2a$10$e59rGaFvpijWXLh03j0aZOzBYQNrIRIjlB8sGwLvBL35fecblsW1m")
			.roles("ADMIN")
			.and()
			.withUser("marcelo")
			.password("$2a$10$e59rGaFvpijWXLh03j0aZOzBYQNrIRIjlB8sGwLvBL35fecblsW1m")
			.roles("CLIENT");
	}
}
