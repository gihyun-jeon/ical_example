package com.worksmobile.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.*;
import com.google.common.base.Strings;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IcalBuildUtil {
	public static String makeSampleScheduleIcal(String summary, ZoneId zoneId, ZonedDateTime startDateTime, Duration duration, String masterEmail, List<String> attendeeEmailList) {
		String ICAL_PRODUCT_ID = "-//YourCompany Inc//Some more info for Calendar 70.9054//EN"; //ICAL 을 생성자 정보

		ICalendar ical = new ICalendar();
		ical.setProductId(ICAL_PRODUCT_ID);

		VEvent event = new VEvent();

		// iCAL 의 UID는 client 에서 보장 해야 합니다.
		event.setUid(UUID.randomUUID().toString());


		// 제목
		if (Strings.isNullOrEmpty(summary)) {
			event.setSummary("Sample & summaryValue ");
		} else {
			event.setSummary(summary);
		}

		// 시작시간
		java.util.Date from = Date.from(startDateTime.withZoneSameLocal(zoneId).toInstant());
		DateStart dtStart = new DateStart(from, true);
		event.setDateStart(dtStart);

		// 종료시간
		java.util.Date until = Date.from(startDateTime.plus(duration).withZoneSameLocal(zoneId).toInstant());
		DateEnd dtEnd = new DateEnd(until, true);
		event.setDateEnd(dtEnd);

		// 마스터정보
		if (!Strings.isNullOrEmpty(masterEmail)) {
			event.setOrganizer(new Organizer(masterEmail.split("@")[0], masterEmail));
		}

		// 참석자정보
		if (!CollectionUtils.isEmpty(attendeeEmailList)) {
			for (String email : attendeeEmailList) {
				event.addAttendee(new Attendee(email.split("@")[0], email));
			}
		}

		// 수정시간
		event.setLastModified(new LastModified(new Date()));
		ical.addEvent(event);

		return Biweekly.write(ical).go();
	}

	@Test
	public void test() {
		String ical = makeSampleScheduleIcal("일정제목", ZoneId.of("Asia/Seoul"), ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES), Duration.ofHours(1), null, null);
		System.out.println(ical);
	}
}
