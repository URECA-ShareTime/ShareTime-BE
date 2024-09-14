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
	public int delete(String id) throws SQLException {
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
	public User select(String id) throws SQLException {
		return dao.select(id);
	}

	@Override
	public List<User> selectAll() throws SQLException {
		return dao.selectAll();
	}

}