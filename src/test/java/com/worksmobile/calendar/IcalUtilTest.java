package com.worksmobile.calendar;

import org.junit.Test;

public class IcalUtilTest {
	private IcalUtil sut = new IcalUtil();

	@Test
	public void test_parse() {
		sut.parserIcal("ical");
	}

}