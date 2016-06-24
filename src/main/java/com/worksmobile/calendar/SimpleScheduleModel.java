package com.worksmobile.calendar;

import biweekly.component.VEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SimpleScheduleModel {
	private String summary;
	private ZonedDateTime start;
	private ZonedDateTime end;


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

	@Override
	public String toString() {
		return "SimpleScheduleModel{" +
				"summary='" + summary + '\'' +
				", start=" + start +
				", end=" + end +
				'}';
	}
}
