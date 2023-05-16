package com.mph.util;

import java.util.List;
import java.util.ArrayList;

import java.util.Properties;

import java.io.File;
import java.io.IOException;

import java.net.URL;

public final class Util {

	private static final String PROPERTIES_FILE = "config.properties";

	private Util() {
	}

	public static List<String> getProperties(String[] propertyNames) {

		List<String> propertyValues = new ArrayList<String>();

		Properties properties = new Properties();

		try {

			properties.load(Util.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));

			for(String propertyName: propertyNames) {
				propertyValues.add(properties.getProperty(propertyName));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return propertyValues;

	}

	public static File getResourceFile(String fileName) {

		URL url = Util.class.getClassLoader().getResource(fileName);

		if(url == null)
			throw new IllegalArgumentException();

		return new File(url.getFile());

	}

}