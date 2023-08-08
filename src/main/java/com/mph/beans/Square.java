package com.mph.beans;

import java.io.Serializable;

public class Square implements Serializable {

	private static final long serialVersionUID = 1L;

	private int row;

	private int column;

	private Token token;

	public Square() {
	}

	public Square(int row, int column) {

		this.row = row;
		this.column = column;

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

}