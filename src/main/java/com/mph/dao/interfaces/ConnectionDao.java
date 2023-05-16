package com.mph.dao.interfaces;

import com.mph.beans.User;

public interface ConnectionDao {

	public boolean isRegisteredUser(String login);

	public boolean isCorrectPassword(String login, String password);

	public void connectUser(User user);

}