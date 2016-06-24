package com.worksmobile.calendar;

import biweekly.component.VEvent;
import biweekly.property.Attendee;
import biweekly.property.Organizer;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

class SimpleScheduleModel {
	private String summary;
	private ZonedDateTime start;
	private ZonedDateTime end;

	private String organize;
	private List<String> attendeeList;


	private SimpleScheduleModel() {

	}

	public static SimpleScheduleModel of(VEvent vEvent, ZoneId zoneId) {
		ZonedDateTime start = ZonedDateTime.ofInstant(vEvent.getDateStart().getValue().toInstant(), zoneId);
		ZonedDateTime end = ZonedDateTime.ofInstant(vEvent.getDateEnd().getValue().toInstant(), zoneId);
		return of(vEvent, start, end);
	}

	public static SimpleScheduleModel of(VEvent vEvent, ZonedDateTime start, ZonedDateTime end) {
		SimpleScheduleModel simpleScheduleModel = new SimpleScheduleModel();
		simpleScheduleModel.setSummary(vEvent.getSummary().getValue());
		simpleScheduleModel.setStart(start);
		simpleScheduleModel.setEnd(end);

		if (null != vEvent.getOrganizer()) {
			Organizer organizer = vEvent.getOrganizer();
			String email = organizer.getEmail();
			if (Strings.isNullOrEmpty(email)) {
				email = organizer.getUri();
			}
			String cn = organizer.getCommonName();
			simpleScheduleModel.setOrganize("cn=" + cn + " email=" + email);
		}

		for (Attendee attendee : vEvent.getAttendees()) {
			if (null == simpleScheduleModel.getAttendeeList()) {
				simpleScheduleModel.setAttendeeList(Lists.newArrayList());
			}

			String email = attendee.getEmail();
			if (Strings.isNullOrEmpty(email)) {
				email = attendee.getUri();
			}
			String cn = attendee.getCommonName();
			simpleScheduleModel.getAttendeeList().add("cn=" + cn + " email=" + email);
		}

		return simpleScheduleModel;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public ZonedDateTime getStart() {
		return start;
	}

	public void setStart(ZonedDateTime start) {
		this.start = start;
	}

	public ZonedDateTime getEnd() {
		return end;
	}

	public void setEnd(ZonedDateTime end) {
		this.end = end;
	}

	public String getOrganize() {
		return organize;
	}

	public void setOrganize(String organize) {
		this.organize = organize;
	}

	public List<String> getAttendeeList() {
		return attendeeList;
	}

	public void setAttendeeList(List<String> attendeeList) {
		this.attendeeList = attendeeList;
	}

	@Override
	public String toString() {
		return "SimpleScheduleModel{" +
				"summary='" + summary + '\'' +
				", start=" + start +
				", end=" + end +
				", organize='" + organize + '\'' +
				", attendeeList=" + attendeeList +
				'}';
	}
}
