package net.zjitc.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSetmealVo extends Order implements Serializable {
    private String setmeal;
}
