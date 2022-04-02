package net.zjitc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_checkgroup_checkitem")
public class CheckGoupAndItem {
    private Integer checkgroup_id;
    private Integer checkitem_id;

    public void setCheckgroupId(Integer checkgroup_id) {
        this.checkgroup_id = checkgroup_id;
    }

    public void setCheckitemId(Integer checkitem_id) {
        this.checkitem_id = checkitem_id;
    }
}
