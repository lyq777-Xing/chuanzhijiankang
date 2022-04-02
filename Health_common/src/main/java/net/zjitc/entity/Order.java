package net.zjitc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检预约信息
 */

@TableName("t_order")
public class Order implements Serializable{
    @TableField(exist = false)
    public static final String ORDERTYPE_TELEPHONE = "电话预约";
    @TableField(exist = false)
    public static final String ORDERTYPE_WEIXIN = "微信预约";
    @TableField(exist = false)
    public static final String ORDERSTATUS_YES = "已到诊";
    @TableField(exist = false)
    public static final String ORDERSTATUS_NO = "未到诊";
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("member_id")
    private Integer memberId;//会员id
    @TableField("orderDate")
    private Date orderDate;//预约日期
    @TableField("orderType")
    private String orderType;//预约类型 电话预约/微信预约
    @TableField("orderStatus")
    private String orderStatus;//预约状态（是否到诊）
    @TableField("setmeal_id")
    private Integer setmealId;//体检套餐id

    public Order() {
    }

    public Order(Integer id) {
        this.id = id;
    }

    public Order(Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmealId = setmealId;
    }

    public Order(Integer id, Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.id = id;
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmealId = setmealId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(Integer setmealId) {
        this.setmealId = setmealId;
    }
}
