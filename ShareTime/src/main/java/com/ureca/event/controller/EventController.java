package com.ureca.event.controller;

import com.ureca.event.dto.Event;
import com.ureca.event.model.dao.EventDAO;
import com.ureca.event.model.service.EventService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Paths; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/main")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventDAO eventDAO;

    // 새로운 Event 생성
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws SQLException {
        System.out.println("POST /events");
        Event newEvent = new Event();
        newEvent.setTitle(event.getTitle());
        newEvent.setDescription(event.getDescription());
        newEvent.setStart_date(event.getStart_date());
        newEvent.setEnd_date(event.getEnd_date());
        newEvent.setGroup_type(event.getGroup_type());
        newEvent.setClass_id(event.getClass_id());
        newEvent.setStudy_id(event.getStudy_id());
        newEvent.setCreator_id(event.getCreator_id());
        Event createdEvent = eventService.insert(newEvent);
        if (createdEvent != null) {
        	System.out.println("Event created");
        	return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ID로 특정 Event 조회
    @GetMapping("/events/{event_id}")
    public ResponseEntity<Event> selectEvent(@PathVariable Integer event_id) throws SQLException {
        System.out.println("GET /events/" + event_id);
        Event event = eventService.select(event_id);
        if (event != null) {
            System.out.println("Event found");
            return new ResponseEntity<>(event, HttpStatus.OK);
        } else {
            System.out.println("Event not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 모든 Events 조회
    @GetMapping
    public ResponseEntity<List<Event>> selectAllEvent() throws SQLException {
        System.out.println("GET /events");
        List<Event> events = eventService.selectAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // 기존 Event 업데이트
    @PutMapping("/events/{event_id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Integer event_id, @RequestBody Event event) throws SQLException {
        System.out.println("PUT /events/" + event_id);
        int updatedEvent = eventService.update(event);
        if (updatedEvent > 0) {
            System.out.println("Event updated");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            System.out.println("Event not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 특정 Event 삭제
    @DeleteMapping("/events/{event_id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer event_id) throws SQLException {
        System.out.println("DELETE /events/" + event_id);
        int isDeleted = eventService.delete(event_id);
        if (isDeleted > 0) {
            System.out.println("Event deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            System.out.println("Event not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
