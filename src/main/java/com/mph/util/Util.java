package com.mph.util;

import java.util.List;
import java.util.ArrayList;

import java.util.Properties;

import java.util.regex.Pattern;

import java.io.File;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.net.URL;

import javax.servlet.http.Part;

public final class Util {

	private static final String PROPERTIES_FILE = "config.properties";

	private static final String IMG_PATH = "/assets/img";

	private static final String IMG_REGEX = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

	private Util() {
	}

	public static void createDir(String fullPath) {

		File dir = new File(fullPath);

		if(!dir.exists())
			dir.mkdir();

	}

	public static File createDefaultProfilePicture(String name) {

		String path = getRealImgPath() + "/users";

		createDir(path);

		File profilePicture = new File(path + "/" + name + ".png");

		copyFile(getResourceFile(getProperties(new String[]{ "img.default.profile" }).get(0)), profilePicture);

		return profilePicture;

	}

	public static File updateProfilePicture(Part part, String name) {

		String path = getRealImgPath() + "/users";

		File profilePicture = new File(path + "/" + name + getFileExtension(part));

		copyPartToFile(part, profilePicture);

		return profilePicture;

	}

	public static void copyFile(File src, File dest) {

		try {
			Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void copyPartToFile(Part part, File file) {

		try {
			part.write(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean isImage(String name) {
		return Pattern.matches(IMG_REGEX, name);
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

	public static File getResourceFile(String name) {

		URL url = Util.class.getClassLoader().getResource(name);

		if(url == null)
			throw new IllegalArgumentException();

		return new File(url.getFile());

	}

	public static String getFileExtension(Part part) {

		for(String content: part.getHeader("content-disposition").split(";")) {

			if(content.trim().startsWith("filename"))
				return content.substring(content.lastIndexOf("."), content.length() - 1);

		}

		return null;

	}

	public static String getRealImgPath() {

		return Util.class.getResource("/").getPath()
			.replace("/WEB-INF/classes/", "") 
			+ IMG_PATH;

	}

	public static File getProfilePictureByLogin(String login) {

		File profilePicture = null;

		String path = getRealImgPath() + "/users";

		createDir(path);

		File dir = new File(path);
		File[] files = dir.listFiles();

		for(File file : files){

			String name = file.getName();

			if(isImage(name) && login.equals(name.substring(0, name.lastIndexOf(".")))) {

				profilePicture = file;
				break;

			}

		}

		if(profilePicture == null)
			profilePicture = createDefaultProfilePicture(login);
 
		return profilePicture;

	}

}