package com.ureca.event.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ureca.event.dto.Event;
import com.ureca.user.dto.User;

@Mapper
public interface EventDAO {
	public int insert(Event event) throws SQLException;

	public int update(Event event) throws SQLException;

	public int delete(int id) throws SQLException;

	public Event select(int id) throws SQLException; //One Event select

	public List<Event> selectAll() throws SQLException;// All Event select
}
