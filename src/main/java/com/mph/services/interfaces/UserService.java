package com.mph.services.interfaces;

import java.util.Set;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;
import com.mph.services.exceptions.InvalidPasswordException;

import com.mph.beans.User;

public interface UserService {

	/**
	 * Creates a new user.
	 * 
	 * <br>
	 * Argument validity checks must be performed before this method is called.
	 * 
	 * @param login the login entered
	 * @param email the email entered
	 * @param password the password entered
	 * 
	 * @return the new user created
	 */
	public User createUser(String login, String email, String password);

	/**
	 * Updates an existing user.
	 * 
	 * <br>
	 * The user given in argument must already be up to date.
	 * <br>
	 * The list of fields indicates which fields need to be updated in the database.
	 * 
	 * @param user the user to update
	 * @param fields the list of user fields to update
	 */
	public void updateUser(User user, Set<String> fields);

	/**
	 * Updates an existing user.
	 * 
	 * @param user the user to update
	 * @param email the email entered by the user
	 * @param password the password entered by the user
	 * @param passwordConfirmation the password confirmation entered by the user
	 * 
	 * @throws InvalidEmailFormatException if the format of the entered email is not correct
	 * @throws EmailAlreadyInUseException if the entered email is already in use
	 * @throws InvalidPasswordFormatException if the format of the entered password is not correct
	 * @throws InvalidPasswordConfirmationException if the entered passwords do not match
	 */
	public void updateUser(User user, String email, String password, String passwordConfirmation) throws InvalidEmailFormatException, EmailAlreadyInUseException,
		InvalidPasswordFormatException, InvalidPasswordConfirmationException;

	/**
	 * Deletes an existing user.
	 * 
	 * @param user the user to delete
	 */
	public void deleteUser(User user);

	/**
	 * Checks the validity of the login entered during a registration attempt.
	 * 
	 * @param login the login entered
	 * 
	 * @throws InvalidLoginFormatException if the format of the entered login is not correct
	 * @throws LoginAlreadyInUseException if the entered login is already in use
	 */
	public void checkNewLogin(String login) throws InvalidLoginFormatException, LoginAlreadyInUseException;

	/**
	 * Checks the validity of the email entered during a registration attempt.
	 * 
	 * @param email the email entered
	 * 
	 * @throws InvalidEmailFormatException if the format of the entered email is not correct
	 * @throws EmailAlreadyInUseException if the entered email is already in use
	 */
	public void checkNewEmail(String email) throws InvalidEmailFormatException, EmailAlreadyInUseException;

	/**
	 * Checks the validity of the password entered during a registration attempt.
	 * 
	 * @param password the password entered
	 * @param passwordConfirmation the password confirmation entered
	 * 
	 * @throws InvalidPasswordFormatException if the format of the entered password is not correct
	 * @throws InvalidPasswordConfirmationException if the entered passwords do not match
	 */
	public void checkNewPassword(String password, String passwordConfirmation) throws InvalidPasswordFormatException, InvalidPasswordConfirmationException;

	/**
	 * Checks the validity of the login entered during a connection attempt.
	 * 
	 * @param user the user
	 * 
	 * @throws InvalidLoginException if no user with this login is found
	 */
	public void checkLogin(User user) throws InvalidLoginException;

	/**
	 * Checks the validity of the password entered during a connection attempt.
	 * 
	 * @param user the user
	 * @param password the password entered by the user
	 * 
	 * @throws InvalidPasswordException if the entered password does not match the one registered for this login
	 */
	public void checkPassword(User user, String password) throws InvalidPasswordException;

	public User getUserByLogin(String login);

	public User getUserByEmail(String email);

	public Set<User> getOnlineUsers();

	public Set<User> getUsers();

}