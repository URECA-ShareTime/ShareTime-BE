package com.ureca.event.model.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.event.dto.Event;
import com.ureca.event.model.dao.EventDAO;

import io.jsonwebtoken.lang.Objects;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	EventDAO eventDAO;
	
	@Autowired
	private ObjectMapper objectMapper; //Json 변환에 이용

	@Override
	public Event insert(Event event) throws SQLException {
	    // List를 쉼표로 구분된 문자열로 변환하여 저장
	    event.setGroupTypeString(convertListToString(event.getGroup_type()));
	    event.setClassIdString(convertListToString(event.getClass_id()));
	    event.setStudyIdString(convertListToString(event.getStudy_id()));
	    
	    System.out.println("======> group_type :" + event.getGroupTypeString());

	    // 데이터베이스에 이벤트 삽입 (ID 생성)
	    int rowsInserted = eventDAO.insert(event);
	    
	    if (rowsInserted > 0) {
	        // 삽입된 이벤트의 ID를 가져와 다시 조회
	        return eventDAO.select(event.getEvent_id());
	    } else {
	        throw new SQLException("Event insertion failed.");
	    }
	}
	
	@Override
	public int update(Event event, int event_id) throws SQLException {
		// TODO Auto-generated method stub
		int id = event_id;
		try {
            // 데이터베이스에서 해당 이벤트가 존재하는지 확인
            Event existingEvent = eventDAO.select(id);
            if (existingEvent != null) {
                // 업데이트할 이벤트의 ID 설정
                event.setEvent_id(id);
                event.setGroupTypeString(convertListToString(event.getGroup_type()));
        	    event.setClassIdString(convertListToString(event.getClass_id()));
        	    event.setStudyIdString(convertListToString(event.getStudy_id()));
                System.out.println("event:" + event);
                // DAO를 통해 이벤트 업데이트 실행
                int rowsUpdated = eventDAO.update(event);
                
                // 업데이트 성공 여부 확인
                if (rowsUpdated > 0) {
                    return 1;  // 업데이트된 이벤트 id 반환
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 예외 처리 (예: 로깅)
        }
        // 이벤트가 존재하지 않거나 업데이트에 실패한 경우
        return 0;
    }

	@Override
	public int delete(int id) throws SQLException {
		// TODO Auto-generated method stub
		return eventDAO.delete(id);
	}

	@Override
	public Event select(int id) throws SQLException {
		// TODO Auto-generated method stub
		return eventDAO.select(id);
	}

	@Override
	public List<Event> selectAll() throws SQLException {
		// TODO Auto-generated method stub
		// 데이터베이스에서 이벤트 데이터 가져오기
        List<Event> events = eventDAO.selectAll();

        // 각 이벤트의 문자열 데이터를 배열로 변환
        for (Event event : events) {
            // groupType 문자열을 List로 변환
            event.setGroup_type(convertStringToList(event.getGroupTypeString()));

            // classId 문자열을 int List로 변환
            event.setClass_id(convertStringToIntegerList(event.getClassIdString()));

            // studyId 문자열을 int List로 변환
            event.setStudy_id(convertStringToIntegerList(event.getStudyIdString()));
        }
        return events;
    }
	
	@Override
	public List<Event> selectAllEventByClassId(int class_id){
		List<Event> classEvents = eventDAO.selectAllEventByClassId(class_id);
		// 각 이벤트의 문자열 데이터를 배열로 변환
        for (Event event : classEvents) {
            // groupType 문자열을 List로 변환
            event.setGroup_type(convertStringToList(event.getGroupTypeString()));

            // classId 문자열을 int List로 변환
            event.setClass_id(convertStringToIntegerList(event.getClassIdString()));

            // studyId 문자열을 int List로 변환
            event.setStudy_id(convertStringToIntegerList(event.getStudyIdString()));
        }
		return classEvents;
	}
	
	@Override
	public List<Event> selectAllEventByStudyList(List<Map<String, Object>> studyList) {
	    List<String> stringList = studyList.stream()
	        .map(map -> String.valueOf((Integer) map.get("study_id"))) // Integer를 String으로 변환
	        .collect(Collectors.toList());
	    List<Event> studyEvents = eventDAO.selectAllEventByStudyList(stringList);
	 // 각 이벤트의 문자열 데이터를 배열로 변환
        for (Event event : studyEvents) {
            // groupType 문자열을 List로 변환
            event.setGroup_type(convertStringToList(event.getGroupTypeString()));

            // classId 문자열을 int List로 변환
            event.setClass_id(convertStringToIntegerList(event.getClassIdString()));

            // studyId 문자열을 int List로 변환
            event.setStudy_id(convertStringToIntegerList(event.getStudyIdString()));
        }
        return studyEvents;
	}
	
	
	// 문자열 리스트 변환 메소드
	private List<String> convertStringToList(String csv) {
	    if (csv == null || csv.isEmpty()) {
	        return List.of(); // 빈 리스트 반환
	    }

	    return Arrays.stream(csv.split(","))
	                 .map(String::trim) // 공백 제거
	                 .collect(Collectors.toList());
	}
	
	// 숫자 리스트 변환 메소드
	private List<Integer> convertStringToIntegerList(String csv) {
	    if (csv == null || csv.isEmpty()) {
	        return List.of(); // 빈 리스트 반환
	    }

	    return Arrays.stream(csv.split(","))
	                 .map(String::trim) // 공백 제거
	                 .map(Integer::parseInt) // Integer로 변환
	                 .collect(Collectors.toList());
	}
	
	//리스트 문자열 변환 메소드
	private String convertListToString(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream()
                   .map(Object::toString) // 각 요소를 문자열로 변환
                   .collect(Collectors.joining(","));
    }
}
