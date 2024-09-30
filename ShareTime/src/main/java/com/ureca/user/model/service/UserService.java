package com.ureca.user.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ureca.user.dto.User;

public interface UserService {
    // 명세 => User 추가/수정/삭제/조회/모두조회
    public int insert(User user) throws SQLException;

    public int update(User user) throws SQLException;

    public int delete(int id) throws SQLException; // String에서 int로 수정

    public User login(User user) throws SQLException;

    public User select(int id) throws SQLException; // String에서 int로 수정

    public List<User> selectAll() throws SQLException;
    
    // 클래스 멤버 조회 메서드 추가
    List<User> selectUsersByClassId(int classId) throws SQLException;
    
 // 사용자가 참여 중인 스터디 리스트를 조회하는 메서드 추가
    List<Map<String, Object>> getStudyListByUserId(int userId) throws SQLException;
    
 // 스터디 ID로 사용자 목록 조회
    List<User> selectUsersByStudyId(int studyId) throws SQLException;
    
}