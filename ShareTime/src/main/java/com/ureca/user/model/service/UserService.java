package com.ureca.user.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ureca.user.dto.User;

public interface UserService {
    // 명세 => User 추가/수정/삭제/조회/모두조회
    public int insert(User user) throws SQLException;

    public int update(User user) throws SQLException;

    public int delete(int id) throws SQLException; // String에서 int로 수정

    public User login(User user) throws SQLException;

    public User select(int id) throws SQLException; // String에서 int로 수정

    public List<User> selectAll() throws SQLException;
}