package net.zjitc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("sp_manager")
//@JsonInclude(JsonInclude.Include.ALWAYS)
public class Users implements Serializable {
    @TableId(value = "mg_id",type = IdType.AUTO)
    private Integer id;
    @TableField("mg_name")
    private String username;
    @TableField("mg_mobile")
    private String mobile;
    @TableField("mg_pwd")
//    @JsonIgnore
    private String password;
    @TableField(exist = false)
    private Integer type;
    @TableField("mg_email")
    private String email;
    @TableField("role_id")
//    @JsonIgnore
    private Integer rid;
    @TableField("mg_time")
    private String create_time;
    @TableField("mg_state")
    private Boolean mg_state;
    @TableField(exist = false)
    private String openid;
    @TableField(exist = false)
    private String modify_time;
    @TableField(exist = false)
    private Boolean is_delete;
    @TableField(exist = false)
    private Boolean is_active;
    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private String key;
    @TableField(exist = false)
    private String token;
    public void setCreateTime(String create_time) {
        this.create_time = create_time;
    }

    public void setMgState(Boolean mg_state) {
        this.mg_state = mg_state;
    }

    public void setModifyTime(String modify_time) {
        this.modify_time = modify_time;
    }

    public void setIsDelete(Boolean is_delete) {
        this.is_delete = is_delete;
    }

    public void setIsActive(Boolean is_active) {
        this.is_active = is_active;
    }
}

