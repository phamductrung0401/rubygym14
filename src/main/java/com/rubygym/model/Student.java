package com.rubygym.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

import java.time.LocalDate;

import javax.persistence.Column;

@Entity
@Table(name="student")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="avatar")
	private String avatar;
	
	@Column(name="name")
	private String name;
	
	@Column(name="sex")
	private int sex;
	
	@Column(name="date_of_birth")
	private LocalDate dateOfBirth;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="email")
	private String email;
	
	@Column(name="description")
	private String description;
	
	@Column(name="weight")
	private float weight;
	
	@Column(name="height")
	private float height;
	
	@Column(name="bmi")
	private float bmi;
	
	@Column(name="others")
	private String others;
	
	@Column(name="target")
	private String target;
	
	@Column(name="account_student_id")
	private int accountId;

	public Student(int id, String avatar, String name, int sex, LocalDate dateOfBirth, String phoneNumber, String email,
			String description, float weight, float height, float bmi, String others, String target, int accountId) {
		super();
		this.id = id;
		this.avatar = avatar;
		this.name = name;
		this.sex = sex;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.description = description;
		this.weight = weight;
		this.height = height;
		this.bmi = bmi;
		this.others = others;
		this.target = target;
		this.accountId = accountId;
	}
	
	public Student() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getBmi() {
		return bmi;
	}

	public void setBmi(float bmi) {
		this.bmi = bmi;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	
	
}
