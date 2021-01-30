package com.example.demo.util;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.GregorianCalendar;

@Slf4j
public class TestDateUtils {
    @SneakyThrows
    public static void mockDateUtilsNow(String dateString) {
        DateUtils.getCurrentDate = () -> {
            var date = DateUtils.parseDate(dateString);
            var calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar;
        };
        log.info("Date mocked: " + DateUtils.now());
    }
}
