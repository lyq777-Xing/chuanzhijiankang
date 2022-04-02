package net.zjitc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.JsonSerializable;
import net.zjitc.entity.CheckItem;

import java.util.List;

public interface CheckItemService extends IService<CheckItem> {
    public void add(CheckItem checkItem);
    public Page<CheckItem> findPage(Integer pagenum,Integer pagesize,String itemName);
    public void edit(CheckItem checkItem);
    public List<CheckItem> findAll();
    public CheckItem findByCode(String code);
    public CheckItem findByName(String name);
    public void deleteById(Integer id);
}
