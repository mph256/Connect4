package com.mph.services.interfaces;

import java.util.Set;

import com.mph.beans.User;

public interface FriendService {

	/**
	 * Removes a friend.
	 *
	 *<br>
	 * Cancels the game request sent to this user or received by this user (if it exists).
	 *
	 * @param user the user
	 * @param friend the friend to delete
	 */
	public void removeFriend(User user, User friend);

	public Set<User> getFriendsByUser(User user);

}