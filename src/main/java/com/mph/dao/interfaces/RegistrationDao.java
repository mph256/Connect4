package com.mph.dao.interfaces;

import com.mph.beans.User;

public interface RegistrationDao {

	public boolean isLoginAlreadyInUse(String login);

	public boolean isEmailAlreadyInUse(String email);

	public void registerUser(User user);

}