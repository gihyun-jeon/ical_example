package com.worksmobile.calendar;

import org.junit.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class IcalBuilderTest {
	IcalBuilder sut = new IcalBuilder();

	@Test
	public void test() {
		String actual = sut.makeSampleScheduleIcal("일정제목", ZoneId.of("Asia/Seoul"), ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES), Duration.ofHours(1), null, null);
		System.out.println(actual);
	}

}