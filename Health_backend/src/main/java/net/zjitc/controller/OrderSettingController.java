package net.zjitc.controller;

import net.zjitc.common.MessageConstant;
import net.zjitc.common.Result;
import net.zjitc.entity.OrderSetting;
import net.zjitc.service.OrderSettingService;
import net.zjitc.utils.POIUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.CredentialNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 预约设置
 */
@CrossOrigin
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * excel文件上传 并解析文件内容保存到数据库
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile){
        try{
//            List<String[]> list = POIUtils.readExcel(exceFile);
//            if(list != null && list.size() > 0){
//                ArrayList<OrderSetting> orderSettings = new ArrayList<>();
//                for (String[] strings:list) {
//                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
//                    orderSettings.add(orderSetting);
//                }
//                orderSettingService.add(orderSettings);
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
//        }
//        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            List<String[]> list = POIUtils.readExcel(excelFile);
//            System.out.println(toArray(list));
            List<OrderSetting> data=new ArrayList<>();
            for (String[] strings : list) {
                String orderDate= strings[0];
                String number= strings[1];
//                System.out.println(orderDate);
//                System.out.println(number);
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                data.add(orderSetting);
            }
//            System.out.println(data);
//            for (OrderSetting datum : data) {
                //判断当前日期是否已经进行了预约设置
//                System.out.println(datum.getOrderDate());
//                long count = ordersettingService.findCountByOrderDate(datum.getOrderDate());
//                System.out.println(count);
//                if(count>0){
//                    List<OrderSetting> byId = ordersettingService.findById(datum);
//                    Integer i=byId.get(0).getId();
//                    datum.setId(i);
//                    ordersettingService.editNumberByOrderDate(datum);
//                }else {
////                   List<OrderSetting> byId = ordersettingService.findById(datum);
////                   List<OrderSetting> list1 = ordersettingService.list();
////                   System.out.println(list1.get(30).getOrderDate());
//                    ordersettingService.add(datum);
//                }
//            }
            orderSettingService.add(data);
            return new Result(true,"创建或更新成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true,"文件解析失败");
        }
    }

    /**
     * 根据日期查询预约数据
     * @param date
     * @return
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //获取预约设置数据成功
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            //获取预约设置数据失败
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据指定日期修改可预约的人数
     * @param orderSetting
     * @return
     */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.editNumberByDate(orderSetting);
            //预约设置成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            //预约设置失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/findall")
    public Result findAll(){
        try{
            List<OrderSetting> all = orderSettingService.findAll();
            if(all == null || all.size()==0){
                return new Result(false,"查询失败");
            }else {
                return new Result(true,"查询成功",all);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }
}
