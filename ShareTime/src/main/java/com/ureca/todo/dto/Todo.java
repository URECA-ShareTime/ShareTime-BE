package com.ureca.todo.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer todo_id;
    private String title;
	private String description;
    private LocalDate created_at;
    private LocalDate updated_at;
    private Boolean is_completed;
    private Integer user_id;
    
    public Integer getTodo_id() {
		return todo_id;
	}
	public void setTodo_id(Integer todo_id) {
		this.todo_id = todo_id;
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
	public LocalDate getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDate create_at) {
		this.created_at = create_at;
	}
	public LocalDate getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDate update_at) {
		this.updated_at = update_at;
	}
	public Boolean getIs_completed() {
		return is_completed;
	}
	public void setIs_completed(Boolean is_completed) {
		this.is_completed = is_completed;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

}