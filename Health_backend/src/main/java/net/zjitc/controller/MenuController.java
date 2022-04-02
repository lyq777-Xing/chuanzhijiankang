package net.zjitc.controller;

import net.zjitc.common.Result;
import net.zjitc.entity.Menu;
import net.zjitc.service.MenuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class MenuController {
    @Reference
    private MenuService menuService;

    @GetMapping("/menu")
    public Result findAll(){
        try{
            List<Menu> all = menuService.findAll();
            if(all != null &&all.size() != 0){
                return new Result(true,"查询成功",all);
            }else {
                return new Result(false,"查询失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }
}
