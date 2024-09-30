package com.ureca.user.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.ureca.user.dto.User;

@Mapper
public interface UserDAO {


	public int insert(User user);  // 회원가입 처리

	public int update(User user) throws SQLException;

    public int delete(int id) throws SQLException; // 파라미터 타입이 int로 일치

	public User login(User user) throws SQLException;

	public User select(int id) throws SQLException;// 특정 Person 한명 조회 => 수정폼/상세페이지

	public List<User> selectAll() throws SQLException;// 모든 Person 조회

    List<User> selectUsersByClassId(@Param("class_id") int classId) throws SQLException;
    
 // 사용자가 참여 중인 스터디 리스트를 조회하는 메서드
    List<Map<String, Object>> selectStudyListByUserId(@Param("user_id") int userId) throws SQLException;
    
    // 스터디 ID로 사용자 목록을 조회하는 메서드
    List<User> selectUsersByStudyId(@Param("study_id") int studyId) throws SQLException;
   }