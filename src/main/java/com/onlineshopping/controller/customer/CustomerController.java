package com.onlineshopping.controller.customer;

import com.onlineshopping.annotation.UserAccessRequired;
import com.onlineshopping.entity.*;
import com.onlineshopping.service.customer.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by loong on 17-6-12.
 */
@RestController
@RequestMapping(path = "/user")
public class CustomerController {
    private CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Resource
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 用户注册
     *
     * @param user_id  用户名
     * @param password 密码
     * @param nickname 昵称
     * @return
     */
    @PostMapping
    public Result registered(@RequestParam String user_id, @RequestParam String password, @RequestParam(required = false) String nickname, HttpSession session, HttpServletResponse response) {
        logger.info("用户注册");
        User user = new User();
        user.setUserId(user_id);
        if (nickname == null || nickname.isEmpty()) {
            nickname = "undefined";
        }
        user.setNickname(nickname);
        user.setPassword(password);
        Result result = customerService.addCustomer(user);
        if (result.getResultCode() == 0) {
            Cookie jsessionid = new Cookie("JSESSIONID", session.getId());
            Cookie userId = new Cookie("user_id", user_id);
            jsessionid.setMaxAge(60 * 30);
            userId.setMaxAge(60 * 30);
            response.addCookie(jsessionid);
            response.addCookie(userId);
        }
        return result;
    }

    /**
     * 退出登录
     *
     * @param session session
     * @return
     */
    @PutMapping
    public Result logout(HttpSession session) {
        logger.info("用户退出登录");
        session.removeAttribute("user_id");
        Result result = new Result();
        result.setResultCode(0);
        result.setResultInfo("OK");
        return result;
    }

    /**
     * 会员登录
     *
     * @param user_id  用户名
     * @param password 密码
     * @param request  请求信息
     * @param response 相应信息
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestParam String user_id, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户登录：" + user_id);
        HttpSession session = request.getSession();
        String id = session.getId();
        Cookie cookie = new Cookie("JSESSIONID", id);
        cookie.setMaxAge(60 * 60 * 5);
        response.addCookie(cookie);

        Result result = customerService.Login(user_id, password);
        if (result.getResultCode() == 0) {
            session.setAttribute("user_id", user_id);
        } else
            session.removeAttribute("user_id");
        return result;
    }

    /**
     * 获取用户信息
     *
     * @param session 会话信息
     * @return
     */
    @GetMapping(path = "/info")
    @UserAccessRequired
    public Result getUserInfo(HttpSession session) {
        logger.info("获取用户信息");
        String user_id = session.getAttribute("user_id").toString();
        return customerService.getUserInfo(user_id);
    }

    /**
     * 修改昵称
     *
     * @param nickname 新昵称
     * @param session  会话信息
     * @return
     */
    @PutMapping(path = "/info/nickname")
    @UserAccessRequired
    public Result updateNickname(@RequestParam String nickname, HttpSession session) {
        logger.info("修改昵称");
        String user_id = session.getAttribute("user_id").toString();
        return customerService.updateNickname(user_id, nickname);
    }

    /**
     * 修改密码
     *
     * @param old_passwd 旧密码
     * @param new_passwd 新密码
     * @param session    会话信息
     * @return
     */
    @PutMapping(path = "/info/passwd")
    @UserAccessRequired
    public Result updatePassword(@RequestParam String old_passwd, @RequestParam String new_passwd, HttpSession session) {
        logger.info("修改密码");
        String user_id = session.getAttribute("user_id").toString();
        Result result = customerService.updatePassword(user_id, old_passwd, new_passwd);
        if (result.getResultCode() == 0) {
            session.removeAttribute("user_id");
        }
        return result;
    }

    /**
     * 获取地址信息
     *
     * @param session 会话信息
     * @return
     */
    @GetMapping("/addresses")
    @UserAccessRequired
    public Result getAddresses(HttpSession session) {
        logger.info("获取地址信息");
        String user_id = session.getAttribute("user_id").toString();
        return customerService.getAddressList(user_id);
    }

    /**
     * 更新地址信息
     *
     * @param address_id 地址id
     * @param address    地址
     * @param zip        邮政编码
     * @param consignee  收件人
     * @param phone      电话
     * @param session    会话信息
     * @return
     */
    @PutMapping("/address/{address_id}")
    @UserAccessRequired
    public Result updateAddress(@PathVariable("address_id") Integer address_id, @RequestParam String address, @RequestParam String zip, @RequestParam String consignee, @RequestParam String phone, HttpSession session) {
        logger.info("更新地址信息");
        String user_id = session.getAttribute("user_id").toString();
        Address aAddress = new Address();
        User user = new User();
        user.setUserId(user_id);
        aAddress.setUser(user);
        aAddress.setPhoneNumber(phone);
        aAddress.setAddress(address);
        aAddress.setZip(zip);
        aAddress.setName(consignee);
        aAddress.setAddressId(address_id);
        return customerService.updateAddress(address_id, aAddress);
    }

    /**
     * 删除地址
     *
     * @param address_id 地址id
     * @param session    会话信息
     * @return
     */
    @DeleteMapping("/address/{address_id}")
    @UserAccessRequired
    public Result deleteAddress(@PathVariable("address_id") Integer address_id, HttpSession session) {
        logger.info("删除地址");
        String user_id = session.getAttribute("user_id").toString();
        return customerService.deleteAddress(user_id, address_id);
    }

    /**
     * 添加地址
     *
     * @param address   地址
     * @param zip       邮编
     * @param consignee 收件人
     * @param phone     电话
     * @param session   会话信息
     * @return
     */
    @PostMapping("/addresses")
    @UserAccessRequired
    public Result addAddress(@RequestParam String address, @RequestParam String zip, @RequestParam String consignee, @RequestParam String phone, HttpSession session) {
        logger.info("添加地址");
        String user_id = session.getAttribute("user_id").toString();
        Address aAddress = new Address();
        aAddress.setName(consignee);
        aAddress.setZip(zip);
        aAddress.setAddress(address);
        aAddress.setPhoneNumber(phone);
        return customerService.addAddress(user_id, aAddress);
    }

    /**
     * 订单查询
     *
     * @param begin_time 开始时间
     * @param end_time   结束时间
     * @param status     订单状态
     * @param session    会话信息
     * @return
     */
    @GetMapping("/orders")
    @UserAccessRequired
    public Result getOrders(@RequestParam String begin_time, @RequestParam String end_time, @RequestParam(required = false) String status, HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");
        return customerService.getOrders(begin_time, end_time, status, user_id);
    }

    /**
     * 获取订单详情
     *
     * @param order_id 订单ID
     * @param session  会话信息
     * @return
     */
    @GetMapping("/order/{order_id}")
    @UserAccessRequired
    public Result getOrderDetail(@PathVariable Integer order_id, HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");
        return customerService.getOrderById(order_id, user_id);
    }

    /**
     * 获取积分信息
     *
     * @param session 会话信息
     * @return
     */
    @UserAccessRequired
    @GetMapping("/points")
    public Result getPoints(HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");
        return customerService.getPointsDetail(user_id);
    }

    @UserAccessRequired
    @RequestMapping("/discount")
    public Result getDiscount(HttpSession session) {
        String userId = (String) session.getAttribute("user_id");
        return customerService.getDiscount(userId);
    }

    @UserAccessRequired
    @RequestMapping("/vip-level/point-rate")
    Result getPointRate(HttpSession session) {
        String userId = (String) session.getAttribute("user_id");
        return customerService.getPointRate(userId);
    }
}
