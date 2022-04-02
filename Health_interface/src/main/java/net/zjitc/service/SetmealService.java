package net.zjitc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.zjitc.entity.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService extends IService<Setmeal> {
    public void add(Setmeal setmeal,Integer[] checkgroupIds);
    public Page<Setmeal> findPage(Integer pagenum,Integer pagesize,String query);
    Setmeal findByCode(String code);
    Setmeal findByName(String name);
    List<Setmeal> findAll();
    Setmeal findById(Integer id);
    List<Map<String,Object>> findSetmealCount();
}
