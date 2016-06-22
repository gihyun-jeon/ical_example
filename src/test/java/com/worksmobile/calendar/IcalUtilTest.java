package com.worksmobile.calendar;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class IcalUtilTest {
	private IcalUtil sut = new IcalUtil();

	@Test
	public void test_parse() {
		ZoneId zoneId = ZoneId.of("Asia/Seoul");
		ZonedDateTime from = ZonedDateTime.of(LocalDateTime.parse("2016-04-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), zoneId);
		ZonedDateTime until = ZonedDateTime.of(LocalDateTime.parse("2016-04-30 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), zoneId);

		sut.parserIcal(ical_반복일정, from, until, zoneId);
	}

	String ical_반복일정 = "BEGIN:VCALENDAR\n" +
			"PRODID:Works Mobile Calendar\n" +
			"VERSION:2.0\n" +
			"BEGIN:VEVENT\n" +
			"DTSTAMP:20151230T020745Z\n" +
			"UID:20151230T020745Z-44624@somekey\n" +
			"SUMMARY:경영지원개발실\n" +
			"DTSTART;TZID=Asia/Seoul:20160104T103000\n" +
			"DTEND;TZID=Asia/Seoul:20160104T120000\n" +
			"LOCATION:그린팩토리\n" +
			"CLASS:PUBLIC\n" +
			"DESCRIPTION:회의합니다.\\n감사합니다.\n" +
			"PRIORITY:0\n" +
			"RRULE:FREQ=WEEKLY;UNTIL=20161231T150000Z;INTERVAL=1;BYDAY=MO\n" +
			"ORGANIZER;X-WORKSMOBILE-WID=123;CN=\"주최자\":mailto:some@navercorp.com\n" +
			"ATTENDEE;X-WORKSMOBILE-WID=222;CN=\"참석자1\";PARTSTAT=NEEDS-ACTION;ROLE=REQ-PARTICIPANT:somsom@navercorp.com\n" +
			"ATTENDEE;X-WORKSMOBILE-WID=223;CN=\"참석자2\";PARTSTAT=NEEDS-ACTION;ROLE=REQ-PARTICIPANT:somsom2@navercorp.com\n" +
			"CREATED:20151230T020745Z\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:DISPLAY\n" +
			"END:VALARM\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:EMAIL\n" +
			"END:VALARM\n" +
			"END:VEVENT\n" +
			"END:VCALENDAR\n";
}