package com.example.demo.test;

public class TestLog {

    public static void write(String msg) {
        System.out.println(msg);
    }

    public static void info(String msg) {
        msg.lines().forEach(s -> write("[INFO] " + msg));
    }

    public static void error(String msg) {
        msg.lines().forEach(s -> write("[ERROR] " + msg));
    }

    public static void warn(String msg) {
        msg.lines().forEach(s -> write("[WARN] " + msg));
    }

}
