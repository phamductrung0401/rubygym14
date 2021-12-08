package com.rubygym.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account_student")
public class AccountStudent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "accumulation")
	private int accumulation;
	
	@Column(name = "expire")
	private LocalDate expireDate;
	
	@Column(name = "service_id")
	private int serviceId;

	public AccountStudent(int id, String username, String password, int accumulation, LocalDate expireDate,
			int serviceId) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.accumulation = accumulation;
		this.expireDate = expireDate;
		this.serviceId = serviceId;
	}
	
	public AccountStudent() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAccumulation() {
		return accumulation;
	}

	public void setAccumulation(int accumulation) {
		this.accumulation = accumulation;
	}

	public LocalDate getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	
	
}
