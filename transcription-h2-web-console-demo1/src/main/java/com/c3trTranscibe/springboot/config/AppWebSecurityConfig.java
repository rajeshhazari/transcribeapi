package com.c3trTranscibe.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html**",
            "/webjars/**","/authenticate/","/public/**","/h2-console/**","/", 
			"/csrf"
            // other public endpoints of your API may be appended to this array
    };

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	 @Bean
     public AuthenticationManager authenticationManagerBean() throws Exception {
         return super.authenticationManagerBean();
     }

	 @Bean
	 @Override
	 protected UserDetailsService userDetailsService() {
		return jwtUserDetailsService;
	}
	 
	 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.sessionManagement()
        // This will turn off creating sessions (as without it you would get a JSESSION-ID Cookie
        // However we provide REST Services here, having an additional session would be absurd.
        // NOTICE: With this it is pointless to use the SecurityContextHolder of Spring-Security,
        //         as without session it won't keep Information there.
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
		.authorizeRequests()
		.antMatchers(AUTH_WHITELIST).permitAll()
		.antMatchers(HttpMethod.GET,"/actuator/**").permitAll()
		.antMatchers("/api/v1/**").authenticated()
		.and().exceptionHandling().authenticationEntryPoint(swaggerAuthenticationEntryPoint())
		
        .and()
        .httpBasic()
        .and()
		.logout()
		.deleteCookies("remove")
		.invalidateHttpSession(true);

		http.rememberMe().disable();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(jwtUserDetailsService)
		//.and().eraseCredentials(true);
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	
	 @Override
	    public void configure(WebSecurity web) throws Exception {
		 	
	        web.ignoring().antMatchers(AUTH_WHITELIST);
	    }
	 
	 @Bean
	    public BasicAuthenticationEntryPoint swaggerAuthenticationEntryPoint() {
	        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
	        entryPoint.setRealmName("Swagger Realm");
	        return entryPoint;
	    }
	 
	 @Bean
	 public DaoAuthenticationProvider daoAuthenticationProvider() {
		 DaoAuthenticationProvider o = new DaoAuthenticationProvider();
		 o.setPasswordEncoder(passwordEncoder());
		 o.setUserDetailsService(jwtUserDetailsService);
		 return o;
				 		
	 }
	/*
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
	 */
	/*
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
	 */
}
