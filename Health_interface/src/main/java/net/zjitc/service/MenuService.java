package net.zjitc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<Menu> findAll();
}
