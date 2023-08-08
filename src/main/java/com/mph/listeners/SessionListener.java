package com.mph.listeners;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mph.services.interfaces.DisconnectionService;
import com.mph.services.DisconnectionServiceImpl;

import com.mph.beans.User;

public class SessionListener implements HttpSessionListener {

	private static final Logger logger = LogManager.getLogger(SessionListener.class);

	private DisconnectionService disconnectionService = new DisconnectionServiceImpl();

	@Override
	public void sessionCreated(HttpSessionEvent event) {

		HttpSession session = event.getSession();

		logger.info("Nouvelle session: " + session.getId());

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {	

		HttpSession session = event.getSession();
		User user = (User) session.getAttribute("user");

		if(user != null) {

			disconnectionService.disconnectUser(user);

			logger.info("Déconnection utilisateur: " + user.getLogin() + " | Fin de session: " + session.getId());

		} else
			logger.info("Fin de session: " + session.getId());

	}

}