package com.rubygym.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="schedule")
public class Schedule {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="trainer_student_id")
	private int trainerStudentId;
	
	@Column(name = "time_id")
	private int timeId;


	public Schedule(int id, int trainerStudentId, int timeId) {
		super();
		this.id = id;
		this.trainerStudentId = trainerStudentId;
		this.timeId = timeId;
	}

	public Schedule() {
		
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

	public int getTimeId() {
		return timeId;
	}

	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}
	
	
	
	
}
