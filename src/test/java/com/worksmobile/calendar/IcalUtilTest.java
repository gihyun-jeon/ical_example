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
		ZonedDateTime until = ZonedDateTime.of(LocalDateTime.parse("2016-06-30 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), zoneId);

		sut.parserIcal(ical_일반일정, null, null, zoneId);
		sut.parserIcal(ical_반복일정, from, until, zoneId);
		sut.parserIcal(ical_반복_예외가_n_개인_복잡한_일정, from, until, zoneId);
	}

	String ical_일반일정 = "BEGIN:VCALENDAR\n" +
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
			"CREATED:20151230T020745Z\n" +
			"END:VEVENT\n" +
			"END:VCALENDAR\n";

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

	String ical_반복_예외가_n_개인_복잡한_일정 = "BEGIN:VCALENDAR\n" +
			"PRODID:Works Mobile Calendar\n" +
			"VERSION:2.0\n" +
			"METHOD:PUBLISH\n" +
			"BEGIN:VEVENT\n" +
			"DTSTAMP:20160623T062643Z\n" +
			"UID:20160623T062411Z-14404@cvcweb03.wcal\n" +
			"ATTENDEE;CN=\"사용자3\";PARTSTAT=NEEDS-ACTION;ROLE=REQ-PARTICIPANT:mailto:uuuser3rd@nwetest.com\n" +
			"X-NAVER-REGISTERER;CUTYPE=INDIVIDUAL:\n" +
			"CREATED:20160623T062411Z\n" +
			"EXDATE;TZID=Asia/Seoul:20160624T150000\n" +
			"LAST-MODIFIED:20160623T062643Z\n" +
			"EXDATE;TZID=Asia/Seoul:20160621T150000\n" +
			"EXDATE;TZID=Asia/Seoul:20160620T150000\n" +
			"SUMMARY:반복 모일정.\n" +
			"DTSTART;TZID=Asia/Seoul:20160619T150000\n" +
			"DTEND;TZID=Asia/Seoul:20160619T160000\n" +
			"LOCATION:\n" +
			"CLASS:PUBLIC\n" +
			"DESCRIPTION:\n" +
			"PRIORITY:0\n" +
			"RRULE:FREQ=DAILY;UNTIL=20160625T150000Z;INTERVAL=1\n" +
			"ORGANIZER;CN=\"사용자2\":mailto:uuuser2nd@nwetest.com\n" +
			"X-NAVER-LAST-MODIFIER;CUTYPE=INDIVIDUAL:\n" +
			"X-NAVER-STICKER;X-WORKSMOBILE-POS=1:501\n" +
			"X-NAVER-CATEGORY-ID:0\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:EMAIL\n" +
			"END:VALARM\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:DISPLAY\n" +
			"END:VALARM\n" +
			"END:VEVENT\n" +
			"BEGIN:VEVENT\n" +
			"ATTENDEE;CN=\"사용자3\";PARTSTAT=NEEDS-ACTION;ROLE=REQ-PARTICIPANT:mailto:uuuser3rd@nwetest.com\n" +
			"RECURRENCE-ID;TZID=Asia/Seoul:20160621T150000\n" +
			"UID:20160623T062411Z-14404@cvcweb03.wcal\n" +
			"CREATED:20160623T062417Z\n" +
			"X-NAVER-REGISTERER;CUTYPE=INDIVIDUAL:\n" +
			"SUMMARY:반복 예외일정. + 이건 시간이 좀 더 깁니다.\n" +
			"DTSTART;TZID=Asia/Seoul:20160621T183000\n" +
			"DTEND;TZID=Asia/Seoul:20160621T203000\n" +
			"LOCATION:\n" +
			"CLASS:PUBLIC\n" +
			"DESCRIPTION:\n" +
			"PRIORITY:0\n" +
			"ORGANIZER;CN=\"사용자2\":mailto:uuuser2nd@nwetest.com\n" +
			"LAST-MODIFIED:20160623T062446Z\n" +
			"DTSTAMP:20160623T062446Z\n" +
			"X-NAVER-LAST-MODIFIER\\;CUTYPE=INDIVIDUAL:\n" +
			"X-NAVER-STICKER;X-WORKSMOBILE-POS=1:501\n" +
			"X-NAVER-CATEGORY-ID:0\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:EMAIL\n" +
			"END:VALARM\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:DISPLAY\n" +
			"END:VALARM\n" +
			"END:VEVENT\n" +
			"BEGIN:VEVENT\n" +
			"ATTENDEE;CN=\"사용자3\";PARTSTAT=NEEDS-ACTION;ROLE=REQ-PARTICIPANT:mailto:uuuser3rd@nwetest.com\n" +
			"RECURRENCE-ID;TZID=Asia/Seoul:20160624T150000\n" +
			"UID:20160623T062411Z-14404@cvcweb03.wcal\n" +
			"CREATED:20160623T062449Z\n" +
			"X-NAVER-REGISTERER;CUTYPE=INDIVIDUAL:\n" +
			"SUMMARY:반복 예외인데\\, 참석자 더 추가\n" +
			"DTSTART;TZID=Asia/Seoul:20160624T170000\n" +
			"DTEND;TZID=Asia/Seoul:20160624T180000\n" +
			"LOCATION:\n" +
			"CLASS:PUBLIC\n" +
			"DESCRIPTION:\n" +
			"PRIORITY:0\n" +
			"ORGANIZER;CN=\"사용자2\":mailto:uuuser2nd@nwetest.com\n" +
			"ATTENDEE;X-WORKSMOBILE-WID=100000372696001;CN=\"사용자3\";PARTSTAT=NEEDS-ACTION;ROLE=REQ-PARTICIPANT:mailto:uuuser7th@nwetest.com\n" +
			"LAST-MODIFIED:20160623T062510Z\n" +
			"DTSTAMP:20160623T062510Z\n" +
			"X-NAVER-LAST-MODIFIER;CUTYPE=INDIVIDUAL:\n" +
			"X-NAVER-STICKER;X-WORKSMOBILE-POS=1:501\n" +
			"X-NAVER-CATEGORY-ID:0\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:EMAIL\n" +
			"END:VALARM\n" +
			"BEGIN:VALARM\n" +
			"TRIGGER:-PT10M\n" +
			"ACTION:DISPLAY\n" +
			"END:VALARM\n" +
			"END:VEVENT\n" +
			"END:VCALENDAR";
}