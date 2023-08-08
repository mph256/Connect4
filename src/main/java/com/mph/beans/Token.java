package com.mph.beans;

import java.io.Serializable;

public class Token implements Serializable {

	private static final long serialVersionUID = 1L;

	private Color color;

	public Token() {
	}

	public Token(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}