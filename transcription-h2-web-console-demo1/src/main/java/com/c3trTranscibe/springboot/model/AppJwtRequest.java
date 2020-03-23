/**
 * 
 */
package com.c3trTranscibe.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rajesh
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppJwtRequest implements Serializable {

	private String username;
	private String password;
}
