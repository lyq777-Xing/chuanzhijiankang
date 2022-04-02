package net.zjitc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_setmeal_checkgroup")
public class SetmealAndCheckgroup {
    @TableField("setmeal_id")
    private Integer setmeal_id;
    @TableField("checkgroup_id")
    private Integer checkgroup_id;

    public void setSetmealId(Integer setmeal_id) {
        this.setmeal_id = setmeal_id;
    }

    public void setCheckgroupId(Integer checkgroup_id) {
        this.checkgroup_id = checkgroup_id;
    }
}
