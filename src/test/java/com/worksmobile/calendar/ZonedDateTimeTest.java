package com.worksmobile.calendar;

import org.junit.Test;

import java.time.ZonedDateTime;

public class ZonedDateTimeTest {

	@Test
	public void test() {
		ZonedDateTime today = ZonedDateTime.now();

		System.out.println(today.toLocalDateTime());
		System.out.println(today.toInstant().toString());
	}
}
