package com.mph.services;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.regex.Pattern;

import com.mph.services.interfaces.UserService;

import com.mph.services.exceptions.InvalidLoginFormatException;
import com.mph.services.exceptions.LoginAlreadyInUseException;
import com.mph.services.exceptions.InvalidLoginException;
import com.mph.services.exceptions.InvalidEmailFormatException;
import com.mph.services.exceptions.EmailAlreadyInUseException;
import com.mph.services.exceptions.InvalidPasswordFormatException;
import com.mph.services.exceptions.InvalidPasswordConfirmationException;
import com.mph.services.exceptions.InvalidPasswordException;

import com.mph.dao.DaoFactory;
import com.mph.dao.interfaces.UserDao;

import com.mph.beans.User;

public class UserServiceImpl implements UserService {

	private static final String EMAIL_REGEX = "^(?=.{1,64}@)[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@[^-][a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$";

	private static Map<String, User> onlineUsers;

	private UserDao userDao;

	static {
		onlineUsers = new ConcurrentHashMap<String, User>();
	}

	public UserServiceImpl() {
		userDao = DaoFactory.getInstance().getUserDao();
	}

	@Override
	public User createUser(String login, String email, String password) {

		User user = new User(login, email, password);

		userDao.add(user);

		onlineUsers.put(login, user);

		return user;

	}

	@Override
	public void updateUser(User user, Set<String> fields) {

		userDao.update(user, fields);

		String login = user.getLogin();

		if(user.isOnline())
			onlineUsers.put(login, user);
		else
			onlineUsers.remove(login);

	}

	@Override
	public void updateUser(User user, String email, String password, String passwordConfirmation) throws InvalidEmailFormatException, EmailAlreadyInUseException,
		InvalidPasswordFormatException, InvalidPasswordConfirmationException {

		Set<String> fields = new HashSet<String>();

		boolean isNewEmail = !email.equals(user.getEmail());
		boolean isNewPassword = !password.equals(user.getPassword());

		if(isNewEmail)
			checkNewEmail(email);

		if(isNewPassword)
			checkNewPassword(password, passwordConfirmation);

		if(isNewEmail) {

			fields.add("email");
			user.setEmail(email);

		}

		if(isNewPassword) {

			fields.add("password");
			user.setPassword(password);

		}

		if(!fields.isEmpty())
			userDao.update(user, fields);

	}

	@Override
	public void deleteUser(User user) {

		userDao.delete(user);

		onlineUsers.remove(user.getLogin());

		user.getFriends().forEach(x -> x.removeFriend(user));
		user.getOnlineFriends().forEach(x -> x.removeOnlineFriend(user));

		user.getProfilePicture().delete();

	}

	@Override
	public User getUserByLogin(String login) {

		User user = null;

		if(onlineUsers.containsKey(login)) 
			user = onlineUsers.get(login);
		else
			user = userDao.findByLogin(login);

		return user;

	}

	@Override
	public User getUserByEmail(String email) {

		User user = onlineUsers.values()
			.stream()
			.filter(x -> email.equals(x.getEmail()))
			.findFirst()
			.orElse(null);

		if(user == null)
			user = userDao.findByEmail(email);

		return user;

	}

	@Override
	public Set<User> getOnlineUsers() {
		return new TreeSet<User>(onlineUsers.values());
	}

	@Override
	public Set<User> getUsers() {

		Set<User> users = new TreeSet<User>();

		for(User user: userDao.findAll()) {

			String login = user.getLogin();

			if(onlineUsers.containsKey(login))
				users.add(onlineUsers.get(login));
			else
				users.add(user);

		}

		return users;

	}

	@Override
	public void checkNewLogin(String login) throws InvalidLoginFormatException, LoginAlreadyInUseException {

		if(login.length() < 6 || login.length() > 30)
			throw new InvalidLoginFormatException();

		if(isLoginAlreadyInUse(login))
			throw new LoginAlreadyInUseException();

	}

	@Override
	public void checkNewEmail(String email) throws InvalidEmailFormatException, EmailAlreadyInUseException {

		if(!isEmail(email))
			throw new InvalidEmailFormatException();

		if(isEmailAlreadyInUse(email))
			throw new EmailAlreadyInUseException();

	}

	@Override
	public void checkNewPassword(String password, String passwordConfirmation) throws InvalidPasswordFormatException, InvalidPasswordConfirmationException {

		if(password.length() < 6 || password.length() > 30)
			throw new InvalidPasswordFormatException();

		if(!password.equals(passwordConfirmation))
			throw new InvalidPasswordConfirmationException();

	}

	@Override
	public void checkLogin(User user) throws InvalidLoginException {

		if(user == null)
			throw new InvalidLoginException();

	}

	@Override
	public void checkPassword(User user, String password) throws InvalidPasswordException {

		if(!password.equals(user.getPassword()))
			throw new InvalidPasswordException();

	}

	private boolean isLoginAlreadyInUse(String login) {

		User user = getUserByLogin(login);

		return (user != null);

	}

	private boolean isEmail(String email) {

		return Pattern.matches(EMAIL_REGEX, email);

	}

	private boolean isEmailAlreadyInUse(String email) {

		User user = getUserByEmail(email);

		return (user != null);

	}

}