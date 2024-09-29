package com.ureca.event.dto;

import java.sql.Date;
import java.util.List;

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
	private Date start_time;
	private Date end_time;
	private List<String> group_type;
	private List<Integer> class_id;
	private List<Integer> study_id;
	private Integer creator_id;
	
	private String groupTypeString;
	private String classIdString;
	private String studyIdString;

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

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public List<String> getGroup_type() {
		return group_type;
	}

	public void setGroup_type(List<String> group_type) {
		this.group_type = group_type;
	}

	public List<Integer> getClass_id() {
		return class_id;
	}

	public void setClass_id(List<Integer> class_id) {
		this.class_id = class_id;
	}

	public List<Integer> getStudy_id() {
		return study_id;
	}

	public void setStudy_id(List<Integer> study_id) {
		this.study_id = study_id;
	}

	public Integer getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(Integer creator_id) {
		this.creator_id = creator_id;
	}

	public String getGroupTypeString() {
		return groupTypeString;
	}

	public void setGroupTypeString(String groupTypeString) {
		this.groupTypeString = groupTypeString;
	}

	public String getClassIdString() {
		return classIdString;
	}

	public void setClassIdString(String classIdString) {
		this.classIdString = classIdString;
	}

	public String getStudyIdString() {
		return studyIdString;
	}

	public void setStudyIdString(String studyIdString) {
		this.studyIdString = studyIdString;
	}

	@Override
	public String toString() {
		return "Event{" +
				"event_id='" + event_id + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", start_time='" + start_time + '\'' +
				", end_time='" + end_time + '\'' +
				", group_type='" + group_type + '\'' +
				", class_id='" + class_id + '\'' +
				", study_id='" + study_id + '\'' +
				", creator_id='" + creator_id + '\'' +
				'}';
	}
}
