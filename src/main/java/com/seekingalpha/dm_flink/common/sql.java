package com.seekingalpha.dm_flink.common;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonSerializer;
import scala.None;
import scala.None$;
import scala.Option;
import scala.collection.immutable.Nil;
import scala.compat.java8.OptionConverters.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;


public class sql {

    public static String createClientType(Optional<String> varNamePageType) {
        String pageType = varNamePageType.orElse("").toLowerCase().trim();
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

    public static String textDecoding(Optional<String> text) throws UnsupportedEncodingException {
        try {
            if (text.orElse("").trim().isEmpty()) {
                return null;
            } else {
                return java.net.URLDecoder.decode(text.get().trim(), StandardCharsets.UTF_8.name());
            }
        } catch (UnsupportedEncodingException e) {
            return String.format("{\"decoding_failure\":\"%s\"}", text.get());
        }


    }

    public static LocalDateTime offsetStringToLocalDateTime(String offsetString, String zoneName) {
        ZonedDateTime zdtNyTz = OffsetDateTime.parse(offsetString).atZoneSameInstant(ZoneId.of(zoneName));
        return LocalDateTime.of(zdtNyTz.getYear(), zdtNyTz.getMonth(), zdtNyTz.getDayOfMonth(), zdtNyTz.getHour(), zdtNyTz.getMinute(), zdtNyTz.getSecond());
    }

    public static String createUrlFirstLevel(Optional<String> url) {
        String fixUrl = url.orElse("").toLowerCase().trim();
        if (fixUrl.equals("/")) {
            return "home";
        } else if (!fixUrl.equals("")) {
            if (fixUrl.split("\\/")[1].equals("account")) {
                return fixUrl.split("\\/")[2];
            } else {
                return fixUrl.split("\\/")[1];
            }
        } else {
            return null;
        }
    }
}
//        if (pageType.contains("mobile")) {
//            return "Mobile Web";
//        } else if (pageType.equals("responsive")) {
//            return "Mobile Web";
//        } else if (pageType.equals("amp")) {
//            return "AMP";
//        } else if (pageType.equals("regular")) {
//            return "Desktop";
//        } else if (pageType.contains("iphone")) {
//            return "Mobile Apps - Apple";
//        } else if (pageType.contains("ipad")) {
//            return "Mobile Apps - Apple";
//        } else if (pageType.contains("android")) {
//            return "Mobile Apps - Android";
//        } else {
//            return "Other";
//        }


