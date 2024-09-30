package com.ureca.event.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ureca.event.dto.Event;

public interface EventService {
	public Event insert(Event event) throws SQLException;  

	public int update(Event event, int event_id) throws SQLException;

	public int delete(int id) throws SQLException;

	public Event select(int id) throws SQLException; //One Event select

	public List<Event> selectAll() throws SQLException;// All Event select
}
