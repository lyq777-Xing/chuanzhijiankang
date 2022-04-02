package net.zjitc.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderSuccess implements Serializable {
    private String name;
    private String setmeal;
    private Date orderDate;
    private String orderType;
}
