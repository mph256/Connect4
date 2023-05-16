package com.mph.listeners;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import com.mph.services.interfaces.DisconnectionService;
import com.mph.services.DisconnectionServiceImpl;

public class SessionListener implements HttpSessionListener {

	private DisconnectionService disconnectionService;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		disconnectionService = new DisconnectionServiceImpl();
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {

		HttpSession session = event.getSession();
		String login = (String) session.getAttribute("login");

		if(login != null)
			disconnectionService.disconnectUser(login);

	}

}