package com.kristurek.polskatv.util;

import android.util.Log;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    public static DateTimeFormatter HHmm = DateTimeFormatter.ofPattern("HH:mm");
    public static DateTimeFormatter HHmmss = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static DateTimeFormatter EEEddMMyyyy = DateTimeFormatter.ofPattern("EEE - dd/MM/yyyy");
    public static DateTimeFormatter EEE = DateTimeFormatter.ofPattern("EEE");
    public static DateTimeFormatter ddMM = DateTimeFormatter.ofPattern("dd/MM");
    public static DateTimeFormatter ddMMMyyyy = DateTimeFormatter.ofPattern("dd. MMM yyyy");
    public static DateTimeFormatter ddMMyy = DateTimeFormatter.ofPattern("ddMMyy");

    public static String unixTimeToString(long unixTime, DateTimeFormatter formatter) {
        ZonedDateTime dt = Instant.ofEpochSecond(unixTime).atZone(ZoneId.of(SELECTED_TIME_ZONE_ID));

        return dt.format(formatter);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    public static String localDateToString(LocalDate localDate, DateTimeFormatter formatter) {
        return localDate.format(formatter);
    }

    public static int currentPercentBetweenUnixTime(long beginUnixTime, long endUnixTime) {
        long current = Instant.now().getEpochSecond();

        long rangeBeginEnd = endUnixTime - beginUnixTime;
        long rangeBeginCurrent = current - beginUnixTime;

        if (rangeBeginEnd == 0)
            return 0;

        int precent = Math.round((float) rangeBeginCurrent * 100 / rangeBeginEnd);
        if (precent > 100)
            return 100;
        else if (precent < 0)
            return 0;
        else
            return precent;
    }

    public static LocalDate getCurrentDaySelectedTimeZone() {
        return LocalDate.now(ZoneId.of(SELECTED_TIME_ZONE_ID));
    }

    public static LocalDateTime getCurrentTimeSelectedTimeZone() {
        return LocalDateTime.now(ZoneId.of(SELECTED_TIME_ZONE_ID));
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
        return day.atStartOfDay(ZoneId.of(SELECTED_TIME_ZONE_ID)).toEpochSecond();
    }

    public static long localDateTimeToUnixTime(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.of(SELECTED_TIME_ZONE_ID)).toEpochSecond();
    }

    public static LocalDate unixTimeToLocalDate(long unixTime) {
        return Instant.ofEpochSecond(unixTime).atZone(ZoneId.of(SELECTED_TIME_ZONE_ID)).toLocalDate();
    }

    public static String rangeUnixTimeToString(long beginUnixTime, long endUnixTime) {
        ZonedDateTime beginDateTime = Instant.ofEpochSecond(beginUnixTime).atZone(ZoneId.of(SELECTED_TIME_ZONE_ID));
        ZonedDateTime endDateTime = Instant.ofEpochSecond(endUnixTime).atZone(ZoneId.of(SELECTED_TIME_ZONE_ID));
        long minutesDuration = Duration.between(beginDateTime, endDateTime).toMinutes();

        StringBuilder result = new StringBuilder();
        result.append(beginDateTime.format(ddMMMyyyy));
        result.append(", ");
        result.append(beginDateTime.format(HHmm));
        result.append(" - ");
        result.append(endDateTime.format(HHmm));
        result.append(" (");
        result.append(minutesDuration);
        result.append(" min.)");

        return result.toString();
    }

    public static String periodUnixTimeToString(long beginUnixTime, long endUnixTime) {
        Duration duration = Duration.between(Instant.ofEpochSecond(beginUnixTime), Instant.ofEpochSecond(endUnixTime));

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d.%02d.%02d", hours, minutes, seconds);
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
        ZonedDateTime dateTime = mDate.atTime(LocalTime.parse(mTime)).atZone(ZoneId.of(SELECTED_TIME_ZONE_ID));
        return dateTime.toEpochSecond();
    }

    public static long unixTimeFromCurrentDayMinus13Days() {
        return LocalDate.now().minusDays(13).atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }
}
