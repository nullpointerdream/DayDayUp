package com.yunsheng.demo;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


/**
 *
 */
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = -4203303500706245706L;

    // 标id
    @NotBlank(message = "orderId 为空")
    private String orderId;

    // 竞价开始时间(yyyy-MM-dd hh:mm:ss)
    private String startTime;

    // 正式竞价时间长度（min）
    private int normalTime;
    // 超时竞价时间间隔（ms）
    private int overTimeInterval;
    // 超时竞价时间总长度（min）
    private int overTime;

    // 最低降价类型，0：无限制，1：数值，2：百分比
    private int minPriceOffType;
    // 最低降价幅度
    private Double minPriceOff;

    // 最高降价幅度
    private Double maxPriceOff;

    // 报价次数限制，0：无限制，1：限制超时阶段次数，2：限制总报价次数
    private int timesLimit;
    // 报价次数
    private Integer times;


    // 报价上限
    private Double maxPrice;


    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId='" + orderId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", normalTime=" + normalTime +
                ", overTimeInterval=" + overTimeInterval +
                ", overTime=" + overTime +
                ", minPriceOffType=" + minPriceOffType +
                ", minPriceOff=" + minPriceOff +
                ", maxPriceOff=" + maxPriceOff +
                ", timesLimit=" + timesLimit +
                ", times=" + times +
                ", maxPrice=" + maxPrice +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getNormalTime() {
        return normalTime;
    }

    public void setNormalTime(int normalTime) {
        this.normalTime = normalTime;
    }

    public int getOverTimeInterval() {
        return overTimeInterval;
    }

    public void setOverTimeInterval(int overTimeInterval) {
        this.overTimeInterval = overTimeInterval;
    }

    public int getOverTime() {
        return overTime;
    }

    public void setOverTime(int overTime) {
        this.overTime = overTime;
    }

    public int getMinPriceOffType() {
        return minPriceOffType;
    }

    public void setMinPriceOffType(int minPriceOffType) {
        this.minPriceOffType = minPriceOffType;
    }

    public Double getMinPriceOff() {
        return minPriceOff;
    }

    public void setMinPriceOff(Double minPriceOff) {
        this.minPriceOff = minPriceOff;
    }

    public Double getMaxPriceOff() {
        return maxPriceOff;
    }

    public void setMaxPriceOff(Double maxPriceOff) {
        this.maxPriceOff = maxPriceOff;
    }

    public int getTimesLimit() {
        return timesLimit;
    }

    public void setTimesLimit(int timesLimit) {
        this.timesLimit = timesLimit;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
