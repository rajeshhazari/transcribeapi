package com.c3transcribe.transcribeapi.module.config;

import com.c3transcribe.transcribeapi.api.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String[] AUTH_WHITELIST = {
			// -- swagger ui
			"/actuator/**",
			"/v2/api-docs/**",           // swagger
			"/webjars/**",            // swagger-ui webjars
			"/swagger-ui.html/**",  // swagger-ui resources
			"/swagger-resources/**",
			"/configuration/**",      // swagger configuration
			"/auth/**",
			"/public/**",
			"h2-console",
			"/csrf/**",
			"/version/**",           // app version
			"/register/**",           // register customer
			// other public endpoints of your API may be appended to this array
	};
	
	
	@Autowired
	private UserDetailsService jwtUserDetailService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		DaoAuthenticationProvider authDaoProvider = new DaoAuthenticationProvider();
		authDaoProvider.setPasswordEncoder(passwordEncoder());
		authDaoProvider.setUserDetailsService(jwtUserDetailService);
		auth.authenticationProvider(authDaoProvider);
		/*
		auth.userDetailsService(jwtUserDetailService)
		 	.passwordEncoder(passwordEncoder());
		 */
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/*
	public AuthenticationManager authenticationManagerBean() throws Exception {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
		return daoAuthenticationProvider;
	}*/

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
				.anonymous().disable()
				.authorizeRequests()
				.antMatchers("/api/v1/").authenticated()
				.antMatchers(AUTH_WHITELIST).permitAll()
				.anyRequest().authenticated()
				.and().
						exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.logout()
				.invalidateHttpSession(true)
				.deleteCookies("remove")
				;
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(AUTH_WHITELIST);
	}
}
