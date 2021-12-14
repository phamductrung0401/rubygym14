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
	private Integer id;
	
	@Column(name = "date_of_week")
	private Integer dayOfWeek;
	
	@Column(name = "start")
	private LocalTime start;
	
	@Column(name = "finish")
	private LocalTime finish;

	public Integer getId() {
		return id;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getFinish() {
		return finish;
	}

	@Override
	public String toString() {
		return "{" + "\"timeId\":" + id 
				+ ",\"dayOfWeek\":" + dayOfWeek 
				+ ",\"start\":" + start == null ? null : start.toString()
				+ ",\"finish\":" + finish == null ? null : finish.toString() + "}";
	}
	
	public JSONObject toJsonObject() {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("dayOfWeek", dayOfWeek);
		jsonObject.put("start", start);
		jsonObject.put("finish", finish);
		return null;
	}
	
}
