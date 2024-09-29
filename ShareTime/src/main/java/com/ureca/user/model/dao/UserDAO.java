package com.ureca.user.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ureca.user.dto.User;

@Mapper
public interface UserDAO {

	public int insert(User user);  // 회원가입 처리

	public int update(User user) throws SQLException;

    public int delete(int id) throws SQLException; // 파라미터 타입이 int로 일치

	public User login(User user) throws SQLException;

	public User select(int id) throws SQLException;// 특정 Person 한명 조회 => 수정폼/상세페이지

	public List<User> selectAll() throws SQLException;// 모든 Person 조회


}