package com.onlineshopping.service.impl.backmanage;

import com.onlineshopping.dao.UserDao;
import com.onlineshopping.dao.VipLevelDao;
import com.onlineshopping.entity.LevelType;
import com.onlineshopping.entity.Result;
import com.onlineshopping.entity.User;
import com.onlineshopping.entity.VipLevel;
import com.onlineshopping.service.backmanage.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private Result result = new Result();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private VipLevelDao VipLevelDao;
    @Resource
    private UserDao UserDao;

    /**
     * 查询会员等级
     * @return
     */
    @Override
    public Result getVipLevel() {
        result.setResultCode(0);
        result.setResultInfo("查询会员等级成功！");
        result.setContent(VipLevelDao.getAll());
        logger.info(result.getResultInfo());
        return result;
    }

    /**
     * 设置会员等级
     * @param vipLevel 会员等级
     * @return
     */
    @Override
    public Result setVipLevel(VipLevel vipLevel) {
        result.setContent(null);

        // 验证信息
        if (vipLevel.getLowerLimitCredit() < 0) {
            result.setResultCode(-2);
            result.setResultInfo("积分下限必须为非0整数！");
            logger.error(result.getResultInfo());
            return result;
        }

        if (vipLevel.getLowerLimitCredit() != 0 && vipLevel.getLevel() == LevelType.普通会员) {
            result.setResultCode(-2);
            result.setResultInfo("普通会员的积分下限必须为0！");
            logger.error(result.getResultInfo());
            return result;
        }

        if (vipLevel.getRateCredit() < 1.00) {
            result.setResultCode(-3);
            result.setResultInfo("积分比例必须填入大不小于100的整数！");
            logger.error(result.getResultInfo());
            return result;
        }

        if (vipLevel.getDiscount() > 1 || vipLevel.getDiscount() <= 0) {
            result.setResultCode(-4);
            result.setResultInfo("折扣必须填入1-100内的整数！");
            logger.error(result.getResultInfo());
            return result;
        }

        VipLevel oldVipLevel = VipLevelDao.getVipLevelByLevelType(vipLevel.getLevel());
        if (oldVipLevel == null) {
            result.setResultCode(-1);
            result.setResultInfo("会员等级" + vipLevel.getLevel() + "不存在！");
            logger.error(result.getResultInfo());
            return result;
        }
        oldVipLevel.setLowerLimitCredit(vipLevel.getLowerLimitCredit());
        oldVipLevel.setRateCredit(vipLevel.getRateCredit());
        oldVipLevel.setDiscount(vipLevel.getDiscount());
        oldVipLevel.setNote(vipLevel.getNote());

        VipLevelDao.updateVipLevel(oldVipLevel);
        result.setResultCode(0);
        result.setResultInfo("设置会员等级成功！");
        logger.info(result.getResultInfo());
        return result;
    }

    /**
     * 查找用户
     * @param user_id       用户ID
     * @param level         用户等级
     * @param begin_time    注册时间下限
     * @param end_time      注册时间上限
     * @return
     */
    @Override
    public Result findUser(String user_id, String level, Date begin_time, Date end_time) {
        result.setContent(null);

        List<User> users;
        // 查找用户并保存到users中
        if (user_id == null || user_id.equals("")) {
            // 用户ID为空，根据其他条件进行查询
            result.setResultCode(0);
            users = findUser(level, begin_time, end_time);
            //查询条件非法
            if (result.getResultCode() != 0) {
                logger.error(result.getResultInfo());
                return result;
            }
        } else {
            // 用户ID不为空，直接根据用户ID查找
            User user = UserDao.findUserByUserId(user_id);
            if (user == null) {
                result.setResultCode(-1);
                result.setResultInfo("用户" + user_id + "不存在！");
                logger.error(result.getResultInfo());
                return result;
            }
            users = new ArrayList<>();
            users.add(user);
        }

        if (users == null) {
            result.setResultCode(-3);
            result.setResultInfo("没有满足条件的会员！");
            logger.error(result.getResultInfo());
        } else {
            result.setResultCode(0);
            result.setResultInfo("查询会员成功！");
            logger.info(result.getResultInfo());
            result.setContent(users);
        }
        return result;
    }

    /**
     * 查找用户
     * @param levelName     用户等级名称
     * @param begin_time    注册时间下限
     * @param end_time      注册时间上限
     * @return 用户列表
     */
    private List<User> findUser(String levelName, Date begin_time, Date end_time) {

        if (levelName == null || levelName.equals("")) {
            if (begin_time == null) {
                if (end_time == null) {
                    // 全为空，查找全部user
                    return UserDao.findAll(User.class);
                } else {
                    // 只有结束时间不为空
                    return UserDao.getUsersByEndTime(end_time);
                }
            } else {
                // 起始时间不为空
                if (end_time == null) {
                    // 只有起始时间不为空
                    return UserDao.getUsersByBeginTime(begin_time);
                } else {
                    // 只有用户等级为空
                    if (begin_time.after(end_time)) {
                        result.setResultCode(-3);
                        result.setResultInfo("起始时间晚于结束时间！");
                        return null;
                    }
                    return UserDao.getUsersByBeginEnd(begin_time, end_time);
                }
            }
        } else {
            LevelType level;
            try {
                level = LevelType.valueOf(levelName);
            } catch (Exception e) {
                result.setResultCode(-2);
                result.setResultInfo("等级不存在！");
                return null;
            }
            // 用户等级不为空
            if (begin_time == null) {
                if (end_time == null) {
                    // 只有用户等级不为空
                    return UserDao.getUsersByLevel(level);
                } else {
                    // 只有起始时间为空
                    return UserDao.getUsersByLevelEnd(level, end_time);
                }
            } else {
                // 起始时间不为空
                if (end_time == null) {
                    // 只有结束时间为空
                    return UserDao.getUsersByLevelBegin(level, begin_time);
                } else {
                    // 全部不为空
                    if (begin_time.after(end_time)) {
                        result.setResultCode(-3);
                        result.setResultInfo("起始时间晚于结束时间！");
                        return null;
                    }
                    return UserDao.getUsersByLevelBeginEnd(level, begin_time, end_time);
                }
            }
        }
    }

    /**
     * 删除用户
     * @param user_id 用户ID
     * @return
     */
    @Override
    public Result deleteUser(String user_id) {
        result.setContent(null);

        User user = UserDao.get(User.class, user_id);
        if (user == null) {
            result.setResultCode(-1);
            result.setResultInfo("用户" + user_id + "不存在！");
            logger.error(result.getResultInfo());
            return result;
        }

        result.setResultCode(UserDao.deleteUser(user));
        switch (result.getResultCode()) {
            case 0:
                result.setResultInfo("删除用户成功！");
                logger.info(result.getResultInfo());
                break;
            case -2:
                result.setResultInfo("删除用户失败！");
                logger.error(result.getResultInfo());
        }
        result.setContent(null);
        return result;
    }
}
