package com.ureca.user.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ureca.user.dto.User;
import com.ureca.user.model.dao.UserDAO;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO dao;

    @Override
    public int insert(User user) throws SQLException {
        return dao.insert(user);
    }

    @Override
    public int update(User user) throws SQLException {
        return dao.update(user);
    }

    @Override
    public int delete(int id) throws SQLException { // 수정된 메서드 시그니처
        return dao.delete(id);
    }

    @Override
    public User login(User user) throws SQLException {
        User loginResult = dao.login(user);
        if (loginResult != null) {
            return loginResult;
        }
        return null;
    }

    @Override
    public User select(int id) throws SQLException { // 수정된 메서드 시그니처
        return dao.select(id);
    }

    @Override
    public List<User> selectAll() throws SQLException {
        return dao.selectAll();
    }
    
    @Override
    public List<User> selectUsersByClassId(int classId) throws SQLException {
        return dao.selectUsersByClassId(classId);
    }
    
    @Override
    public List<String> getStudyListByUserId(int userId) throws SQLException {
        return dao.selectStudyListByUserId(userId);
    }
}