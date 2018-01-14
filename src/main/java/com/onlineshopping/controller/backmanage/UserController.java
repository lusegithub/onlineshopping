package com.onlineshopping.controller.backmanage;

import com.onlineshopping.annotation.ManagerAccessRequired;
import com.onlineshopping.entity.LevelType;
import com.onlineshopping.entity.Result;
import com.onlineshopping.entity.VipLevel;
import com.onlineshopping.service.backmanage.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * created by lsy
 */
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 自动转化日期参数
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询会员等级
     * @return
     */
    @GetMapping("/vip-level")
    @ManagerAccessRequired
    public Result getVipLevel() {
        logger.info("管理员查询会员等级");
        return userService.getVipLevel();
    }

    /**
     * 设置会员等级
     * @param level_name            等级名称
     * @param lower_limit_credit    积分下限
     * @param rate_credit           积分比例
     * @param discount              折扣
     * @return
     */
    @PostMapping("/vip-level/{level_name}")
    @ManagerAccessRequired
    public Result setVipLevel(
            @PathVariable String level_name,
            int lower_limit_credit,
            double rate_credit,
            double discount) {
        // create vipLevel entity and set attribute
        VipLevel vipLevel = new VipLevel();
        LevelType level;
        try {
            level = LevelType.valueOf(level_name);
        } catch (Exception e) {
            level = null;
            e.printStackTrace();
        }
        vipLevel.setLevel(level);
        vipLevel.setLowerLimitCredit(lower_limit_credit);
        vipLevel.setRateCredit(rate_credit);
        vipLevel.setDiscount(discount);

        logger.info("管理员设置会员等级" + vipLevel.toString());
        return userService.setVipLevel(vipLevel);
    }

    /**
     * 查找用户
     * @param user_id       用户ID
     * @param level         用户等级
     * @param begin_time    注册时间下限
     * @param end_time      注册时间上限
     * @return
     */
    @GetMapping("/user")
    @ManagerAccessRequired
    public Result findUser(
            String user_id,
            String level,
            Date begin_time,
            Date end_time) {
        logger.info("管理员查找会员");
        if (end_time == null) {
            return userService.findUser(user_id, level, begin_time, null);
        }else {
            // 日期默认取时间0点，将结束日期+1，以查询包括结束日期的数据
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(end_time);
            calendar.add(Calendar.DATE, 1);
            return userService.findUser(user_id, level, begin_time, calendar.getTime());
        }
    }

    /**
     * 删除用户
     * @param user_id 用户ID
     * @return
     */
    @DeleteMapping("/user/{user_id}")
    @ManagerAccessRequired
    public Result deleteUser(@PathVariable String user_id) {
        user_id = user_id.replace("。", ".");
        logger.info("管理员删除会员" + user_id);
        return userService.deleteUser(user_id);
    }
}
