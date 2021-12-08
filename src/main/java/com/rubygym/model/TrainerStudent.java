package com.rubygym.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="trainer_student")
public class TrainerStudent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="trainer_id")
	private int trainerId;
	
	@Column(name="student_id")
	private int studentId;
	
	@Column(name="route")
	private String route;
	
	@Column(name="comment")
	private String comment;

	public TrainerStudent(int id, int trainerId, int studentId, String route, String comment) {
		super();
		this.id = id;
		this.trainerId = trainerId;
		this.studentId = studentId;
		this.route = route;
		this.comment = comment;
	}
	
	public TrainerStudent() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
