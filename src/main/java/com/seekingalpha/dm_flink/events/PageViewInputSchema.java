package com.seekingalpha.dm_flink.events;

import org.apache.commons.lang3.StringUtils;
import scala.Int;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.seekingalpha.dm_flink.common.BaseApplication.*;

public class PageViewInputSchema {
    private String req_time;
    private String user_id;
    private String machine_cookie;
    private String session_cookie;
    private String user_agent;
    private String referrer;
    private String referrer_key;
    private String url;
    private String url_params;
    private String machine_ip;
    private String page_key;
    private String page_type;
    private Integer px_score;
    private String other;




    public String getReqTime() {
        return StringUtils.isEmpty(this.req_time) ? MaxDateAsText : this.req_time; // can never be null
    }

    public void setReq_time(String req_time) {
        this.req_time = req_time;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMachineCookie() {
        return machine_cookie;
    }

    public void setMachine_cookie(String machine_cookie) {
        this.machine_cookie = machine_cookie;
    }

    public String getSessionCookie() {
        return session_cookie;
    }

    public void setSession_cookie(String session_cookie) {
        this.session_cookie = session_cookie;
    }

    public String getUserAgent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public Optional<String> getReferrer() {
        return StringUtils.isEmpty(this.referrer) ? Optional.empty() : Optional.of(this.referrer.trim());
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getReferrerKey() {
        return referrer_key;
    }

    public void setReferrer_key(String referrer_key) {
        this.referrer_key = referrer_key;
    }

    public Optional<String> getUrl() {
        return StringUtils.isEmpty(this.url) ? Optional.empty() : Optional.of(this.url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Optional<String> getUrlParams() {
        return StringUtils.isEmpty(this.url_params) ? Optional.empty() : Optional.of(this.url_params);
    }

    public void setUrl_params(String url_params) {
        this.url_params = url_params;
    }

    public String getMachineIp() {
        return machine_ip;
    }

    public void setMachine_ip(String machine_ip) {
        this.machine_ip = machine_ip;
    }

    public String getPageKey() {
        return page_key;
    }

    public void setPage_key(String page_key) {
        this.page_key = page_key;
    }

    public Optional<String> getPageType() {
        return StringUtils.isEmpty(this.page_type) ? Optional.empty() : Optional.of(this.page_type);
    }

    public void setPage_type(String page_type) {
        this.page_type = page_type;
    }

    public Optional<Integer> getPxScore() {
//        return Optional.of(this.px_score);
        return this.px_score == null ? Optional.empty() : Optional.of(this.px_score);
    }

    public void setPx_score(String px_score) {
        this.px_score = Integer.parseInt(px_score);
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
