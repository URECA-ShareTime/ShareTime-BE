package com.ureca.event.dto;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Integer event_id;
	private String title;
	private String description;
	private Date start_date;
	private Date end_date;
	private String group_type;
	private Integer class_id;
	private Integer study_id;
	private Integer creator_id;

	// Getters and Setters
	public Integer getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getGroup_type() {
		return group_type;
	}

	public void setGroup_type(String group_type) {
		this.group_type = group_type;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public Integer getStudy_id() {
		return study_id;
	}

	public void setStudy_id(Integer study_id) {
		this.study_id = study_id;
	}

	public Integer getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(Integer creator_id) {
		this.creator_id = creator_id;
	}

	@Override
	public String toString() {
		return "Event{" +
				"event_id='" + event_id + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", start_date='" + start_date + '\'' +
				", end_date='" + end_date + '\'' +
				", group_type='" + group_type + '\'' +
				", class_id='" + class_id + '\'' +
				", study_id='" + study_id + '\'' +
				", creator_id='" + creator_id + '\'' +
				'}';
	}
}
