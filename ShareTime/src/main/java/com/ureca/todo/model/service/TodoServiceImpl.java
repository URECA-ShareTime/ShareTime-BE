package com.ureca.todo.model.service;

import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ureca.todo.dto.Todo;
import com.ureca.todo.model.dao.TodoDAO;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoDAO todoDAO;

    @Override
    public Todo insert(Todo todo) throws SQLException {
        int rowsInserted = todoDAO.insert(todo);
        if (rowsInserted > 0) {
            return todoDAO.findById(todo.getTodo_id());
        } else {
            throw new SQLException("Failed to insert todo");
        }
    }

    @Override
    public Todo findById(int id) throws SQLException {
        return todoDAO.findById(id);
    }

    @Override
    public List<Todo> findByDate(LocalDate date, int user_id) throws SQLException {
    	System.out.println("=======> date: " + date);
        return todoDAO.findByDate(date, user_id);
    }

    @Override
    public Todo update(int id, Todo todo) throws SQLException {
        try {
            Todo existingTodo = todoDAO.findById(id);
            if (existingTodo != null) {
                existingTodo.setTitle(todo.getTitle());
                existingTodo.setDescription(todo.getDescription());
                existingTodo.setIs_completed(todo.getIs_completed());
                System.out.println("===========> existingTodo:" + existingTodo);
                int rowsUpdated = todoDAO.update(existingTodo);
                if (rowsUpdated > 0) {
                    return todoDAO.findById(id);
                } else {
                    throw new SQLException("Failed to update todo");
                }
            } else {
                throw new SQLException("Todo not found");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        return todoDAO.delete(id);
    }
}
