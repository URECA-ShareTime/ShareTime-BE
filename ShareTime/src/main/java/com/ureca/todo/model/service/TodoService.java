package com.ureca.todo.model.service;

import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.ureca.todo.dto.Todo;

public interface TodoService {
    public Todo insert(Todo todo) throws SQLException;
    public Todo findById(int id) throws SQLException;
    public List<Todo> findByDate(LocalDate date, int user_id) throws SQLException;
    public Todo update(int id, Todo todo) throws SQLException;
    public int delete(int id) throws SQLException;
}
