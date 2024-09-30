package com.ureca.event.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ureca.event.dto.Event;

@Mapper
public interface EventDAO {
	public int insert(Event event) throws SQLException;

	public int update(Event event) throws SQLException;

	public int delete(int id) throws SQLException;

	public Event select(int id) throws SQLException; //One Event select

	public List<Event> selectAll() throws SQLException;// All Event select

	public List<Event> selectAllEventByClassId(int class_id);

	public List<Event> selectAllEventByStudyList(List<String> stringList);
}
