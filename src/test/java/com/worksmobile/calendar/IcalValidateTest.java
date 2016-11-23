package com.worksmobile.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.ValidationWarnings;
import org.junit.Test;

import java.util.List;

public class IcalValidateTest {

	@Test
	public void test() {
		List<ICalendar> icalList = Biweekly.parse(icalString).all();
		icalList.forEach(
				iCalendar -> {
					ValidationWarnings validationWarnings = iCalendar.validate(iCalendar.getVersion());
					validationWarnings.forEach(warn -> System.out.println(warn));
				}
		);
	}

	String icalString = "BEGIN:VCALENDAR\n" +
			"VERSION:2.0\n" +
			"PRODID:Naver Calendar\n" +
			"CALSCALE:GREGORIAN\n" +
			"BEGIN:VTIMEZONE\n" +
			"TZID:Asia/Seoul\n" +
			"END:VEVENT\n" +
			"END:VCALENDAR";


}