package ca.bc.gov.mal.cirras.claims.persistence.v1.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String MAX_DATE_TIME_STRING = "9999-12-31 00:00:00";
	public static final String MAX_DATE_STRING = "9999-12-31";

	public static LocalDateTime toLocalDateTime(String dateTimeString) {
		LocalDateTime result = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

		if (dateTimeString == null || dateTimeString.trim().length() == 0) { return null; }

		result = LocalDateTime.parse(dateTimeString, formatter);

		return result;
	}

	public static LocalDate toLocalDate(String dateString) {
		LocalDate result = null;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		if (dateString == null || dateString.trim().length() == 0) { return null; }

		result = LocalDate.parse(dateString, formatter);

		return result;
	}

	public static String fromLocalDate(LocalDate dateTime) {
		String result = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

		if(dateTime == null) { return null; }

		result = dateTime.format(formatter);

		return result;
	}

	public static String fromLocalDateTime(LocalDateTime dateTime) {
		String result = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

		if(dateTime == null) { return null; }

		result = dateTime.format(formatter);

		return result;
	}

	public static LocalDateTime genMaxLocalDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

		LocalDateTime result = LocalDateTime.parse(MAX_DATE_TIME_STRING, formatter);

		return result;
	}

	public static LocalDate genMaxLocalDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

		LocalDate result = LocalDate.parse(MAX_DATE_STRING, formatter);

		return result;
	}

}
