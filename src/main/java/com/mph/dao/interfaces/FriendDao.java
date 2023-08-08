package com.mph.dao.interfaces;

import java.util.Set;

import com.mph.beans.User;

public interface FriendDao {

	public void delete(User user, User friend);

	public Set<User> findByUser(User user);

}