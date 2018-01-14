package com.onlineshopping.service.impl.backmanage;

import com.onlineshopping.dao.ManagerDao;
import com.onlineshopping.entity.Manager;
import com.onlineshopping.entity.Result;
import com.onlineshopping.service.backmanage.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lsy
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    private Result result = new Result();
    private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    @Resource
    private ManagerDao ManagerDao;

    /**
     * 管理员登录
     * @param manager_id    管理员ID
     * @param password      密码
     * @return
     */
    @Override
    public Result login(String manager_id, String password) {
        Manager realManager = ManagerDao.get(Manager.class, manager_id);

        if (realManager == null) {
            result.setResultCode(-1);
            result.setResultInfo("管理员" + manager_id + "不存在！");
            logger.error(result.getResultInfo());
            return result;
        }

        if (!realManager.getPassword().equals(password)) {
            result.setResultCode(-2);
            result.setResultInfo("密码错误！");
            logger.error(result.getResultInfo());
            return result;
        }


        result.setResultCode(0);
        result.setResultInfo("登录成功！");
        logger.info(result.getResultInfo());
        return result;
    }

    /**
     * 管理员登出
     * @return
     */
    @Override
    public Result logout() {
        result.setResultCode(0);
        result.setResultInfo("退出成功！");
        return result;
    }
}
