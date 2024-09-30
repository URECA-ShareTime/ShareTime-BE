package com.ureca.todo.model.dao;

import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ureca.todo.dto.Todo;

@Mapper
public interface TodoDAO {

	int insert(Todo todo) throws SQLException;

	Todo findById(Integer todo_id) throws SQLException;

	List<Todo> findByDate(@Param("date") LocalDate date, @Param("user_id") int user_id) throws SQLException;

	int update(Todo todo) throws SQLException;

	int delete(int id) throws SQLException;

}
