package com.mph.beans;

import java.io.Serializable;

public class Board implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int ROWS_DEFAULT = 6;

	private static final int COLUMNS_DEFAULT = 7;

	private int rows;

	private int columns;

	private Square[][] squares;

	public Board() {

		rows = ROWS_DEFAULT;
		columns = COLUMNS_DEFAULT;

		squares = new Square[rows][columns];

		for(int i = 0; i < rows; i++) {

			for(int j = 0; j < columns; j++) {

				squares[i][j] = new Square(i, j);

			}

		}

	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public Square[][] getSquares() {
		return squares;
	}

	public void setSquares(Square[][] squares) {
		this.squares = squares;
	}

}