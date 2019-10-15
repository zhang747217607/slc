package com.slc.agent.model;

import com.slc.agent.utils.NetworkUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zrh
 */
public class HttpServiceBean extends BaseBean implements java.io.Serializable {
    private static final long serialVersionUID = -1401514565399171693L;
    private Long begin;
    private Long end;
    private Long userTime;
    private String errorMsg;
    private String errorType;
    private String requestUrl;
    private String requestMethod;
    private Integer responseStatus;

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getUserTime() {
        return userTime;
    }

    public void setUserTime(Long userTime) {
        this.userTime = userTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }


    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public  void setStartAgent(HttpServletRequest request){
        this.setBegin(System.currentTimeMillis());
        this.setHostIp(NetworkUtil.getIpAddress(request));
        this.setRequestMethod(request.getMethod());
        this.setRequestUrl(request.getRequestURL().toString());
        this.setRecordModel("http service");
        this.setRecordTime(System.currentTimeMillis());
    }

    public  void setErrorAgent(Throwable e){
        this.setErrorType(e.getClass().getSimpleName());
        this.setErrorMsg(e.getMessage());
    }

    public  void setEndAgent(HttpServletResponse response){
        this.setEnd(System.currentTimeMillis());
        this.setUserTime(this.end - this.begin);
        this.setResponseStatus(response.getStatus());
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "HttpServiceBean{" +
                "begin=" + begin +
                ", end=" + end +
                ", userTime=" + userTime +
                ", errorMsg='" + errorMsg + '\'' +
                ", errorType='" + errorType + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", responseStatus=" + responseStatus +
                '}' + super.toString();
    }
}
