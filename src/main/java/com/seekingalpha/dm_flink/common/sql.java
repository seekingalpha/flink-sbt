package com.seekingalpha.dm_flink.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonSerializer;
import scala.None;
import scala.None$;
import scala.Option;
import scala.collection.immutable.Nil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class sql {

    public static String createClientType(String varNamePageType){
        String pageType = varNamePageType.toLowerCase().trim();
        if (pageType.contains("mobile")) {
            return "Mobile Web";
        } else if (pageType.equals("responsive")) {
            return "Mobile Web";
        } else if (pageType.equals("amp")) {
            return "AMP";
        } else if (pageType.equals("regular")) {
            return "Desktop";
        } else if (pageType.contains("iphone")) {
            return "Mobile Apps - Apple";
        } else if (pageType.contains("ipad")) {
            return "Mobile Apps - Apple";
        } else if (pageType.contains("android")) {
            return "Mobile Apps - Android";
        } else {
            return "Other";
        }
    }

    public static Option<String> textDecoding(String text) throws UnsupportedEncodingException {
        return StringUtils.isEmpty(text) ? scala.Option.apply(null) :  Option.apply(java.net.URLDecoder.decode(text.trim(), StandardCharsets.UTF_8.name()));
    }

    public static LocalDateTime offsetStringToLocalDateTime(String offsetString, String zoneName){
        ZonedDateTime zdtNyTz = OffsetDateTime.parse(offsetString).atZoneSameInstant(ZoneId.of(zoneName));
        return LocalDateTime.of(zdtNyTz.getYear(),zdtNyTz.getMonth(),zdtNyTz.getDayOfMonth(),zdtNyTz.getHour(),zdtNyTz.getMinute(),zdtNyTz.getSecond());
    }

//    public static String
}
