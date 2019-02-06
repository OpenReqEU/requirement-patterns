package edu.upc.gessi.rptool.domain.metrics;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;

/*
 * This class represent a TimePoint metric. This type
 * of metric can't have a default value.
 */

@Entity
@Table(name = "TIME_POINT_METRIC")
public class TimePointMetric extends SimpleMetric {

    /*
     * ATTRIBUTES
     */
    // Using "_VALUE" because the year, month, day, hour, minute, second are
    // reserved
    @Column(name = "YEAR_VALUE", nullable = true)
    protected Integer year;

    @Column(name = "MONTH_VALUE", nullable = true)
    protected Integer month;

    @Column(name = "DAY_VALUE", nullable = true)
    protected Integer day;

    @Column(name = "HOUR_VALUE", nullable = true)
    protected Integer hour;

    @Column(name = "MINUTE_VALUE", nullable = true)
    protected Integer minute;

    @Column(name = "SECOND_VALUE", nullable = true)
    protected Integer second;

    /*
     * CREATORS
     */

    public void setTimeAttrib(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.setTimeZone(TimeZone.getTimeZone("GMT"));

	year = cal.get(Calendar.YEAR);
	month = cal.get(Calendar.MONTH) + 1;
	day = cal.get(Calendar.DAY_OF_MONTH);
	hour = cal.get(Calendar.HOUR_OF_DAY);
	minute = cal.get(Calendar.MINUTE);
	second = cal.get(Calendar.SECOND);
    }

    public void setTimeAttrib(String date) {
	String year = date.substring(0, 4);
	String month = date.substring(5, 7);
	String day = date.substring(8, 10);
	String hour = date.substring(11, 13);
	String min = date.substring(14, 16);
	String sec = date.substring(17, 19);
	if (!year.equals("XXXX")) {
	    this.year = Integer.valueOf(year);
	}
	if (!month.equals("XX")) {
	    this.month = Integer.valueOf(month);
	}
	if (!day.equals("XX")) {
	    this.day = Integer.valueOf(day);
	}
	if (!hour.equals("XX")) {
	    this.hour = Integer.valueOf(hour);
	}
	if (!min.equals("XX")) {
	    this.minute = Integer.valueOf(min);
	}
	if (!sec.equals("XX")) {
	    this.second = Integer.valueOf(sec);
	}
    }

    public TimePointMetric(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "date", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") Date date)
	    throws IntegrityException {
	super(name, description, comments);
	setTimeAttrib(date);

    }

    public TimePointMetric() {
    }

    /*
     * GET'S AND SET'S METHODS
     */

    @JsonIgnore
    public String getDateInString() {
	String year = this.year == null ? "XXXX" : String.format("%04d", this.year);
	String month = this.month == null ? "XX" : String.format("%02d", this.month);
	String day = this.day == null ? "XX" : String.format("%02d", this.day);
	String hour = this.hour == null ? "XX" : String.format("%02d", this.hour);
	String minute = this.minute == null ? "XX" : String.format("%02d", this.minute);
	String second = this.second == null ? "XX" : String.format("%02d", this.second);
	return "" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;

    }

    public void setYear(Integer y) {
	year = y;
    }

    public Integer getYear() {
	return year;
    }

    public void setMonth(Integer m) {
	month = m;
    }

    public Integer getMonth() {
	return month;
    }

    public void setDay(Integer d) {
	day = d;
    }

    public Integer getDay() {
	return day;
    }

    public void setHour(Integer h) {
	hour = h;
    }

    public Integer getHour() {
	return hour;
    }

    public void setMinute(Integer m) {
	minute = m;
    }

    public Integer getMinute() {
	return minute;
    }

    public void setSecond(Integer s) {
	second = s;
    }

    public Integer getSecond() {
	return second;
    }

    /*
     * OTHER METHODS
     */

    /**
     * This method is useful to see if this metric has a default value assigned. In
     * this type if metric, never has a default value assigned. Because of this,
     * this method always return false.
     * 
     * @return False (this type of metric never has a default value assigned).
     */
    @Override
    public boolean hasDefaultValue() {
	return false;
    }

    /**
     * This method do nothing (this type of metric never has a default value
     * assigned)
     */
    @Override
    public void unsetDefaultValue() {
	// Do anything (this type of metric never has a
	// default value assigned)
    }

    @Override
    public Type getType() {
	return Type.TIME;
    }

}