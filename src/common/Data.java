package common;

import java.io.Serializable;

public class Data implements Serializable {
	private int year;
	private int month;
	private int day;

	public Data() {
	}

	public Data(int year, int month, int day) {
		setYear(year);
		setMonth(month);
		setDay(day);
	}

	// Getters and Setters
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String toString() {
		return (getDay() + "/" + getMonth() + "/" + getYear());
	}
}
