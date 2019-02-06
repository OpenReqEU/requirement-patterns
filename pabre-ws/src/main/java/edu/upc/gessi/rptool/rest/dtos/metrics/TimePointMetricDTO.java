package edu.upc.gessi.rptool.rest.dtos.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;

@JsonInclude(Include.ALWAYS)
public class TimePointMetricDTO extends MetricDTO {
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer minute;
    private Integer second;

    public TimePointMetricDTO(TimePointMetric m) {
	super(m);
	this.year = m.getYear();
	this.month = m.getMonth();
	this.day = m.getDay();
	this.hour = m.getHour();
	this.minute = m.getMinute();
	this.second = m.getSecond();
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public Integer getMonth() {
	return month;
    }

    public void setMonth(Integer month) {
	this.month = month;
    }

    public Integer getDay() {
	return day;
    }

    public void setDay(Integer day) {
	this.day = day;
    }

    public Integer getHour() {
	return hour;
    }

    public void setHour(Integer hour) {
	this.hour = hour;
    }

    public Integer getMinute() {
	return minute;
    }

    public void setMinute(Integer minute) {
	this.minute = minute;
    }

    public Integer getSecond() {
	return second;
    }

    public void setSecond(Integer second) {
	this.second = second;
    }
}
