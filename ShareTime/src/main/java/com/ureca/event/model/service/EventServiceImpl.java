package com.ureca.event.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ureca.event.dto.Event;
import com.ureca.event.model.dao.EventDAO;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	EventDAO dao;

	@Override
	public Event insert(Event event) throws SQLException {
		// TODO Auto-generated method stub
		if (dao.insert(event) > 0) {
			return dao.select(event.getEvent_id());
		}
		return null;
	}

	@Override
	public int update(Event event) throws SQLException {
		// TODO Auto-generated method stub
		int id = event.getEvent_id();
		try {
            // 데이터베이스에서 해당 이벤트가 존재하는지 확인
            Event existingEvent = dao.select(id);
            if (existingEvent != null) {
                // 업데이트할 이벤트의 ID 설정
                event.setEvent_id(id);

                // DAO를 통해 이벤트 업데이트 실행
                int rowsUpdated = dao.update(event);
                
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
		return dao.delete(id);
	}

	@Override
	public Event select(int id) throws SQLException {
		// TODO Auto-generated method stub
		return dao.select(id);
	}

	@Override
	public List<Event> selectAll() throws SQLException {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}
	
}
