package com.onlineshopping.interceptor;

import com.onlineshopping.annotation.ManagerAccessRequired;
import com.onlineshopping.annotation.UserAccessRequired;
import com.onlineshopping.controller.backmanage.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Created by jacky on 17-6-15.
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        UserAccessRequired userAccessRequired = method.getAnnotation(UserAccessRequired.class);
        ManagerAccessRequired managerAccessRequired = method.getAnnotation(ManagerAccessRequired.class);
        HttpSession session = httpServletRequest.getSession();
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setMaxAge(60 * 60 * 5);
        cookie.setHttpOnly(true);
        cookie.setPath(httpServletRequest.getContextPath());
        httpServletResponse.addCookie(cookie);
        if (userAccessRequired != null) {
            String userId = (String) session.getAttribute("user_id");
            if (userId != null) {
                return true;
            } else {
                // 返回错误码
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
            }
            return false;
        }
        if (managerAccessRequired != null) {
            String managerId = (String) session.getAttribute("manager_id");
            if (managerId != null) {
                return true;
            } else {
                // 返回错误码
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "管理员未登录");
                logger.error("未登录！");

            }
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
