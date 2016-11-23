package com.worksmobile.calendar;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IcalValidateByIcal4jTest {

	@Test
	public void test() throws IOException, ParserException {
		CalendarBuilder builder = new CalendarBuilder();
		InputStream in = new ByteArrayInputStream(icalString.getBytes());
		Calendar calendar = builder.build(in);
		System.out.println(calendar);
	}

	String icalString = "BEGIN:VCALENDAR\n" +
			"VERSION:2.0\n" +
			"PRODID:Naver Calendar\n" +
			"CALSCALE:GREGORIAN\n" +
			"BEGIN:VTIMEZONE\n" +
			"TZID:Asia/Seoul\n" +
			"BEGIN:STANDARD\n" +
			"DTSTART:19700101T000000\n" +
			"TZNAME:GMT+:00\n" +
			"TZOFFSETFROM:+00\n" +
			"TZOFFSETTO:+00\n" +
			"END:STANDARD\n" +
			"END:VTIMEZONE\n" +
			"BEGIN:VEVENT\n" +
			"SEQUENCE:0\n" +
			"CLASS:PUBLIC\n" +
			"TRANSP:OPAQUE\n" +
			"UID:AAAANtfvtM2m1Kz\n" +
			"DTSTART;TZID=Asia/Seoul:20161116T190000\n" +
			"DTEND;TZID=Asia/Seoul:20161116T193000\n" +
			"SUMMARY:[제목] 캘린더API로 추가한 일정 \n" +
			"DESCRIPTION:[상세] 회의 합니다 \n" +
			"LOCATION:[장소] 그린팩토리 \n" +
			"CREATED:20161116T160000\n" +
			"LAST-MODIFIED:20161122T160000\n" +
			"DTSTAMP:20161122T160000\n" +
			"END:VEVENT\n" +
			"END:VCALENDAR";
}