package com.c3transcribe.transcribeapi.api.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Table("APPUSERS")
@Data @AllArgsConstructor @NoArgsConstructor
public class AppUsers implements Serializable {

	private @Id
	Long userid;
	private String username;
	@Column(value = "first_name")
	private String firstName;
	@Column(value = "last_name")
	private String lastName;
	private String email;
	@Column(value = "phone_number")
	private String phoneNumber;
	private boolean active;
	private boolean disabled;
	private boolean verified;
	private boolean locked;
	private String zipcode;
	private String password;
	@Column(value = "registered_date")
	private LocalDateTime registeredDate;
	@Column(value = "registered_date")
	private LocalDateTime lastModified;

	@MappedCollection(idColumn="userid", keyColumn="userid")
	private List<AppUsersAuth> appUsersAuthList ;

}

