package com.mph.services.interfaces;

import com.mph.beans.User;

public interface DisconnectionService {

	/**
	 * Disconnects an existing user.
	 *
	 * <br>
	 * Cancels his game requests sent and declines his game requests received.
	 *
	 * @param user the connected user to disconnect
	 */
	public void disconnectUser(User user);

}