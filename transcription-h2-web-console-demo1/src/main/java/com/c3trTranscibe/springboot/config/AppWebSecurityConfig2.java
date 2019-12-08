package com.c3trTranscibe.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*@Configuration
@EnableWebSecurity
public class AppWebSecurityConfig2 extends WebSecurityConfigurerAdapter {
*/

public class AppWebSecurityConfig2 {
	/*
	 * @Autowired private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	 * 
	 * @Autowired private UserDetailsService jwtUserDetailsService;
	 * 
	 * @Autowired private JwtRequestFilter jwtRequestFilter;
	 * 
	 * private AuthenticationProvider authenticationProvider;
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("daoAuthenticationProvider") public void
	 * setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
	 * this.authenticationProvider = authenticationProvider; }
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("daoAuthenticationManager") public void
	 * configureAuthManager(AuthenticationManagerBuilder
	 * authenticationManagerBuilder){
	 * authenticationManagerBuilder.authenticationProvider(authenticationProvider);
	 * }
	 * 
	 * @Bean public DaoAuthenticationProvider daoAuthenticationProvider( ){
	 * DaoAuthenticationProvider daoAuthenticationProvider = new
	 * DaoAuthenticationProvider();
	 * daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	 * daoAuthenticationProvider.setUserDetailsService(jwtUserDetailsService);
	 * return daoAuthenticationProvider; }
	 * 
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
	 * throws Exception { // configure AuthenticationManager so that it knows from
	 * where to load // user for matching credentials // Use BCryptPasswordEncoder
	 * auth.userDetailsService(jwtUserDetailsService).passwordEncoder(
	 * passwordEncoder()).and().eraseCredentials(true);
	 * 
	 * //auth.authenticationProvider(daoAuthenticationProvider());
	 * 
	 * auth.inMemoryAuthentication() .withUser("user")
	 * .password(passwordEncoder().encode("password")) .authorities("ROLE_USER");
	 * 
	 * }
	 * 
	 * @Bean public PasswordEncoder passwordEncoder() { return new
	 * BCryptPasswordEncoder(); }
	 * 
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.cors().and().csrf().disable().authorizeRequests()
	 * .antMatchers(HttpMethod.POST
	 * ,"/api/v1/authenticate","/api/v1/public/**","/h2-console/**",
	 * "/api/v1/swagger-ui.html/**","/api/v1/swagger**").permitAll()
	 * .antMatchers("/api/v1/**").authenticated()
	 * .and().exceptionHandling().authenticationEntryPoint(
	 * jwtAuthenticationEntryPoint).and() .sessionManagement()
	 * .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and() .logout()
	 * .deleteCookies("remove") .invalidateHttpSession(true);
	 * 
	 * http.rememberMe().disable();
	 * 
	 * }
	 * 
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception { auth.userDetailsService(jwtUserDetailsService)
	 * .and().eraseCredentials(true); }
	 * 
	 * 
	 * 
	 * @Bean
	 * 
	 * @Override public AuthenticationManager authenticationManagerBean() throws
	 * Exception { return super.authenticationManagerBean(); }
	 * 
	 * @Override protected void configure(HttpSecurity httpSecurity) throws
	 * Exception { httpSecurity.cors() // dont authenticate this particular request
	 * .and().authorizeRequests().antMatchers("/","/actuator/**",
	 * "/api/v1/authenticate","/api/v1/**","/api/v1/public/**","/h2-console/**",
	 * "/api/v1/swagger-ui.html/**","/api/v1/swagger**").permitAll(). // all other
	 * requests need to be authenticated anyRequest().authenticated()
	 * .and().formLogin() .loginPage("/api/v1/authenticate") // default is /login
	 * with an HTTP get .failureUrl("/api/v1/error?failed") // make sure we use
	 * stateless session; session won't be used to // store user's state.
	 * //.and().exceptionHandling().authenticationEntryPoint(
	 * jwtAuthenticationEntryPoint) .and().sessionManagement()
	 * .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); // Add a filter to
	 * validate the tokens with every request
	 * httpSecurity.authenticationProvider(daoAuthenticationProvider())
	 * .exceptionHandling() .authenticationEntryPoint(jwtAuthenticationEntryPoint);
	 * 
	 * // We don't need CSRF for this example httpSecurity.csrf().disable();
	 * //httpSecurity.addFilterAfter((Filter) jwtRequestFilter,
	 * UsernamePasswordAuthenticationFilter.class);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @Bean CorsConfigurationSource corsConfigurationSource() { CorsConfiguration
	 * configuration = new CorsConfiguration();
	 * configuration.setAllowedOrigins(Arrays.asList("*"));
	 * configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS",
	 * "DELETE", "PUT", "PATCH"));
	 * configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin",
	 * "Content-Type", "Accept", "Authorization"));
	 * configuration.setAllowCredentials(true); UrlBasedCorsConfigurationSource
	 * source = new UrlBasedCorsConfigurationSource();
	 * source.registerCorsConfiguration("/**", configuration); return source; }
	 * 
	 */
	


}
