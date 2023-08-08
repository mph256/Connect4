package com.mph.dao.interfaces;

import java.util.Set;

import com.mph.beans.User;

public interface UserDao {

	public void add(User user);

	public void update(User user, Set<String> fields);

	public void delete(User user);

	public User findByLogin(String login);

	public User findByEmail(String email);

	public Set<User> findAll();

}