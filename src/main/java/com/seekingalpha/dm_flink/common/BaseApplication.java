package com.seekingalpha.dm_flink.common;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
// 2020-03-03T15:14:46+00:00
public class BaseApplication {
    private static final String timestampWithTzPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String MaxDateAsText = new SimpleDateFormat(timestampWithTzPattern).format(new Date(Integer.MAX_VALUE * 1000L));
    private static final String mainTimestampPattern = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter mainTimestampFormat = DateTimeFormatter.ofPattern(mainTimestampPattern);
    private static final String mainDatePattern = "yyyy-MM-dd";
    public static final DateTimeFormatter mainDateFormat = DateTimeFormatter.ofPattern(mainDatePattern);
}
