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

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class IcalParse {
	private static final int MAX_REPEAT_COUNT = 1000;

	/**
	 * @param icalString parsing 대상 iCal
	 * @param from       parsing 범위 시작일
	 * @param until      parsing 범위 종료일
	 * @param zoneId     parsing 기준 zoneId
	 */
	public void parserIcal(String icalString, ZonedDateTime from, ZonedDateTime until, ZoneId zoneId) {
		List<ICalendar> icalList = Biweekly.parse(icalString).all();

		// 하나의 iCalendar 안에, n개의 VEVENT 가 있을 수 있습니다.
		// 반복모일정 + 자일정 일 수도 있고, 전혀 관계없는 VEVENT 일 수도 있습니다.
		// 먼저 UID 로 GROUPING 합니다.
		Map<String, List<VEvent>> veventMap = Maps.newHashMap();
		for (ICalendar iCalendar : icalList) {
			for (VEvent ve : iCalendar.getEvents()) {
				String key = ve.getUid().getValue();
				if (veventMap.containsKey(key)) {
					veventMap.get(key).add(ve);
				} else {
					veventMap.put(key, Lists.newArrayList(ve));
				}
			}
		}

		for (Map.Entry<String, List<VEvent>> entry : veventMap.entrySet()) {
			System.out.println("\nUID" + entry.getKey() + " VEVENT List 를 parsing 합니다.");
			List<SimpleScheduleModel> simpleScheduleModelList = parseSingleUidVeventList(from, until, zoneId, entry.getValue());
			printResult(simpleScheduleModelList);
		}
	}


	private void printResult(List<SimpleScheduleModel> simpleScheduleModelList) {
		for (SimpleScheduleModel simpleScheduleModel : simpleScheduleModelList) {
			System.out.println(simpleScheduleModel.toString());
		}
	}


	private List<SimpleScheduleModel> parseSingleUidVeventList(ZonedDateTime from, ZonedDateTime until, ZoneId zoneId, List<VEvent> vEventList) {
		if (null == zoneId || CollectionUtils.isEmpty(vEventList)) {
			throw new IllegalArgumentException("zoneId 와 vEventList 는 필수값 입니다.");
		}

		validateVeventList(vEventList);

		boolean hasRecurrenceRule = false;
		for (VEvent vEvent : vEventList) {
			if (null != vEvent.getRecurrenceRule()) {
				hasRecurrenceRule = true;
			}
		}

		if (hasRecurrenceRule) {
			// 반복일정
			return parseRepeatVEvent(vEventList, from, until, zoneId);
		} else {
			// 비반복 일정들
			return parseNotRepeatVeventList(vEventList, zoneId);
		}
	}


	private void validateVeventList(List<VEvent> vEventList) {
		String uid = vEventList.get(0).getUid().getValue();
		for (VEvent vEvent : vEventList) {
			if (!uid.equals(vEvent.getUid().getValue())) {
				throw new IllegalArgumentException("vEventList's UID is not same!");
			}
		}
	}


	/**
	 * @param vEventList 비반복일정 1개 일 수도 있고, 반복예외일정만 n 개일 수도 있음.
	 * @param zoneId     client 가 Viewing 하기 원하는 timezone
	 * @return
	 */
	private List<SimpleScheduleModel> parseNotRepeatVeventList(List<VEvent> vEventList, ZoneId zoneId) {
		List<SimpleScheduleModel> simpleScheduleModelList = Lists.newArrayList();
		for (VEvent vEvent : vEventList) {
			simpleScheduleModelList.add(SimpleScheduleModel.of(vEvent, zoneId));
		}
		return simpleScheduleModelList;
	}


	private List<SimpleScheduleModel> parseRepeatVEvent(List<VEvent> 반복_모일정과_반복예외_일정리스트, ZonedDateTime from, ZonedDateTime until, ZoneId zoneId) {
		List<SimpleScheduleModel> simpleScheduleModelList = Lists.newArrayList();
		if (null == from || null == until) {
			throw new IllegalArgumentException("반복일정을 parsing 하기 위해서는, 범위가 필요합니다.");
		}

		// 반복 예외날짜 추출
		// Server 에 따라, EXDATE 없이 반복예외를 처리 할 수도 있으니, 주의
		List<ICalDate> exceptionDatesList = Lists.newArrayList();
		for (VEvent vEvent : 반복_모일정과_반복예외_일정리스트) {
			List<ExceptionDates> exceptionDates = vEvent.getExceptionDates();
			if (CollectionUtils.isEmpty(exceptionDates)) {
				continue;
			}
			for (ExceptionDates exds : exceptionDates) {
				if (null != exds.getValues()) {
					exceptionDatesList.addAll(exds.getValues());
				}
			}
		}

		for (VEvent vEvent : 반복_모일정과_반복예외_일정리스트) {
			RecurrenceRule recurrenceRule = vEvent.getRecurrenceRule();
			if (null == recurrenceRule) { //반복일정인데, RRULE 이 없으면 반복예외일정입니다.
				buildSingleScheduleModelForViewFromExVEvent(from, until, zoneId, simpleScheduleModelList, vEvent);
				continue;
			}

			// 반복 모일정이 기간을 저장
			Duration vEventDuration = Duration.between(vEvent.getDateStart().getValue().toInstant(), vEvent.getDateEnd().getValue().toInstant());

			// TODO 예쁘게 jump 로직 구현
			ICalDate recurrenceStart = vEvent.getDateStart().getValue();
			DateIterator it = recurrenceRule.getDateIterator(recurrenceStart);
			//it.advanceTo(new Date(from.toEpochSecond()));
			int repeatCount = 0;
			while (it.hasNext()) {
				if (repeatCount++ > MAX_REPEAT_COUNT) {
					break;
				}

				ZonedDateTime iDateTime = ZonedDateTime.ofInstant(it.next().toInstant(), zoneId);
				if (iDateTime.isBefore(from)) {
					continue;
				}
				if (iDateTime.isAfter(until)) {
					break;
				}

				// + RRID 가 있으면, 너도 예외다.
				boolean isExDate = false;
				for (ICalDate exICalDate : exceptionDatesList) {
					ZonedDateTime exDateTime = ZonedDateTime.ofInstant(exICalDate.toInstant(), zoneId);
					if (exDateTime.toLocalDate().equals(iDateTime.toLocalDate())) {
						isExDate = true;
					}
				}

				if (isExDate) {
					System.out.println("반복예외 날짜임으로 pass iDateTime=" + iDateTime.toString());
					continue;
				}

				ZonedDateTime startDateTime = ZonedDateTime.ofInstant(iDateTime.toInstant(), zoneId);
				ZonedDateTime endDateTime = startDateTime.plusSeconds(vEventDuration.getSeconds());
				simpleScheduleModelList.add(SimpleScheduleModel.of(vEvent, startDateTime, endDateTime));
			}

		}

		return simpleScheduleModelList;
	}

	private void buildSingleScheduleModelForViewFromExVEvent(ZonedDateTime from, ZonedDateTime until, ZoneId zoneId, List<SimpleScheduleModel> simpleScheduleModelList, VEvent vEvent) {
		ZonedDateTime startDateTime = ZonedDateTime.ofInstant(vEvent.getDateStart().getValue().toInstant(), zoneId);
		ZonedDateTime endDateTime = ZonedDateTime.ofInstant(vEvent.getDateEnd().getValue().toInstant(), zoneId);
		// 반복예외일정이, parsing 범위안에 있는지 검사.
		if (startDateTime.isBefore(from) || endDateTime.isAfter(until)) {
			return;
		}

		simpleScheduleModelList.add(SimpleScheduleModel.of(vEvent, zoneId));
		return;
	}

}

