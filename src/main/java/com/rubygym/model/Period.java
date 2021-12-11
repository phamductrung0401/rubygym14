package com.rubygym.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="period")
public class Period {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="trainer_student_id")
	private Integer trainerStudentId;
	
	@Column(name="start")
	private LocalDateTime start;
	
	@Column(name="finish")
	private LocalDateTime finish;
	
	@Column(name="content")
	private String content;
	
	@Column(name="note")
	private String note;

	public Period(Integer id, Integer trainerStudentId, LocalDateTime start, LocalDateTime finish, String content,
			String note) {
		super();
		this.id = id;
		this.trainerStudentId = trainerStudentId;
		this.start = start;
		this.finish = finish;
		this.content = content;
		this.note = note;
	}
	
	public Period() {

	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTrainerStudentId() {
		return trainerStudentId;
	}

	public void setTrainerStudentId(Integer trainerStudentId) {
		this.trainerStudentId = trainerStudentId;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getFinish() {
		return finish;
	}

	public void setFinish(LocalDateTime finish) {
		this.finish = finish;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
