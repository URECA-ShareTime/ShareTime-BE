package com.ureca.event.controller;

import com.ureca.event.dto.Event;
import com.ureca.event.model.dao.EventDAO;
import com.ureca.event.model.service.EventService;
import com.ureca.user.dto.User;
import com.ureca.user.model.service.UserService;
import com.ureca.user.util.JwtUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;

    // 새로운 Event 생성
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws SQLException {
        System.out.println("POST /events");
        Event newEvent = new Event();
        newEvent.setTitle(event.getTitle());
        newEvent.setDescription(event.getDescription());
        newEvent.setStart_time(event.getStart_time());
        newEvent.setEnd_time(event.getEnd_time());
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
    @GetMapping("/events")
    public ResponseEntity<List<Event>> selectAllEvent() throws SQLException {
        System.out.println("GET /events");
        List<Event> events = eventService.selectAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    
    //user의 class, study에 해당되는 event 조회
    @GetMapping("/events/user")
    public ResponseEntity<?> selectAllEventById(@RequestHeader("Authorization") String token) throws SQLException { 
        // JWT 토큰에서 user_id 추출
    	String extractedToken = token.replace("Bearer ", "");
        Integer user_id;
        try {
            user_id = jwtUtil.extractUserId(extractedToken);
            if (user_id == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage());
        }
       System.out.println("Get /events/user");
       
//       User user = userService.select(user_id);
//       List<Map<String, Object>> studyList = userService.getStudyListByUserId(user_id);
//       
//       if (user == null) {
//    	   return ResponseEntity.status(404).body("User not found.");
//       }
//       
//       Set<Event> eventSet = new HashSet<>(); 
//       List<Event> classEvents = eventService.selectAllEventByClassId(user.getClass_id());
//       List<Event> studyEvents = eventService.selectAllEventByStudyList(studyList);
//       
//       List<Event> mergedEvents = new ArrayList<>();
//   		
//       // 두 리스트를 모두 합치면서 중복을 제거
//       for (Event event : classEvents) {
//           if (!containsEventWithId(mergedEvents, event.getEvent_id())) {
//               mergedEvents.add(event);
//           }
//       }
//
//       for (Event event : studyEvents) {
//           if (!containsEventWithId(mergedEvents, event.getEvent_id())) {
//               mergedEvents.add(event);
//           }
//       }
       User user = userService.select(user_id);
       List<Map<String, Object>> studyList = userService.getStudyListByUserId(user_id);
       
       if (user == null) {
           return ResponseEntity.status(404).body("User not found.");
       }
       
       List<Event> mergedEvents = new ArrayList<>();
       
       // Class events 추가
       List<Event> classEvents = eventService.selectAllEventByClassId(user.getClass_id());
       for (Event event : classEvents) {
           if (!containsEventWithId(mergedEvents, event.getEvent_id())) {
               mergedEvents.add(event);
           }
       }
       
       // Study events 추가 (studyList가 비어있지 않을 때만)
       if (studyList != null && !studyList.isEmpty()) {
           List<Event> studyEvents = eventService.selectAllEventByStudyList(studyList);
           for (Event event : studyEvents) {
               if (!containsEventWithId(mergedEvents, event.getEvent_id())) {
                   mergedEvents.add(event);
               }
           }
       }
       
       return new ResponseEntity<>(mergedEvents, HttpStatus.OK);
    }
    
    // Helper 메서드: mergedEvents 리스트에 특정 event_id를 가진 이벤트가 있는지 확인
    private boolean containsEventWithId(List<Event> eventList, Integer eventId) {
        for (Event event : eventList) {
            if (event.getEvent_id().equals(eventId)) {
                return true;
            }
        }
        return false;
    }
    
    // 기존 Event 업데이트
    @PutMapping("/events/{event_id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("event_id") Integer event_id, @RequestBody Event event) throws SQLException {
        System.out.println("PUT /events/" + event_id);
        int eid = event_id;
        int updatedEvent = eventService.update(event, eid);
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
    public ResponseEntity<Void> deleteEvent(@PathVariable("event_id") Integer event_id) throws SQLException {
        System.out.println("DELETE /events/" + event_id);
        int eid = event_id;
        int isDeleted = eventService.delete(eid);
        if (isDeleted > 0) {
            System.out.println("Event deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            System.out.println("Event not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
