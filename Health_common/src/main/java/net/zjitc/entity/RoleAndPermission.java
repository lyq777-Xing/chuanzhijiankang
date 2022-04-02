package net.zjitc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_role_permission")
@Data
public class RoleAndPermission {
    @TableField("role_id")
    private Integer role_id;
    @TableField("permission_id")
    private Integer permission_id;
}
