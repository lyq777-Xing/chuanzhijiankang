package net.zjitc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("t_user_role")
@Data
public class RoleAndUser implements Serializable {
    @TableField("user_id")
    private Integer user_id;
    @TableField("role_id")
    private Integer role_id;
}
