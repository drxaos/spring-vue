package com.example.demo.test;

import com.example.demo.Main;
import com.example.demo.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.TimeZone;

@Slf4j
@ComponentScan(basePackageClasses = Main.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Main.class))
public abstract class AbstractTest {

    String savedConfig;

    protected SoftAssertions softly;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.setProperty("user.timezone", "UTC");
        log.info("Timezone: UTC, now: " + DateUtils.now());
    }

    {
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().getCanonicalName().equals("org.junit.Test")) {
                    throw new AssertionError("Method [" + declaredMethod.getName() + "] uses junit4 @Test annotation!");
                }
            }
        }
    }

    @BeforeEach
    public void softlyBefore() {
        softly = new SoftAssertions();
    }

    @AfterEach
    public void softlyAfter() {
        softly.assertAll();
    }

    @BeforeEach
    public void logBefore(TestInfo testInfo) {
        var name = testInfo.getTestClass().map(Class::getName).orElse(null) +
                "#" + testInfo.getTestMethod().map(Method::getName).orElse(null);

        TestLog.info("╔═══════════════════════════════════════════════════╗");
        TestLog.info("║                                                   ║");
        TestLog.info("║     ██████╗ ████████╗███████╗███████╗████████╗    ║");
        TestLog.info("║    ██╔═══██╗╚══██╔══╝██╔════╝██╔════╝╚══██╔══╝    ║");
        TestLog.info("║    ██║██╗██║   ██║   █████╗  ███████╗   ██║       ║");
        TestLog.info("║    ██║██║██║   ██║   ██╔══╝  ╚════██║   ██║       ║");
        TestLog.info("║    ╚█║████╔╝   ██║   ███████╗███████║   ██║       ║");
        TestLog.info("║     ╚╝╚═══╝    ╚═╝   ╚══════╝╚══════╝   ╚═╝       ║");
        TestLog.info("║                                                   ║");
        TestLog.info("╚═══════════════════════════════════════════════════╝");
        TestLog.info(">> " + name);
        TestLog.info(" ");
        log.info("Running test " + name);
    }

    @AfterEach
    public void logAfter(TestInfo testInfo) {
        long heapSize = Runtime.getRuntime().totalMemory(); // Get current size of heap in bytes
        long heapMaxSize = Runtime.getRuntime().maxMemory(); // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
        long heapFreeSize = Runtime.getRuntime().freeMemory(); // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.

        log.info("Finished test " +
                testInfo.getTestClass().map(Class::getName).orElse(null) +
                "#" + testInfo.getTestMethod().map(Method::getName).orElse(null));
        System.gc();
        log.info("Memory heapSize: " + formatSize(heapSize) + ", heapFree: " + formatSize(heapFreeSize) + ", max: " + formatSize(heapMaxSize));
    }

    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

}
