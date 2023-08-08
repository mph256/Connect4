package com.mph.listeners;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContextListener implements ServletContextListener {

	private static final Logger logger = LogManager.getLogger(ContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("Application démarrée");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Application stoppée");
	}

}