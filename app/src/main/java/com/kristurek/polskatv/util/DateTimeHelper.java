package com.kristurek.polskatv.util;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO use - Date and DateTime only in DateTimeHelper - outside only LocalDate, LocalDateTime
public class DateTimeHelper {

    public static void setSelectedTimeZoneId(String timeZoneId) {
        Log.d(Tag.UI, "DateTimeHelper.setSelectedTimeZoneId()[" + timeZoneId + "]");

        if (Arrays.asList(TIME_ZONE_IDS).contains(timeZoneId))
            SELECTED_TIME_ZONE_ID = timeZoneId;
        else
            throw new IllegalArgumentException("Provided time zone, doesn't fit");
    }

    public static final String DEFAULT_TIME_ZONE_ID = "Europe/Warsaw";
    public static String SELECTED_TIME_ZONE_ID = DEFAULT_TIME_ZONE_ID;
    public static final String[] TIME_ZONE_IDS = new String[]{
            "Europe/Warsaw",
            "Etc/GMT-14",
            "Etc/GMT-13",
            "Etc/GMT-12",
            "Etc/GMT-11",
            "Etc/GMT-10",
            "Etc/GMT-9",
            "Etc/GMT-8",
            "Etc/GMT-7",
            "Etc/GMT-6",
            "Etc/GMT-5",
            "Etc/GMT-4",
            "Etc/GMT-3",
            "Etc/GMT-2",
            "Etc/GMT-1",
            "Etc/GMT0",
            "Etc/GMT+1",
            "Etc/GMT+2",
            "Etc/GMT+3",
            "Etc/GMT+4",
            "Etc/GMT+5",
            "Etc/GMT+6",
            "Etc/GMT+7",
            "Etc/GMT+8",
            "Etc/GMT+9",
            "Etc/GMT+10",
            "Etc/GMT+11",
            "Etc/GMT+12"};

    public static DateTimeFormatter HHmm = DateTimeFormat.forPattern("HH:mm");
    public static DateTimeFormatter HHmmss = DateTimeFormat.forPattern("HH:mm:ss");
    public static DateTimeFormatter EEEddMMyyyy = DateTimeFormat.forPattern("EEE - dd/MM/yyyy");
    public static DateTimeFormatter EEE = DateTimeFormat.forPattern("EEE");
    public static DateTimeFormatter ddMM = DateTimeFormat.forPattern("dd/MM");
    public static DateTimeFormatter ddMMMyyyy = DateTimeFormat.forPattern("dd. MMM yyyy");
    public static DateTimeFormatter ddMMMyy = DateTimeFormat.forPattern("ddMMyy");

    public static String unixTimeToString(long unixTime, DateTimeFormatter formatter) {
        DateTime dt = new DateTime(unixTimeToMiliseconds(unixTime), DateTimeZone.forID(SELECTED_TIME_ZONE_ID));

        return dt.toString(formatter);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.toString(formatter);
    }

    public static String localDateToString(LocalDate localDate, DateTimeFormatter formatter) {
        return localDate.toString(formatter);
    }

    public static int currentPercentBetweenUnixTime(long beginUnixTime, long endUnixTime) {
        long current = milisecondsToUnixTime(DateTime.now().getMillis());

        long rangeBeginEnd = endUnixTime - beginUnixTime;
        long rangeBeginCurrent = current - beginUnixTime;

        if (rangeBeginEnd == 0)
            return 0;

        int precent = Math.round(rangeBeginCurrent * 100 / rangeBeginEnd);
        if (precent > 100)
            return 100;
        else if (precent < 0)
            return 0;
        else
            return precent;
    }

    private static long unixTimeToMiliseconds(long unixTime) {
        return unixTime * 1000L;
    }

    private static long milisecondsToUnixTime(long miliseconds) {
        return miliseconds / 1000L;
    }

    public static LocalDate getCurrentDaySelectedTimeZone() {
        return LocalDate.now(DateTimeZone.forID(SELECTED_TIME_ZONE_ID));
    }

    public static LocalDateTime getCurrentTimeSelectedTimeZone() {
        return LocalDateTime.now(DateTimeZone.forID(SELECTED_TIME_ZONE_ID));
    }

    public static LocalDate getCurrentDayDeviceTimeZone() {
        return LocalDate.now();
    }

    public static LocalDateTime getCurrentTimeDeviceTimeZone() {
        return LocalDateTime.now();
    }

    private static LocalDate toCurrentDateAdd7Days() {
        return getCurrentDaySelectedTimeZone().plusDays(7);
    }

    private static LocalDate toCurrentDateMinus13Days() {
        return getCurrentDaySelectedTimeZone().minusDays(13);
    }

    public static LocalDate getNextDay(LocalDate day) {
        LocalDate nextDay = day.plusDays(1);
        if (nextDay.isAfter(toCurrentDateAdd7Days()))
            return null;

        return nextDay;
    }

    public static LocalDate getPreviousDay(LocalDate day) {
        LocalDate previousDay = day.minusDays(1);
        if (previousDay.isBefore(toCurrentDateMinus13Days()))
            return null;

        return previousDay;
    }

    public static List<LocalDate> generateDays() {
        List<LocalDate> days = new ArrayList<>();
        LocalDate day = toCurrentDateMinus13Days();
        while (true) {
            days.add(day);
            if (day.plusDays(1).isAfter(toCurrentDateAdd7Days()))
                break;
            else
                day = day.plusDays(1);
        }

        return days;
    }

    public static long localDateToUnixTime(LocalDate day) {
        return day.toDateTimeAtStartOfDay(DateTimeZone.forID(SELECTED_TIME_ZONE_ID)).getMillis() / 1000L;
    }

    public static long localDateTimeToUnixTime(LocalDateTime dateTime) {
        return milisecondsToUnixTime(dateTime.toDateTime(DateTimeZone.forID(SELECTED_TIME_ZONE_ID)).getMillis());
    }

    public static LocalDate unixTimeToLocalDate(long unixTime) {
        return new LocalDate(unixTimeToMiliseconds(unixTime), DateTimeZone.forID(SELECTED_TIME_ZONE_ID));
    }

    public static String rangeUnixTimeToString(long beginUnixTime, long endUnixTime) {
        DateTime beginDateTime = new DateTime(unixTimeToMiliseconds(beginUnixTime), DateTimeZone.forID(SELECTED_TIME_ZONE_ID));
        DateTime endDateTime = new DateTime(unixTimeToMiliseconds(endUnixTime), DateTimeZone.forID(SELECTED_TIME_ZONE_ID));
        long minutesDuration = new Duration(beginDateTime, endDateTime).getStandardMinutes();

        StringBuilder result = new StringBuilder();
        result.append(beginDateTime.toString(ddMMMyyyy));
        result.append(", ");
        result.append(beginDateTime.toString(HHmm));
        result.append(" - ");
        result.append(endDateTime.toString(HHmm));
        result.append(" (");
        result.append(minutesDuration);
        result.append(" min.)");

        return result.toString();
    }

    public static String periodUnixTimeToString(long beginUnixTime, long endUnixTime) {
        Period pr = new Period(unixTimeToMiliseconds(beginUnixTime), unixTimeToMiliseconds(endUnixTime));

        return String.format("%02d.%02d.%02d", pr.getHours(), pr.getMinutes(), pr.getSeconds());
    }

    public static int compareCurrentDayBetweenTimeZones() {
        int dayInDefaultTimeZone = getCurrentTimeDeviceTimeZone().getDayOfMonth();
        int dayInSelectedTimeZone = getCurrentTimeSelectedTimeZone().getDayOfMonth();

        if (dayInSelectedTimeZone > dayInDefaultTimeZone)
            return 1;
        else if (dayInSelectedTimeZone < dayInDefaultTimeZone)
            return -1;
        else
            return 0;
    }

    public static long customDateTimeToUnixTime(LocalDate mDate, String mTime) {
        DateTime dateTime = mDate.toDateTime(LocalTime.parse(mTime), DateTimeZone.forID(SELECTED_TIME_ZONE_ID));
        return milisecondsToUnixTime(dateTime.getMillis());
    }

    public static long unixTimeFromCurrentDayMinus13Days() {
        return Duration.millis(DateTime.now().withTimeAtStartOfDay().minusDays(13).getMillis()).getStandardSeconds();
    }

    public static void main(String[] argv) {
        long unixTime = 1575924100;milisecondsToUnixTime(DateTime.now().withTimeAtStartOfDay().getMillis());

        System.out.println(unixTimeToString(unixTime, DateTimeHelper.ddMMMyy));
    }
}
