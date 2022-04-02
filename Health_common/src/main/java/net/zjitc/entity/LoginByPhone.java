package net.zjitc.entity;

import lombok.Data;

@Data
public class LoginByPhone {
    private String phoneNumber;
    private String code;
}
