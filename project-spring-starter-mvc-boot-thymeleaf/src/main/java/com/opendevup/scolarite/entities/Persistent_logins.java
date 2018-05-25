/**
* @author  Walid Abbassi
*/
package com.opendevup.scolarite.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

//This tells Hibernate to make a table out of this class
@Entity
@Table(name = "persistent_logins")
public class Persistent_logins {
	
	// ------------------------
	// PRIVATE FIELDS
	// ------------------------
	
	@Id
	@Column(name="series")
	private String series;
	@NotEmpty
	@Column(name="username")
	private String username;
	@NotEmpty
	@Column(name="token")
	private String token;
	@NotEmpty
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_used")
	private Date last_used;
	
	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	// constructor For JPA
	public Persistent_logins() {
		super();
	}

	// constructor using Field
	public Persistent_logins(String series, @NotEmpty String username, @NotEmpty String token,
			@NotEmpty Date last_used) {
		super();
		this.series = series;
		this.username = username;
		this.token = token;
		this.last_used = last_used;
	}

	// Getter and setter methods
	
	public String getSeries() {
		return series;
	}


	public void setSeries(String series) {
		this.series = series;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public Date getLast_used() {
		return last_used;
	}


	public void setLast_used(Date last_used) {
		this.last_used = last_used;
	}
		
}// class persistent_logins