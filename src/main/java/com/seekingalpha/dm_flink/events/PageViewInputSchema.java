package com.seekingalpha.dm_flink.events;

import org.apache.commons.lang3.StringUtils;
import scala.Int;

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
        return req_time;
    }

    public void setReq_time(String req_time) {
        this.req_time = StringUtils.isEmpty(req_time) ? null : req_time;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = StringUtils.isEmpty(user_id) ? null : user_id;
    }

    public String getMachineCookie() {
        return machine_cookie;
    }

    public void setMachine_cookie(String machine_cookie) {
        this.machine_cookie = StringUtils.isEmpty(machine_cookie) ? null : machine_cookie;
    }

    public String getSessionCookie() {
        return session_cookie;
    }

    public void setSession_cookie(String session_cookie) {
        this.session_cookie = StringUtils.isEmpty(session_cookie) ? null : session_cookie;
    }

    public String getUserAgent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = StringUtils.isEmpty(user_agent) ? null : user_agent;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = StringUtils.isEmpty(referrer) ? null : referrer;
    }

    public String getReferrerKey() {
        return referrer_key;
    }

    public void setReferrer_key(String referrer_key) {
        this.referrer_key = StringUtils.isEmpty(referrer_key) ? null : referrer_key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = StringUtils.isEmpty(url) ? null : url;
    }

    public String getUrlParams() {
        return url_params;
    }

    public void setUrl_params(String url_params) {
        this.url_params = StringUtils.isEmpty(url_params) ? null : url_params;
    }

    public String getMachineIp() {
        return machine_ip;
    }

    public void setMachine_ip(String machine_ip) {
        this.machine_ip = StringUtils.isEmpty(machine_ip) ? null : machine_ip;
    }

    public String getPageKey() {
        return page_key;
    }

    public void setPage_key(String page_key) {
        this.page_key = StringUtils.isEmpty(page_key) ? null : page_key;
    }

    public String getPageType() {
        return page_type;
    }

    public void setPage_type(String page_type) {
        this.page_type = StringUtils.isEmpty(page_type) ? null : page_type;
    }

    public Integer getPxScore() {
        return px_score;
    }

    public void setPx_score(String px_score) {
        this.px_score = StringUtils.isEmpty(px_score) ? null : Integer.parseInt(px_score);
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = StringUtils.isEmpty(other) ? null : other;
    }
}
