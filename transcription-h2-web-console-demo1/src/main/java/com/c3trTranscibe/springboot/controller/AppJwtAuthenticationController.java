package com.c3trTranscibe.springboot.controller;

import com.c3trTranscibe.springboot.domain.AppUsers;
import com.c3trTranscibe.springboot.domain.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import com.c3trTranscibe.springboot.config.AppJwtTokenUtil;
import com.c3trTranscibe.springboot.model.AppJwtRequest;
import com.c3trTranscibe.springboot.model.JwtResponse;
import com.c3trTranscibe.springboot.services.JwtUserDetailsService;

@RestController
@CrossOrigin
@Api(
		value = "AppJwtAuthenticationController",
		description = "Authentication of users in to Transcription service return jwt token.",
		produces="application/json" ,
		consumes="application/json" )
public class AppJwtAuthenticationController {



	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AppJwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@ApiOperation(value = "Authenticate to api service", response = JwtResponse.class)
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "Successfully Authenticated."),
					@ApiResponse(code = 401, message = "You are not authorized to login."),
					@ApiResponse(
							code = 403,
							message = "Accessing the resource you were trying to reach is forbidden"),
					@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
			})
  @RequestMapping(value = "/auth", method = RequestMethod.POST , produces = "application/json")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody AppJwtRequest authenticationRequest) throws Exception {
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    final AppUsers appuser =
        userDetailsService.getUserByEmail(authenticationRequest.getUsername());
    final String token = jwtTokenUtil.generateToken(new UserDTO(appuser.getUsername(),appuser.getPassword()));
    return ResponseEntity.ok(new JwtResponse(appuser.getEmail(),token));
  }

	private void authenticate(String username, String password) throws DisabledException,BadCredentialsException {
		try {
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			Assert.notNull(auth.getPrincipal(), "Authentication was unsuccessful"+auth.getName());
		} catch (DisabledException e) {
			throw new DisabledException("Your user is Disabled for security reasons, please contact us @rajesh_hazar@yahoo.com", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password.", e);
		}
	}
}