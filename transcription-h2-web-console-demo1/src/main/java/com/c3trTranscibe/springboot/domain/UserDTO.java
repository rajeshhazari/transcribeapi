/**
 * 
 */
package com.c3trTranscibe.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author rajesh
 *
 */
@Table(value = "APPUSERS")
@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class UserDTO {

	private String email;
	private String password;

}
