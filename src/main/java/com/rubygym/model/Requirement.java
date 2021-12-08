package com.rubygym.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "requirement")
public class Requirement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "trainer_student_id")
	private int trainerStudentId;
	
	@Column(name = "schedule_id")
	private int scheduleId = -1; // mặc định là -1, nếu thao tác là create
	
	@Column(name = "time_id_new")
	private int timeId;
	
	@Column(name = "category")
	private int category;


	public Requirement(int id, int trainerStudentId, int scheduleId, int timeId, int category) {
		super();
		this.id = id;
		this.trainerStudentId = trainerStudentId;
		this.scheduleId = scheduleId;
		this.timeId = timeId;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrainerStudentId() {
		return trainerStudentId;
	}

	public void setTrainerStudentId(int trainerStudentId) {
		this.trainerStudentId = trainerStudentId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}


	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getTimeId() {
		return timeId;
	}

	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}
	
	
}
