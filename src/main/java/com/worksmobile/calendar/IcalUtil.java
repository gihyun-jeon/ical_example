package com.worksmobile.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.ExceptionDates;
import biweekly.property.RecurrenceRule;
import biweekly.util.ICalDate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.ical.compat.javautil.DateIterator;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IcalUtil {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final int MAX_REPEAT_COUNT = 100;

	/**
	 * @param icalString parsing 대상 iCal
	 * @param from       parsing 범위 시작일
	 * @param until      parsing 범위 종료일
	 * @param zoneId     parsing 기준 zoneId
	 */
	public void parserIcal(String icalString, ZonedDateTime from, ZonedDateTime until, ZoneId zoneId) {
		List<ICalendar> icalList = Biweekly.parse(icalString).all();
		System.out.println("iCAl 에 포함된 CalendarItem size=" + icalList.size());
		List<Map<String, String>> repeatDateMapList = Lists.newArrayList();

		for (ICalendar iCalendar : icalList) {
			boolean isRepeat = false;
			for (VEvent vEvent : iCalendar.getEvents()) {
				if (null != vEvent.getRecurrenceRule()) {
					isRepeat = true;
				}
			}


			// 예외 날짜 추출
			List<ICalDate> exceptionDatesList = new ArrayList<ICalDate>();
			for (VEvent vEvent : iCalendar.getEvents()) {
				List<ExceptionDates> exceptionDates = vEvent.getExceptionDates();
				if (CollectionUtils.isEmpty(exceptionDates)) {
					continue;
				}
				for (ExceptionDates exds : exceptionDates) {
					if (null == exds.getValues()) {
						continue;
					}
					for (ICalDate exICalDate : exds.getValues()) {
						exceptionDatesList.add(exICalDate);
					}
				}
			}

			for (VEvent vEvent : iCalendar.getEvents()) {
				RecurrenceRule recurrenceRule = vEvent.getRecurrenceRule();
				if (null == recurrenceRule) {
					// 반복예외일정이니 따로 처리
					ZonedDateTime startDateTime = ZonedDateTime.ofInstant(vEvent.getDateStart().getValue().toInstant(), zoneId);
					ZonedDateTime endDateTime = ZonedDateTime.ofInstant(vEvent.getDateEnd().getValue().toInstant(), zoneId);
					if (startDateTime.isAfter(from) && endDateTime.isBefore(until)) {
						Map<String, String> repeatMap = Maps.newHashMap();
						repeatMap.put("name", vEvent.getSummary().getValue());
						repeatMap.put("startDate", startDateTime.format(DATE_TIME_FORMATTER).toString());
						repeatMap.put("endDate", endDateTime.format(DATE_TIME_FORMATTER).toString());
						repeatDateMapList.add(repeatMap);
					}
					continue;
				}

				ICalDate recurrenceStart = vEvent.getDateStart().getValue();
				DateIterator it = recurrenceRule.getDateIterator(recurrenceStart);

				int repeatCount = 0;
				while (it.hasNext()) {
					if (repeatCount++ > MAX_REPEAT_COUNT) {
						break;
					}

					ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(it.next().toInstant(), zoneId);
					if (currentDateTime.isBefore(from)) {
						continue;
					}
					if (currentDateTime.isAfter(until)) {
						break;
					}

					boolean isExDate = false;
					for (ICalDate exICalDate : exceptionDatesList) {
						ZonedDateTime exDateTime = ZonedDateTime.ofInstant(exICalDate.toInstant(), zoneId);
						if (exDateTime.toLocalDate().equals(currentDateTime.toLocalDate())) {
							isExDate = true;
						}
					}
					if (isExDate) {
						System.out.println("반복예외 날짜임으로 pass currentDateTime=" + currentDateTime.toString());
						continue;
					}

					ICalDate st = vEvent.getDateStart().getValue();
					ICalDate en = vEvent.getDateStart().getValue();
					long duration = en.toInstant().toEpochMilli() - st.toInstant().toEpochMilli();

					ZonedDateTime startDateTime = currentDateTime;
					ZonedDateTime endDateTime = startDateTime.plusSeconds(duration);

					Map<String, String> repeatMap = Maps.newHashMap();
					repeatMap.put("name", vEvent.getSummary().getValue());
					repeatMap.put("startDate", startDateTime.format(DATE_TIME_FORMATTER).toString());
					repeatMap.put("endDate", endDateTime.format(DATE_TIME_FORMATTER).toString());
					repeatDateMapList.add(repeatMap);
				}

			}

			for (VEvent vEvent : iCalendar.getEvents()) {
				System.out.println("일정제목=" + vEvent.getSummary().getValue());
			}
		}
	}

}
