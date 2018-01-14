package com.onlineshopping.controller.backmanage;

import com.onlineshopping.annotation.ManagerAccessRequired;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.backmanage.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * created by lsy
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    @Resource
    private ManagerService managerService;

    /**
     * 管理员登录
     * @param manager_id    管理员ID
     * @param password      密码
     * @param session       session
     * @param response      响应信息
     * @return
     */
    @GetMapping
    public Result login(String manager_id, String password, HttpSession session, HttpServletResponse response) {
        logger.info("管理员" + manager_id + "使用密码" + password + "登录");
        Result result = managerService.login(manager_id, password);
        if (result.getResultCode() == 0) {
            Cookie cookie = new Cookie("JSESSIONID",session.getId());
            cookie.setMaxAge(1800);
            response.addCookie(cookie);

//            cookie = new Cookie("manager_id",manager_id);
//            cookie.setMaxAge(1800);
//            response.addCookie(cookie);

            session.setAttribute("manager_id", manager_id);
        }
        return result;
    }

    /**
     * 管理员登出
     * @param session session
     * @return
     */
    @PutMapping
    @ManagerAccessRequired
    public Result logout(HttpSession session) {
        logger.info("管理员" + session.getAttribute("manager_id") + "退出");
        session.removeAttribute("manager_id");
        return managerService.logout();
    }
}
