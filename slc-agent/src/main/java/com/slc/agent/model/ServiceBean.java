package com.slc.agent.model;

import com.slc.agent.utils.JsonUtil;

/**
 *
 * @author zrh
 */
public class ServiceBean extends BaseBean implements java.io.Serializable {
    private static final long serialVersionUID = -1401514565399171693L;
    private Long begin;
    private Long end;
    private Long userTime;
    private String errorMsg;
    private String errorType;
    private String serviceName;
    private String simpleName;
    private String methodName;

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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public  void setStartAgent(String className,String methodName){
        this.setBegin(System.currentTimeMillis());
        this.setServiceName(className);
        this.setMethodName(methodName);
        this.setSimpleName(className.substring(className.lastIndexOf("/")));
        this.setRecordModel("service");
        this.setRecordTime(System.currentTimeMillis());
    }

    public  void setErrorAgent(Throwable e){
        this.setErrorType(e.getClass().getSimpleName());
        this.setErrorMsg(e.getMessage());
    }

    public  void setEndAgent(){
        this.setEnd(System.currentTimeMillis());
        this.setUserTime(this.end - this.begin);
        System.out.println(JsonUtil.toJsonStr(this));
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                "begin=" + begin +
                ", end=" + end +
                ", userTime=" + userTime +
                ", errorMsg='" + errorMsg + '\'' +
                ", errorType='" + errorType + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", simpleName='" + simpleName + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
