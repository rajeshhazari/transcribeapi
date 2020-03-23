/**
 *
 */
package com.c3trTranscibe.springboot.domain;

import com.c3trTranscibe.springboot.domain.AppUsers;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author rajesh
 *
 */
///@Table(value = "REGISTEREDAPPUSERS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisteredUser {

	@Id
	public Long id;
	public String username;
	public String email;

}
