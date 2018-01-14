package com.onlineshopping.service.impl.customer;

import com.onlineshopping.dao.*;
import com.onlineshopping.entity.*;
import com.onlineshopping.service.customer.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by loong on 17-6-12.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private UserDao userDao;
    @Resource
    private VipLevelDao vipLevelDao;
    @Resource
    private AddressDao addressDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderDetailDao orderDetailDao;

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    /**
     * 会员注册
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public Result addCustomer(User user) {
        Result result = new Result();
        if (!isValidEmail(user.getUserId())) {
            result.setResultCode(-4);
            result.setResultInfo("用户名不合法");
        } else if (!isValidPassword(user.getPassword())) {
            result.setResultCode(-2);
            result.setResultInfo("密码不合法");
        } else if (!isValidNickName(user.getNickname())) {
            result.setResultCode(-3);
            result.setResultInfo("昵称不合法");
        } else if (userDao.isUserExist(user.getUserId())) {
            result.setResultCode(-1);
            result.setResultInfo("用户名已存在");
        } else {
            result.setResultCode(0);
            result.setResultInfo(user.getUserId());
            user.setPoints(0);
            user.setRegdate(new Date());
            user.setLevel(LevelType.普通会员);
            userDao.save(user);
        }
        result.setContent(user);
        return result;
    }

    /**
     * 会员登录
     *
     * @param user_id  用户名（id）
     * @param password 密码
     * @return
     */
    @Override
    public Result Login(String user_id, String password) {
        Result result = new Result();
        User user = userDao.findUserByUserId(user_id);
        if (user == null) {
            result.setResultCode(-1);
            result.setResultInfo("用户名不存在");
        } else if (!user.getPassword().equals(password)) {
            result.setResultCode(-2);
            result.setResultInfo("用户名和密码不匹配");
        } else {
            result.setResultCode(0);
            result.setResultInfo(user_id + "登录成功！");
            // 设置会员等级
            user.setLevel(getVipLevelByPoints(user.getPoints()));
            result.setContent(user);
        }
        return result;
    }

    /**
     * 获取会员信息
     *
     * @param user_id 用户id
     * @return
     */
    @Override
    public Result getUserInfo(String user_id) {
        Result result = new Result();
        User user = userDao.findUserByUserId(user_id);
        if (user == null) {
            result.setResultCode(-1);
            result.setResultInfo("用户名不存在");
        } else {
            result.setResultCode(0);
            result.setResultInfo("获取用户成功");
            user.setLevel(getVipLevelByPoints(user.getPoints()));
        }
        result.setContent(user);
        return result;
    }

    /**
     * 更改个人昵称
     *
     * @param user_id  会员id
     * @param nickname 昵称
     * @return
     */
    @Override
    public Result updateNickname(String user_id, String nickname) {
        Result result = new Result();
        if (nickname.isEmpty() || !isValidNickName(nickname)) {
            result.setResultCode(-1);
            result.setResultInfo("昵称不合法");
            return result;
        }
        User user = userDao.findUserByUserId(user_id);
        if (user == null) {
            result.setResultCode(-2);
            result.setResultInfo("用户不存在");
        } else {
            user.setNickname(nickname);
            userDao.update(user);
            result.setResultCode(0);
            result.setResultInfo("昵称修改成功");
            result.setContent(user.getNickname());
        }
        return result;
    }

    /**
     * 修改个人密码
     *
     * @param user_id      会员id
     * @param old_password 旧密码
     * @param new_password 新密码
     * @return
     */
    @Override
    public Result updatePassword(String user_id, String old_password, String new_password) {
        Result result = new Result();
        if (!isValidPassword(new_password)) {
            result.setResultCode(-2);
            result.setResultInfo("新密码不合法");
            return result;
        }
        User user = userDao.findUserByUserId(user_id);
        if (user == null) {
            result.setResultCode(-2);
            result.setResultInfo("用户名不存在");
        } else if (!user.getPassword().equals(old_password)) {
            result.setResultCode(-1);
            result.setResultInfo("原密码错误");
        } else {
            user.setPassword(new_password);
            userDao.update(user);
            result.setResultCode(0);
            result.setResultInfo("修改密码成功");
        }
        result.setContent(user);
        return result;
    }

    /**
     * 获取指定用户的地址簿
     *
     * @param user_id 用户id
     * @return
     */
    @Override
    public Result getAddressList(String user_id) {
        Result result = new Result();
        User user = userDao.findUserByUserId(user_id);
        if (user == null) {
            result.setResultCode(-2);
            result.setResultInfo("用户名不存在");
        } else {
            List<Address> addresses = addressDao.getAddressByUser(user);
            if (addresses == null || addresses.isEmpty()) {
                result.setResultCode(-1);
                result.setResultInfo("地址数量为0");
            } else {
                result.setResultCode(0);
                result.setResultInfo("获取地址信息成功");
            }
            result.setContent(addresses);
        }
        return result;
    }

    /**
     * 修改地址信息
     *
     * @param address_id 地址id
     * @param address    地址
     * @return
     */
    @Override
    public Result updateAddress(Integer address_id, Address address) {
        Result result = new Result();
        if (address.getName().length() > 20) {
            result.setResultCode(-3);
            result.setResultInfo("姓名不合法");
        } else if (address.getAddress().length() > 50) {
            result.setResultCode(-1);
            result.setResultInfo("地址不合法");
        } else if (!(address.getZip().equals("") || address.getZip().length() == 6)) {
            result.setResultCode(-2);
            result.setResultInfo("邮编不合法");
        } else if (!isValidPhoneNumber(address.getPhoneNumber())) {
            result.setResultCode(-4);
            result.setResultInfo("电话不合法");
        } else {
            Address address1 = addressDao.get(Address.class, address_id);
            if (address1 == null) {
                result.setResultCode(-5);
                result.setResultInfo("地址id不存在");
            } else {
                address1.setName(address.getName());
                address1.setAddress(address.getAddress());
                address1.setZip(address.getZip());
                address1.setPhoneNumber(address.getPhoneNumber());
                addressDao.update(address1);
                result.setResultCode(0);
                result.setResultInfo("修改地址成功");
            }
        }
        return result;
    }

    /**
     * 删除地址
     *
     * @param address_id 地址id
     * @return
     */
    @Override
    public Result deleteAddress(String user_id, Integer address_id) {
        Result result = new Result();
        Address address = addressDao.get(Address.class, address_id);
        if (address == null) {
            result.setResultCode(-1);
            result.setResultInfo("地址id不合法");
        } else if (!address.getUser().getUserId().equals(user_id)) {
            result.setResultCode(-2);
            result.setResultInfo("用户没有当前操作的权限");
        } else {
            addressDao.delete(address);
            result.setResultCode(0);
            result.setResultInfo("删除地址成功");
        }
        return result;
    }

    /**
     * 添加地址
     *
     * @param address 地址
     * @return
     */
    @Override
    public Result addAddress(String user_id, Address address) {
        Result result = new Result();
        if (address.getName().length() > 20) {
            result.setResultCode(-1);
            result.setResultInfo("姓名不合法");
        } else if (address.getAddress().length() > 50) {
            result.setResultCode(-2);
            result.setResultInfo("地址不合法");
        } else if (!(address.getZip().equals("") || address.getZip().length() == 6)) {
            result.setResultCode(-3);
            result.setResultInfo("邮编不合法");
        } else if (!isValidPhoneNumber(address.getPhoneNumber())) {
            result.setResultCode(-4);
            result.setResultInfo("电话不合法");
        } else {
            User user = userDao.findUserByUserId(user_id);
            if (user == null) {
                result.setResultCode(-5);
                result.setResultInfo("当前用户没有操作权限");
            } else if (addressDao.countAddressByUser(user) >= 4) {
                result.setResultCode(-6);
                result.setResultInfo("地址数量超出限制");
            } else {
                address.setUser(user);
                addressDao.save(address);
                result.setResultCode(0);
                result.setResultInfo("添加地址成功");
            }
        }
        return result;
    }

    @Override
    public Result getOrders(String begin_time, String end_time, String status, String user_id) {
        logger.info("获取订单");
        Result result = new Result();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime, endTime;
        StatusType statusType;
        try {
            beginTime = format.parse(begin_time);
            endTime = format.parse(end_time);
            if (beginTime.getTime() > endTime.getTime()) {
                result.setResultCode(-3);
                result.setResultInfo("开始时间不能大于结束时间");
                return result;
            }
            endTime.setTime(endTime.getTime() + 24 * 3600 * 1000);  // 延后一天
        } catch (ParseException e) {
            result.setResultCode(-1);
            result.setResultInfo("time参数不合法");
            return result;
        }

        logger.info("\n开始时间：" + beginTime +
                "\n结束时间：" + endTime +
                "\n订单状态：" + status);
        User user = new User();
        user.setUserId(user_id);
        List<Order> orders = orderDao.getOrdersByUserBeginEnd(user, beginTime, endTime);
        if (orders == null || orders.isEmpty()) {
            logger.info("订单总数为：0");
        } else {
            logger.info("订单总数为：" + orders.size());
            if (status != null && !status.isEmpty()) {
                try {
                    statusType = StatusType.valueOf(status);
                    logger.info("订单状态筛选...");
                    orders.removeIf(order -> order.getStatus() != statusType);
                } catch (Exception e) {
                    result.setResultCode(-2);
                    result.setResultInfo("status参数不合法");
                    return result;
                }
            }
            logger.info("订单数量为：" + orders.size());
            orders.forEach(order -> order.setOrderDetailList(null));
            result.setContent(orders);
        }
        if (orders == null || orders.isEmpty()) {
            result.setResultCode(-4);
            result.setResultInfo("订单的数量为0");
            return result;
        }
        result.setResultCode(0);
        result.setResultInfo("获取订单信息成功");
        return result;
    }

    @Override
    public Result getOrderById(Integer order_id, String user_id) {
        Result result = new Result();
        Order order = orderDao.get(Order.class, order_id);
        if (order == null || !order.getUser().getUserId().equals(user_id)) {
            result.setResultCode(-1);
            result.setResultInfo("order_id不合法");
            return result;
        }
        List<OrderDetail> orderDetail = orderDetailDao.getOrderDetailByOrder(order);
        if (orderDetail != null) {
            orderDetail.forEach(detail -> detail.setOrder(null));
            order.setOrderDetailList(orderDetail);
        }
//        else {
//            result.setResultCode(-2);
//            result.setResultInfo("获取订单详情失败");
//        }
        order.setOrderDetailList(orderDetail);
        result.setResultCode(0);
        result.setResultInfo("获取订单详情成功");
        result.setContent(order);
        return result;
    }

    @Override
    public Result getDiscount(String userId) {
        User user = userDao.findUserByUserId(userId);
        LevelType levelType = user.getLevel();
        Double discount = vipLevelDao.getVipLevelByLevelType(levelType).getDiscount();
        Result result = new Result();
        result.setContent(discount);
        result.setResultCode(0);
        result.setResultInfo("请求成功");
        return result;
    }

    @Override
    public Result getPointsDetail(String user_id) {
        Result result = new Result();
        User user = userDao.get(User.class, user_id);
        if (user == null) {
            result.setResultCode(-1);
            result.setResultInfo("操作错误");
            return result;
        }
        List<Order> orders = orderDao.getOrdersByUser(user);
        Points points = new Points();
        if (orders != null) {
            orders.removeIf(order -> order.getStatus() != StatusType.已收货);
            if (!orders.isEmpty()) {
                List<PointsItem> pointsItems = new ArrayList<>(orders.size());
                orders.forEach(order -> {
                    pointsItems.add(new PointsItem(order.getOrderId(), order.getOrderTime(), order.getPrice(), order.getPoints()));
                });
                points.setTotalPoints(user.getPoints());
                points.setPointsItems(pointsItems);
            }
        }
        result.setResultCode(0);
        result.setResultInfo("获取积分成功");
        result.setContent(points);
        return result;
    }

    @Override
    public Result getPointRate(String userId) {
        Result result = new Result();
        User user = userDao.get(User.class, userId);
        if (user == null) {
            result.setResultCode(-1);
            result.setResultInfo("操作错误");
            return result;
        }
        VipLevel vipLevel = vipLevelDao.getVipLevelByLevelType(getVipLevelByPoints(user.getPoints()));
        double rate = vipLevel.getRateCredit();
        result.setResultCode(0);
        result.setContent("请求成功");
        result.setContent(rate);
        return result;
    }

    /**
     * 根据积分获取会员等级
     *
     * @param points 积分
     * @return 会员等级
     */
    private LevelType getVipLevelByPoints(int points) {
        logger.info("获取等级信息");
        List<VipLevel> levels = vipLevelDao.getAll();
        if (levels == null) {
            logger.warn("等级信息为空");
            return null;
        }
        levels.sort(Comparator.comparingInt(VipLevel::getLowerLimitCredit));
        LevelType levelType = LevelType.普通会员;
        for (VipLevel level : levels) {
            if (points > level.getLowerLimitCredit())
                levelType = level.getLevel();
            else
                break;
        }
        return levelType;
    }

    /**
     * 判断邮箱格式是否合法
     *
     * @param email 邮箱
     * @return
     */
    private boolean isValidEmail(String email) {
        if (email.length() > 30)
            return false;
        String check = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * 判断密码格式是否合法
     *
     * @param password 密码
     * @return
     */
    private boolean isValidPassword(String password) {
        if (password.length() > 20 || password.length() < 6)
            return false;
        for (int i = 0; i < password.length(); i++) {
            if ((password.charAt(i) + "").getBytes().length != 1)
                return false;
        }
        return true;
    }

    /**
     * 判断昵称是否合法
     *
     * @param nickname 昵称
     * @return
     */
    private boolean isValidNickName(String nickname) {
        if (nickname.length() > 30)
            return false;
        return true;
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone.length() > 20) {
            return false;
        }
        // 电话号码正则表达式（支持手机号码，3-4位区号，7-8位直播号码，1－4位分机号）
        String check = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d)|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d))$)";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(phone);
        return matcher.matches();
    }
}
