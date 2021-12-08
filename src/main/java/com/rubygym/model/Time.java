package com.rubygym.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.simple.JSONObject;

@Entity
@Table(name = "time")
public class Time {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name = "date_of_week")
	private int dateOfWeek;
	
	@Column(name = "start")
	private LocalTime start;
	
	@Column(name = "finish")
	private LocalTime finish;

	public int getId() {
		return id;
	}

	public int getDateOfWeek() {
		return dateOfWeek;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getFinish() {
		return finish;
	}

	@Override
	public String toString() {
		return "{" + "\"id\":" + id 
				+ ",\"dateOfWeek\":" + dateOfWeek 
				+ ",\"start\":" + start 
				+ ",\"finish\":" + finish + "}";
	}
	
	public JSONObject toJsonObject() {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("dayOfWeek", dateOfWeek);
		jsonObject.put("start", start);
		jsonObject.put("finish", finish);
		return null;
	}
	
}
