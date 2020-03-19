package com.seekingalpha.dm_flink.common;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.flink.shaded.zookeeper.org.apache.zookeeper.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;

import net.minidev.json.JSONObject;
import com.google.common.base.Splitter;


public class sql {
    public static Logger logger = LoggerFactory.getLogger(sql.class);
    static String mobile_web = "Mobile Web";
    static String amp = "AMP";
    static String desktop = "Desktop";
    static String mobile_apple = "Mobile Apps - Apple";
    static String mobile_android = "Mobile Apps - Android";
    static String other = "Other";
    static String page_view = "page_view";
    static String url_params_row = "url_params_row";

    public static String  emptyToNull(String input) {
        return (input == null || input.trim().isEmpty() || input.contains("{}") ) ? null : input;
    }

    public static String createClientType(Optional<String> varNamePageType) {
        String pageType = varNamePageType.orElse("").toLowerCase().trim();
        if (pageType.contains("mobile")) {
            return mobile_web;
        } else if (pageType.equals("responsive")) {
            return mobile_web;
        } else if (pageType.equals("amp")) {
            return amp;
        } else if (pageType.equals("regular")) {
            return desktop;
        } else if (pageType.contains("iphone")) {
            return mobile_apple;
        } else if (pageType.contains("ipad")) {
            return mobile_apple;
        } else if (pageType.contains("android")) {
            return mobile_android;
        } else {
            return other;
        }
    }

    public static String createSymbol(Optional<String> urlFirstLevel, Optional<String> clientType, Optional<String> url) {
        String rdyUrlFirstLevel = urlFirstLevel.orElse("").toLowerCase().trim();
        String rdyClientType = clientType.orElse("");
        String rdyUrl = url.orElse("");
        if (rdyUrlFirstLevel.equals("symbol") && rdyClientType.equals(mobile_apple)) {
            return rdyUrl.split("\\/")[3].toUpperCase();
        } else if (rdyUrlFirstLevel.equals("symbol") && rdyClientType.equals(mobile_android)) {
            return rdyUrl.split("\\/")[2].split("\\?")[1].toUpperCase();
        } else if(rdyUrlFirstLevel.equals("symbol")) {
            return rdyUrl.split("\\/")[2].toUpperCase();
        } else {
            return null;
        }
    }

    public static String textDecoding(Optional<String> text) throws UnsupportedEncodingException {
        try {
            if (text.orElse("").trim().isEmpty() || text.orElse("").trim().length() <= 1) { // url should never be null. checked at 2020-03-17
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
        if (fixUrl.equals("/") || fixUrl.equals("")) { // url should never be null. checked at 2020-03-17
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

    public static boolean isInteger(Object object) {
        if(object instanceof Integer ) {
            return true;
        } else {
            String string = object.toString();
            try {
                Integer.parseInt(string);
            } catch(Exception e) {
                return false;
            }
        }
        return true;
    }

    public static String createPageViewEventName(Optional<String> urlFirstLevel) {
        String rdyUrlFirstLevel = urlFirstLevel.orElse("").equals("") ? "" : ("|" + urlFirstLevel.get());
        return (page_view + rdyUrlFirstLevel);
    }


    public static String createUserIdCode(Optional<String> userId) {
        String rdyUserId = userId.orElse(""); // trim already done in class schema
        return (isInteger(rdyUserId) || rdyUserId.equals("")) ? null : rdyUserId;
    }

    public static Integer createUserId(Optional<String> userId) {
        String rdyUserId = userId.orElse(""); // trim already done in class schema
        return isInteger(rdyUserId) ? Integer.parseInt(rdyUserId) : null;
    }

    public static String keyJsonString = "jsonString";
    public static String keyJsonStringRow = "jsonStringRow";
//    static String valJsonString = "jsonString";
    public static String keyNameTrafficSourceParam = "colNameTrafficSourceParam";
    static String valNameTrafficSourceParam = "source";
    public static String keyNameUtmSource = "colNameUtmSource";
    static String valNameUtmSource = "utm_source";
    public static String keyNameUtmMeduim = "colNameUtmMeduim";
    static String valNameUtmMeduim = "utm_meduim";
    public static String keyNameUtmCampaign = "colNameUtmCampaign";
    static String valNameUtmCampaign = "utm_campaign";
    public static String keyNameUtmTerm = "colNameUtmTerm";
    static String valNameUtmTerm = "utm_term";
    public static String keyNameUtmContent = "colNameUtmContent";
    static String valNameUtmContent = "utm_content";

    public static HashMap<String, String> splitter(String text)  {
        HashMap<String, String> jsonHashMap = new HashMap<>( Splitter.on('&').trimResults().withKeyValueSeparator('=').split(text) );
        HashMap<String, String> hashMap = new HashMap<String, String>()
        {
            {
                put(keyJsonStringRow, null);
                put(keyNameTrafficSourceParam, emptyToNull(jsonHashMap.get(valNameTrafficSourceParam)));
                put(keyNameUtmSource, emptyToNull(jsonHashMap.get(valNameUtmSource)));
                put(keyNameUtmMeduim, emptyToNull(jsonHashMap.get(valNameUtmMeduim)));
                put(keyNameUtmCampaign, emptyToNull(jsonHashMap.get(valNameUtmCampaign)));
                put(keyNameUtmTerm, emptyToNull(jsonHashMap.get(valNameUtmTerm)));
                put(keyNameUtmContent, emptyToNull(jsonHashMap.get(valNameUtmContent)));
                put(keyJsonString, emptyToNull(new JSONObject(jsonHashMap).toString()));
            }
        };
        jsonHashMap.remove(valNameTrafficSourceParam);
        jsonHashMap.remove(valNameUtmSource);
        jsonHashMap.remove(valNameUtmMeduim);
        jsonHashMap.remove(valNameUtmCampaign);
        jsonHashMap.remove(valNameUtmTerm);
        jsonHashMap.remove(valNameUtmContent);

        hashMap.put(keyJsonString, emptyToNull(new JSONObject(jsonHashMap).toString()));
        return hashMap;
    }

    public static HashMap<String, String> emptyUarams()  {
        return new HashMap<String,String>()
    {
        {
            put(keyJsonString, null);
            put(keyNameTrafficSourceParam, null);
            put(keyNameUtmSource, null);
            put(keyNameUtmMeduim, null);
            put(keyNameUtmCampaign, null);
            put(keyNameUtmTerm, null);
            put(keyNameUtmContent, null);
        }
    };

    }

    public static HashMap<String, String> mappingUrlParams(Optional<String> urlParam) {
        String rdyUrlParam = urlParam.orElse(""); // trim already done in decoding
        if (rdyUrlParam.equals("") || rdyUrlParam.length() <= 1) {
            HashMap<String,String> map = emptyUarams();
            map.put(keyJsonStringRow,null);
            return map;
        } else if (!rdyUrlParam.startsWith("?")) {
            HashMap<String,String> hashMap = emptyUarams();
            hashMap.put(keyJsonStringRow,String.format("{\"%s\":\"%s\"}", url_params_row, rdyUrlParam));
            return hashMap;
        } else {
            try {
                System.out.println("does_splitter_succed?");
                return splitter(rdyUrlParam.split("\\?")[1]);
            }
            catch(Exception e) {
                HashMap<String,String> hashMap = emptyUarams();
                hashMap.put(keyJsonStringRow,String.format("{\"%s\":\"%s\"}", url_params_row, rdyUrlParam));
                return hashMap;
            }
        }
    }

    public static String extractUrlParamsRow(Optional<String> urlParam) {
        String rdyUrlParam = urlParam.orElse("");
        if (rdyUrlParam.equals("") || rdyUrlParam.length() <= 1) {
            return null;
        } else {
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(rdyUrlParam);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return "zz";
        }
    }


}

