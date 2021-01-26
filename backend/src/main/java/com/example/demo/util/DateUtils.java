package com.example.demo.util;

import lombok.SneakyThrows;
import lombok.val;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    static Supplier<GregorianCalendar> getCurrentDate = GregorianCalendar::new;

    public static Date now() {
        return getCurrentDate.get().getTime();
    }

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static Date parseDate(String date, String format) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(String date) {
        if (date == null) {
            return null;
        }

        var result = new AtomicReference<Date>();

        List.of("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd HH:mm:ss,SSSSSS",
                "yyyy-MM-dd HH:mm:ss,SSS",
                "yyyy-MM-dd HH:mm:ss.SSSSSS",
                "yyyy-MM-dd HH:mm:ss.SSS",
                "yyyy-MM-dd HH:mm:ss",
                "HH:mm:ss dd.MM.yyyy",
                "dd.MM.yyyy HH:mm:ss",
                "HH:mm:ss dd.MM.yy",
                "dd.MM.yy HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "HH:mm dd.MM.yyyy",
                "dd.MM.yyyy HH:mm",
                "HH:mm dd.MM.yy",
                "dd.MM.yy HH:mm",
                "yyyy-MM-dd",
                "dd.MM.yyyy",
                "dd.MM.yy",
                "MM.yyyy",
                "MM.yy",
                "yyyy",
                "yy").stream().anyMatch(format -> {
            try {
                var simpleDateFormat = new SimpleDateFormat(format);
                simpleDateFormat.setLenient(false);
                result.set(simpleDateFormat.parse(date));
                return true;
            } catch (ParseException pe) {
                return false;
            }
        });

        return result.get();
    }

    public static String formatDate(GregorianCalendar date, String format) {
        if (date == null) {
            return "None";
        }
        val sdf = new SimpleDateFormat(format);
        sdf.setCalendar(date);
        return sdf.format(date.getTime());
    }

    public static String formatDate(Date date) {
        return formatDate(date, "dd.MM.yyyy");
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, "HH:mm:ss dd.MM.yyyy");
    }

    public static String formatDateTimeMs(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static Date max(Date date1, Date date2) {
        return date1.compareTo(date2) > 0 ? date1 : date2;
    }

    /**
     * DateTime в формате ISO-8601 в UTC '2011-12-03T10:15:30Z'
     */
    public static String toIso8601utc(Date date) {
        return date == null ? null : DateTimeFormatter.ISO_INSTANT.format(date.toInstant());
    }

    public static Date nowMinusDuration(Duration duration) {
        var now = Duration.of(now().getTime(), ChronoUnit.MILLIS);
        return new Date(now.minus(duration).toMillis());
    }

    public static boolean isBefore(Date dateBefore, Date dateAfter) {
        return dateBefore != null && dateAfter != null && dateBefore.compareTo(dateAfter) < 0;
    }

    public static boolean isAfter(Date dateAfter, Date dateBefore) {
        return dateAfter != null && dateBefore != null && dateAfter.compareTo(dateBefore) > 0;
    }


    @SneakyThrows
    public static XMLGregorianCalendar xmlNow() {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(getCurrentDate.get());
    }

    public static Date parseXml(String text) {
        return DatatypeConverter.parseDateTime(text).getTime();
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    public static String formatDuration(long ms) {
        long seconds = ms / 1000;
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}
