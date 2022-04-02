package net.zjitc.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrderInfo {
    private String sex;
    private String telephone;
    private String validateCode;
    private String idCard;
    private String date;
    private Integer setmealId;
    private String name;
}
