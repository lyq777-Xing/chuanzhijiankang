package net.zjitc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jdk.nashorn.internal.objects.annotations.Getter;
import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.constant.RedisConstant;
import net.zjitc.entity.CheckGroup;
import net.zjitc.entity.Setmeal;
import net.zjitc.service.SetmealService;
import net.zjitc.utils.JedisUtils;
import net.zjitc.utils.QiniuUtils;
import net.zjitc.utils.RedisUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 上传文件
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
//        获取原始name
        String originalFilename = imgFile.getOriginalFilename();
//        获取.jpg等后缀
        int index = originalFilename.lastIndexOf(".");
        String extention = originalFilename.substring(index);
//        随机产生fileName
        String fileName = UUID.randomUUID().toString() + extention;
        try {
//            将文件上传到七牛云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
//            上传到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @PostMapping("/add/{checkgroupIds}")
    public Result add(@RequestBody Setmeal setmeal,
                      @PathVariable("checkgroupIds") Integer[] checkgroupIds){
        try{
//            判断项目编码是否已经存在
            Setmeal code = setmealService.findByCode(setmeal.getCode());
            if(code != null){
//                说明该code已经被占用
                return new Result(false, MessageConstant.ADD_SETMEAL_FAIL,"该code已经被占用，请重新输入");
            }else {
//                判断项目名称是否已经存在
                Setmeal name = setmealService.findByName(setmeal.getName());
                if(name != null){
//                    说明name已经被占用
                    return new Result(false, MessageConstant.ADD_SETMEAL_FAIL,"该name已经被占用，请重新输入");
                }else {
//                    表明方法调用成功
                    setmealService.add(setmeal, checkgroupIds);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 获取分页查询
     * @param pagenum
     * @param pagesize
     * @param query
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @GetMapping("/findpage")
    public Result findPage(@RequestParam(required = true) Integer pagenum,
                           @RequestParam(required = true) Integer pagesize,
                           @RequestParam(required = false) String query){
        Page<Setmeal> page = null;
        try{
             page = setmealService.findPage(pagenum, pagesize, query);
            if(page.getRecords().size() == 0){
                return new Result(false,"查询失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
        return new Result(true,"查询成功",page);
    }

    /**
     * 查询所有
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @GetMapping("/findall")
    public Result findAll(){
        try{
            List<Setmeal> all = setmealService.findAll();
            if(all.size()>0 && all!=null){
                return new Result(true,"查询成功",all);
            }else {
                return new Result(false,"查询失败");
            }
        }catch (Exception e){
            return new Result(false,"查询失败");
        }
    }

}
